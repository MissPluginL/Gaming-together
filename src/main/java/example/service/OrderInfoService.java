package example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import example.dto.OrderCreateDTO;

public interface OrderInfoService {
    Object createOrder(Long userId, OrderCreateDTO dto);
    IPage<?> userOrderList(Long userId);
    IPage<?> playerOrderList(Long userId);
    void acceptOrder(Long id, Long playerId);
    void rejectOrder(Long id, Long playerId);
    void completeOrder(Long id, Long userId);
    void cancelOrder(Long id, Long userId);
    Object getOrderDetail(Long id, Long userId);
}