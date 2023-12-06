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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public OrderDTO createOrder(OrderDTO orderDTO, Long itemId, int itemQuantity) {
        User user = userRepository.findById(orderDTO.getUserID()).orElseThrow(() -> new UserNotFoundExceptionException("User not found!"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Item not found!"));
        Order order = new Order();

        if (itemQuantity > item.getNoOfAvailableItems()) {
            throw new InsufficientStockException("Insufficient stock for one or more items in the order.");
        }
        double totalAmount = 0;
        totalAmount += itemQuantity * item.getPrice();
        order.setUser(user);
        order.setNoOfItems(order.getNoOfItems() + itemQuantity);
        order.setTotalAmount(totalAmount);
        order.getItemSetOrder().add(item);

        Order savedOrder = orderRepository.save(order);

        updateStock(order.getItemSetOrder(), itemQuantity);

        OrderDTO responseOrderDTO = new OrderDTO();
        responseOrderDTO.setId(order.getId());
        responseOrderDTO.setOrderCreatedAt(order.getOrderCreatedAt());
        responseOrderDTO.setUserID(user.getId());
        responseOrderDTO.setNoOfItems(order.getNoOfItems());

        return responseOrderDTO;
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
            throw new OrderNotFoundException("Order not found!");
        }
    }

    @Override
    public OrderDTO findById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found!"));

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
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId + "."));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Item not found with id: " + itemId + "."));

        if (checkAvailability(Set.of(item))) {
            order.getItemSetOrder().add(item);
        }
        Order orderUpdated = orderRepository.save(order);

        return objectMapper.convertValue(orderUpdated, OrderDTO.class);
    }

    @Override
    public OrderDTO removeItemFromOrder(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId + "."));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Item not found with id: " + itemId + "."));

        if (order.getItemSetOrder().contains(item) && !order.getPaymentStatus().equals("SUCCESSFUL")) {
            order.getItemSetOrder().remove(item);

            Order updateOrder = orderRepository.save(order);

            return objectMapper.convertValue(updateOrder, OrderDTO.class);
        } else {
            throw new ItemCantBeRemovedException("Invalid operation: Item with id " + itemId + " cannot be removed from Order " + orderId + ".");
        }
    }

    private boolean checkAvailability(Set<Item> items) {
        for (Item item : items) {
            if (item.getNoOfAvailableItems() < 1) {
                throw new ProductNotAvailableException("Sorry, the product is no longer available at the moment");
            }
        }
        return true;
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

        if (status.equalsIgnoreCase("SUCCESSFUL")) {
            order.setPaymentStatus(PaymentStatus.SUCCESSFUL);
        } else if (status.equalsIgnoreCase("REIMBURSED")) {
            order.setPaymentStatus(PaymentStatus.REIMBURSED);
        } else if (status.equalsIgnoreCase("FAILED")) {
            order.setPaymentStatus(PaymentStatus.FAILED);
        } else {
            throw new PaymentStatusExceptionException("Invalid payment status!");
        }

        orderRepository.save(order);
        return order.getPaymentStatus().toString();
    }
}
