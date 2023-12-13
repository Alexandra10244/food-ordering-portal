package com.portal.foodordering.controllers;

import com.portal.foodordering.exceptions.ItemNotFoundException;
import com.portal.foodordering.exceptions.OrderNotFoundException;
import com.portal.foodordering.models.dtos.OrderDTO;
import com.portal.foodordering.serivces.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{userId}/{itemId}")
    public ResponseEntity<OrderDTO> createOrder(@PathVariable Long userId,
                                                @PathVariable Long itemId,
                                                @RequestParam int quantity) {
        return ResponseEntity.ok(orderService.createOrder(userId, itemId, quantity));
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.deleteOrder(id));
    }

    @GetMapping("/by-id")
    public ResponseEntity<OrderDTO> findOrderById(@RequestParam Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<OrderDTO>> findOrderByUser(@RequestParam Long userId) {
        return ResponseEntity.ok(orderService.findOrderByUserId(userId));
    }

    @PatchMapping
    public ResponseEntity<OrderDTO> addItemsToOrder(@RequestParam Long orderId,
                                                    @RequestParam Long itemId,
                                                    @RequestParam int quantity) {
        return ResponseEntity.ok(orderService.addItemToOrder(orderId, itemId, quantity));
    }

    @DeleteMapping("/{order_id}/{item_id}")
    public ResponseEntity<OrderDTO> removeItemFromOrder(@RequestParam Long orderId,
                                                        @RequestParam Long itemId,
                                                        @RequestParam int quantity) {
        return ResponseEntity.ok(orderService.removeItemFromOrder(orderId, itemId, quantity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> processPayment(@PathVariable Long id,
                                                 @RequestParam String option) {
        return ResponseEntity.ok(orderService.processPaymentConfirmation(id, option));
    }

//    @PatchMapping("/{orderId}/order-item/{itemId}")
//    public ResponseEntity<OrderDTO> addItem(@PathVariable Long orderId,
//                                            @PathVariable Long itemId) {
//        return  ResponseEntity.ok(orderService.addItemToOrder(orderId,itemId));
//    }
}

