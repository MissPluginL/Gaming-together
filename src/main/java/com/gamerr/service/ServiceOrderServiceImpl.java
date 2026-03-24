package com.gamerr.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gamerr.dto.EvaluationCreateDTO;
import com.gamerr.entity.Evaluation;
import com.gamerr.entity.Friendship;
import com.gamerr.entity.ServiceOrder;
import com.gamerr.exception.BusinessException;
import com.gamerr.mapper.EvaluationMapper;
import com.gamerr.mapper.ServiceOrderMapper;
import com.gamerr.service.impl.FriendshipServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServiceOrderServiceImpl extends ServiceImpl<ServiceOrderMapper, ServiceOrder> implements ServiceOrderService {

    @Autowired
    private EvaluationMapper evaluationMapper;

    @Autowired
    private FriendshipServiceImpl friendshipService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceOrder createOrder(Long publisherId, ServiceOrder order) {
        if (publisherId == null) {
            throw new BusinessException("请先登录");
        }
        order.setId(null);
        order.setPublisherId(publisherId);
        order.setStatus(0);
        order.setPaid(0);
        order.setTakerId(null);
        this.save(order);
        return order;
    }

    @Override
    public List<ServiceOrder> listOpenOrders() {
        QueryWrapper<ServiceOrder> qw = new QueryWrapper<>();
        qw.eq("status", 0).eq("paid", 1).orderByDesc("create_time");
        return this.list(qw);
    }

    @Override
    public List<ServiceOrder> listOpenOrdersByGame(String gameName) {
        QueryWrapper<ServiceOrder> qw = new QueryWrapper<>();
        qw.eq("status", 0).eq("paid", 1);
        if (gameName != null && !gameName.trim().isEmpty()) {
            qw.eq("game_name", gameName.trim());
        }
        qw.orderByDesc("create_time");
        return this.list(qw);
    }

    @Override
    public List<ServiceOrder> listMyPublished(Long userId) {
        if (userId == null) {
            throw new BusinessException("请先登录");
        }
        QueryWrapper<ServiceOrder> qw = new QueryWrapper<>();
        qw.eq("publisher_id", userId).orderByDesc("create_time");
        return this.list(qw);
    }

    @Override
    public List<ServiceOrder> listMyTaking(Long userId) {
        if (userId == null) {
            throw new BusinessException("请先登录");
        }
        QueryWrapper<ServiceOrder> qw = new QueryWrapper<>();
        qw.eq("taker_id", userId).orderByDesc("create_time");
        return this.list(qw);
    }

    @Override
    public ServiceOrder getDetail(Long id) {
        ServiceOrder o = this.getById(id);
        if (o == null) {
            throw new BusinessException("订单不存在");
        }
        return o;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payOrder(Long orderId, Long userId) {
        ServiceOrder o = getDetail(orderId);
        if (!userId.equals(o.getPublisherId())) {
            throw new BusinessException("仅发单人可发起支付");
        }
        if (o.getStatus() != 0) {
            throw new BusinessException("当前状态不可支付");
        }
        o.setPaid(1);
        this.updateById(o);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void acceptOrder(Long orderId, Long takerId) {
        ServiceOrder o = getDetail(orderId);
        if (takerId.equals(o.getPublisherId())) {
            throw new BusinessException("不能接自己的单");
        }
        if (o.getStatus() != 0 || o.getPaid() != 1) {
            throw new BusinessException("订单不可接单（需已支付展示费）");
        }

        Long publisherId = o.getPublisherId();

        o.setTakerId(takerId);
        o.setStatus(1);
        this.updateById(o);

        // 自动添加好友关系
        // 打手视角：对方是单主（类型1）
        addFriendIfNotExists(takerId, publisherId, 1);
        // 单主视角：对方是陪玩/代练（类型2）
        addFriendIfNotExists(publisherId, takerId, 2);
    }

    /**
     * 添加好友关系（如果不存在）
     */
    private void addFriendIfNotExists(Long userId, Long friendId, Integer relationType) {
        try {
            if (!friendshipService.isFriend(userId, friendId)) {
                Friendship friendship = new Friendship();
                friendship.setUserId(userId);
                friendship.setFriendId(friendId);
                friendship.setRelationType(relationType);
                friendshipService.save(friendship);
            }
        } catch (Exception e) {
            // 好友关系添加失败不影响订单接单
            // 可能已经存在好友关系
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitProof(Long orderId, Long userId, String proofUrls) {
        ServiceOrder o = getDetail(orderId);
        if (!userId.equals(o.getTakerId())) {
            throw new BusinessException("仅接单人可提交完成凭据");
        }
        if (o.getStatus() != 1) {
            throw new BusinessException("订单状态不正确，无法提交凭据");
        }
        o.setProofUrls(proofUrls);
        o.setSubmitted(1);
        o.setAuditStatus(0);
        this.updateById(o);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId, Long userId) {
        ServiceOrder o = getDetail(orderId);
        if (!userId.equals(o.getPublisherId())) {
            throw new BusinessException("仅发单人可取消");
        }
        if (o.getStatus() != 0) {
            throw new BusinessException("进行中或已完成订单不可取消");
        }
        o.setStatus(3);
        this.updateById(o);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Evaluation evaluateOrder(Long orderId, Long userId, EvaluationCreateDTO dto) {
        ServiceOrder o = getDetail(orderId);
        if (o.getStatus() != 2) {
            throw new BusinessException("订单未完成，不能评价");
        }
        Long toUserId;
        if (userId.equals(o.getPublisherId())) {
            toUserId = o.getTakerId();
        } else if (userId.equals(o.getTakerId())) {
            toUserId = o.getPublisherId();
        } else {
            throw new BusinessException("无权评价");
        }
        if (toUserId == null) {
            throw new BusinessException("对方用户不存在");
        }
        QueryWrapper<Evaluation> qw = new QueryWrapper<>();
        qw.eq("order_id", orderId).eq("from_user_id", userId);
        if (evaluationMapper.selectCount(qw) > 0) {
            throw new BusinessException("您已评价过该订单");
        }
        Evaluation ev = new Evaluation();
        ev.setOrderId(orderId);
        ev.setFromUserId(userId);
        ev.setToUserId(toUserId);
        ev.setRating(dto.getRating());
        ev.setComment(dto.getComment());
        evaluationMapper.insert(ev);
        return ev;
    }
}
