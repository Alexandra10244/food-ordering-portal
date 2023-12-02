package com.portal.foodordering.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.portal.foodordering.models.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(name = "order_created_at")
    @CreationTimestamp
    private LocalDateTime orderCreatedAt;

    @Column(name = "order_delivered_at")
    private LocalDateTime orderDeliveredAt;

    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "number_of_items")
    private int noOfItems;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "order_item",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private Set<Item> itemSetOrder = new HashSet<>();
}

