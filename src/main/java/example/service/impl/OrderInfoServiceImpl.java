package example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import example.dto.OrderCreateDTO;
import example.entity.OrderInfo;
import example.mapper.OrderInfoMapper;
import example.service.OrderInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Override
    public Object createOrder(Long userId, OrderCreateDTO dto) {
        OrderInfo order = new OrderInfo();
        order.setUserId(userId);
        orderInfoMapper.insert(order);
        return order;
    }

    @Override
    public IPage<?> userOrderList(Long userId) { return null; }

    @Override
    public IPage<?> playerOrderList(Long userId) { return null; }

    @Override
    public void acceptOrder(Long id, Long playerId) {}

    @Override
    public void rejectOrder(Long id, Long playerId) {}

    @Override
    public void completeOrder(Long id, Long userId) {}

    @Override
    public void cancelOrder(Long id, Long userId) {}

    @Override
    public Object getOrderDetail(Long id, Long userId) { return null; }
}