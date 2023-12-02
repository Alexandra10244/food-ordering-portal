package com.portal.foodordering.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "description")
    private String description;

    @Column(name = "number_of_available_items")
    private Integer noOfAvailableItems;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Restaurant> restaurants = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "itemSetOrder", fetch = FetchType.LAZY)
    private Set<Order> orders = new HashSet<>();
}

