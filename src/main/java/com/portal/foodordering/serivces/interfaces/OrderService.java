package com.portal.foodordering.serivces.interfaces;

import com.portal.foodordering.models.dtos.OrderDTO;

import java.util.List;

public interface OrderService {

    OrderDTO createOrder(OrderDTO orderDTO);
    List<OrderDTO> getAllOrders();
    String deleteOrder(Long id);
    OrderDTO findById(Long id);
    List<OrderDTO> findOrderByUserId(Long userId);
    OrderDTO addItemToOrder(Long orderId,Long itemId);
    OrderDTO removeItemFromOrder(Long orderId, Long itemId);
    void processPaymentConfirmation(Long id);
}