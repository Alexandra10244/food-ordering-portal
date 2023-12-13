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
    @JsonIgnore
    private User user;

    @Column(name = "order_created_at")
    @CreationTimestamp
    private LocalDateTime orderCreatedAt;

    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "number_of_items")
    private int noOfItems;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @JsonIgnore
    private Set<Item> itemSetOrder = new HashSet<>();
}

