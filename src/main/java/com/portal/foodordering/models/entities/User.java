package com.portal.foodordering.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
    @Entity
    @Table(name = "users")
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "first_name")
        private String firstName;

        @Column(name = "last_name")
        private String lastName;

        @Column(name = "phone_number")
        private String phoneNumber;

        @Column(name = "email", unique = true)
        private String email;

        @Column(name = "address")
        private String address;

        @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
        private Set<Order> orders;

}

