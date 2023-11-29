package com.portal.foodordering.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

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

    @Column(name = "quantity")
    private int quantity;

    @ManyToMany
    private List<Restaurant> restaurantList;

    @ManyToMany(mappedBy = "itemSetRestaurant")
    private Set<Restaurant> restaurants = new HashSet<>();

    @ManyToMany(mappedBy = "itemSetOrder", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Order> orders = new HashSet<>();

}

