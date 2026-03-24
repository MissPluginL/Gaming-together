package com.gamerr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gamerr.dto.EvaluationCreateDTO;
import com.gamerr.entity.Evaluation;
import com.gamerr.entity.ServiceOrder;

import java.util.List;

public interface ServiceOrderService extends IService<ServiceOrder> {

    ServiceOrder createOrder(Long publisherId, ServiceOrder order);

    List<ServiceOrder> listOpenOrders();

    List<ServiceOrder> listOpenOrdersByGame(String gameName);

    List<ServiceOrder> listMyPublished(Long userId);

    List<ServiceOrder> listMyTaking(Long userId);

    ServiceOrder getDetail(Long id);

    void payOrder(Long orderId, Long userId);

    void acceptOrder(Long orderId, Long takerId);

    /** 打手上传完成凭据（不再直接完成订单，交由管理员审核） */
    void submitProof(Long orderId, Long userId, String proofUrls);

    void cancelOrder(Long orderId, Long userId);

    Evaluation evaluateOrder(Long orderId, Long userId, EvaluationCreateDTO dto);
}
