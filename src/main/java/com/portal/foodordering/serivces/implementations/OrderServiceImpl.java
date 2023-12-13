package com.portal.foodordering.serivces.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.foodordering.exceptions.*;
import com.portal.foodordering.models.dtos.OrderDTO;
import com.portal.foodordering.models.entities.Item;
import com.portal.foodordering.models.entities.Order;
import com.portal.foodordering.models.entities.User;
import com.portal.foodordering.models.enums.PaymentStatus;
import com.portal.foodordering.repositories.ItemRepository;
import com.portal.foodordering.repositories.OrderRepository;
import com.portal.foodordering.repositories.UserRepository;
import com.portal.foodordering.serivces.interfaces.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public OrderDTO createOrder(Long userId, Long itemId, int itemQuantity) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found!"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Item not found!"));
        Order order = new Order();

        if (itemQuantity > item.getNoOfAvailableItems()) {
            throw new InsufficientStockException("Insufficient stock for one or more items in the order.");
        }
        double totalAmount = 0;
        totalAmount += itemQuantity * item.getPrice();
        order.setUser(user);
        order.setNoOfItems(itemQuantity);
        order.setTotalAmount(totalAmount);
        order.getItems().add(item);
        order.setPaymentStatus(PaymentStatus.UNPAID);

        Order savedOrder = orderRepository.save(order);

        updateStock(order.getItems(), itemQuantity);

        OrderDTO responseOrderDTO = new OrderDTO();
        responseOrderDTO.setId(savedOrder.getId());
        responseOrderDTO.setOrderCreatedAt(savedOrder.getOrderCreatedAt());
        responseOrderDTO.setUserID(user.getId());
        responseOrderDTO.setNoOfItems(savedOrder.getNoOfItems());
        responseOrderDTO.setTotalAmount(totalAmount);
        responseOrderDTO.setPaymentStatus(savedOrder.getPaymentStatus());

        return responseOrderDTO;
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> orderDTOS = new ArrayList<>();
        for (Order order : orders) {
            orderDTOS.add(new OrderDTO(order.getId(), order.getUser().getId(), order.getOrderCreatedAt(), order.getTotalAmount(),
                    order.getNoOfItems(), order.getPaymentStatus()));
        }

        return orderDTOS;
    }

    @Override
    public String deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);

            return "Order successfully deleted!";
        } else {
            throw new OrderNotFoundException("Order not found!");
        }
    }

    @Override
    public OrderDTO findById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found!"));
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setUserID(order.getUser().getId());
        orderDTO.setOrderCreatedAt(order.getOrderCreatedAt());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setNoOfItems(order.getNoOfItems());
        orderDTO.setPaymentStatus(order.getPaymentStatus());
        return orderDTO;
    }

    @Override
    public List<OrderDTO> findOrderByUserId(Long userId) {
        List<Order> orders = orderRepository.findOrdersByUserId(userId);
        List<OrderDTO> ordersDTO = new ArrayList<>();
        for (Order element : orders) {
            ordersDTO.add(new OrderDTO(element.getId(), element.getUser().getId(), element.getOrderCreatedAt(), element.getTotalAmount(), element.getNoOfItems(), element.getPaymentStatus()));
        }
        return ordersDTO;
    }

    @Override
    public OrderDTO addItemToOrder(Long orderId, Long itemId, int quantity) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId + "."));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Item not found with id: " + itemId + "."));
        Item itemForOrder = new Item();
        if (item.getNoOfAvailableItems() > 0) {
            order.getItems().add(item);
            order.setTotalAmount(order.getTotalAmount() + (item.getPrice() * quantity));
            order.setNoOfItems(order.getNoOfItems() + quantity);
        } else {
            throw new ProductNotAvailableException("Sorry, the product is no longer available at the moment");
        }
        Order updatedOrder = orderRepository.save(order);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(updatedOrder.getId());
        orderDTO.setUserID(updatedOrder.getUser().getId());
        orderDTO.setOrderCreatedAt(updatedOrder.getOrderCreatedAt());
        orderDTO.setPaymentStatus(updatedOrder.getPaymentStatus());
        orderDTO.setNoOfItems(updatedOrder.getNoOfItems());
        orderDTO.setTotalAmount(updatedOrder.getTotalAmount());
        return orderDTO;
    }

    @Override
    public OrderDTO removeItemFromOrder(Long orderId, Long itemId, int quantity) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId + "."));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Item not found with id: " + itemId + "."));

        if (order.getItems().contains(item) && !order.getPaymentStatus().equals("SUCCESSFUL")) {
            order.getItems().remove(item);
            order.setNoOfItems(order.getNoOfItems() - quantity);
            order.setTotalAmount(order.getTotalAmount() - (quantity * item.getPrice()));

            Order updateOrder = orderRepository.save(order);

            return objectMapper.convertValue(updateOrder, OrderDTO.class);
        } else {
            throw new ItemCantBeRemovedException("Invalid operation: Item with id " + itemId + " cannot be removed from Order " + orderId + ".");
        }
    }

    private void updateStock(Set<Item> items, int orderedQuantity) {
        for (Item item : items) {
            int remainingStock = item.getNoOfAvailableItems() - orderedQuantity;
            item.setNoOfAvailableItems(remainingStock);

            itemRepository.save(item);
        }
    }

    @Override
    public String processPaymentConfirmation(Long id, String status) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found!"));

        if (status.equalsIgnoreCase("PAID")) {
            order.setPaymentStatus(PaymentStatus.PAID);
        } else if (status.equalsIgnoreCase("UNPAID")) {
            order.setPaymentStatus(PaymentStatus.UNPAID);
        } else if (status.equalsIgnoreCase("FAILED")) {
            order.setPaymentStatus(PaymentStatus.FAILED);
        } else {
            throw new PaymentStatusException("Invalid payment status!");
        }

        orderRepository.save(order);
        return order.getPaymentStatus().toString();
    }
}
