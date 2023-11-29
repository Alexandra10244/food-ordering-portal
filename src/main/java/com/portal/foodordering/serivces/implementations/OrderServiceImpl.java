package com.portal.foodordering.serivces.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.foodordering.models.dtos.OrderDTO;
import com.portal.foodordering.models.entities.Item;
import com.portal.foodordering.models.entities.Order;
import com.portal.foodordering.repositories.ItemRepository;
import com.portal.foodordering.repositories.OrderRepository;
import com.portal.foodordering.serivces.interfaces.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final ItemRepository itemRepository;

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = orderRepository.save(objectMapper.convertValue(orderDTO, Order.class));

        return objectMapper.convertValue(order, OrderDTO.class);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders
                .stream()
                .map(order -> objectMapper.convertValue(order, OrderDTO.class)).toList();
    }

    @Override
    public String deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);

            return "Order successfully deleted!";
        } else {
            throw new EntityNotFoundException("Order not found!");
        }
    }

    @Override
    public OrderDTO findById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found"));

        return objectMapper.convertValue(order, OrderDTO.class);
    }

    @Override
    public List<OrderDTO> findOrderByUserId(Long userId) {
        List<Order> orders = orderRepository.findOrdersByUserId(userId);
        List<OrderDTO> ordersDTO = new ArrayList<>();
        for (Order element : orders) {
            ordersDTO.add(objectMapper.convertValue(element, OrderDTO.class));
        }
        return ordersDTO;
    }

    @Override
    public OrderDTO addItemToOrder(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + itemId));

        order.getItemSetOrder().add(item);
        Order orderUpdated = orderRepository.save(order);

        return objectMapper.convertValue(orderUpdated, OrderDTO.class);
    }

    @Override
    public OrderDTO removeItemFromOrder(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + itemId));

        if (order.getItemSetOrder().contains(item) && !order.getPaymentStatus().equals("SUCCESSFUL")) {
            order.getItemSetOrder().remove(item);

            Order updateOrder = orderRepository.save(order);

            return objectMapper.convertValue(updateOrder, OrderDTO.class);
        } else {
            throw new IllegalStateException("Invalid operation: Item with id " + itemId + " cannot be removed from Order " + orderId);
        }
    }
}
