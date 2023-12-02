package com.portal.foodordering.serivces.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.foodordering.models.dtos.OrderDTO;
import com.portal.foodordering.models.entities.Item;
import com.portal.foodordering.models.entities.Order;
import com.portal.foodordering.models.enums.PaymentStatus;
import com.portal.foodordering.repositories.ItemRepository;
import com.portal.foodordering.repositories.OrderRepository;
import com.portal.foodordering.serivces.interfaces.OrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
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

    @Transactional
    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = objectMapper.convertValue(orderDTO, Order.class);

        if (order.getNoOfItems() > order.getNoOfItems()) {
            throw new RuntimeException("Insufficient stock for one or more items in the order.");
        }
        double totalAmount = calculateTotalAmount(order.getItemSetOrder(), order.getNoOfItems());

        Order savedOrder = orderRepository.save(order);

        updateStock(order.getItemSetOrder(), order.getNoOfItems());

        return objectMapper.convertValue(savedOrder, OrderDTO.class);
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

    private boolean checkAvailability(Set<Item> items) {
        for (Item item : items) {
            if (item.getNoOfAvailableItems() < 1) {
                throw new RuntimeException("Sorry, the product is no longer available at the moment");
            }
        }
        return true;
    }

    private double calculateTotalAmount(Set<Item> items, Integer noOfItems) {
        double totalAmount = 0.0;
        for (Item item : items) {
            totalAmount += (item.getPrice() * noOfItems);
        }
        return totalAmount;
    }

    private void updateStock(Set<Item> items, int orderedQuantity) {
        for (Item item : items) {
            int remainingStock = item.getNoOfAvailableItems() - orderedQuantity;
            item.setNoOfAvailableItems(remainingStock);

            itemRepository.save(item);
        }
    }

    @Override
    public void processPaymentConfirmation(Long id) {
       Order order = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found"));

       order.setPaymentStatus(PaymentStatus.SUCCESSFUL);

       orderRepository.save(order);
    }
}
