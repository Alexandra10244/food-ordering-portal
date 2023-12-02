package com.portal.foodordering.controllers;

import com.portal.foodordering.models.dtos.OrderDTO;
import com.portal.foodordering.serivces.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Validated @RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.createOrder(orderDTO));
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.deleteOrder(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> findOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<OrderDTO> findOrderByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.findById(userId));
    }

    @PatchMapping
    public ResponseEntity<OrderDTO> addItemsToOrder(@PathVariable Long orderId, @PathVariable Long itemId) {
        return ResponseEntity.ok(orderService.addItemToOrder(orderId, itemId));
    }

    @DeleteMapping("/{order_id}/{item_id}")
    public ResponseEntity<OrderDTO> removeItemFromOrder(@PathVariable Long orderId, @PathVariable Long itemId) {
        return ResponseEntity.ok(orderService.removeItemFromOrder(orderId, itemId));
    }
}

