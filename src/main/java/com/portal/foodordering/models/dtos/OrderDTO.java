package com.portal.foodordering.models.dtos;

import com.portal.foodordering.models.entities.Item;
import com.portal.foodordering.models.enums.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class OrderDTO {
    private Long id;
    @NotBlank(message = "This field must not be empty!")
    private Long restaurantId;
    @NotBlank(message = "This field must not be empty!")
    private Long userID;
    private LocalDateTime orderCreatedAt;
    private LocalDateTime orderDeliveredAt;
    @NotBlank(message = "This field must not be empty!")
    @Size(min = 1, message = "Invalid quantity")
    private int noOfItems;
    private double totalAmount;

}
