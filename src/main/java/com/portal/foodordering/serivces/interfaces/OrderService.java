package com.portal.foodordering.serivces.interfaces;

import com.portal.foodordering.models.dtos.OrderDTO;

import java.util.List;

public interface OrderService {

    OrderDTO createOrder(Long userId, Long itemId, int itemQuantity);
    List<OrderDTO> getAllOrders();
    String deleteOrder(Long id);
    OrderDTO findById(Long id);
    List<OrderDTO> findOrderByUserId(Long userId);
    OrderDTO addItemToOrder(Long orderId,Long itemId, int quantity);
    OrderDTO removeItemFromOrder(Long orderId, Long itemId, int quantity);
    String processPaymentConfirmation(Long id, String status);
}