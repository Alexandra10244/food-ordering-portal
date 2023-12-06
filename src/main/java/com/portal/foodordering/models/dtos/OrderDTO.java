package com.portal.foodordering.models.dtos;

import com.portal.foodordering.models.entities.Item;
import com.portal.foodordering.models.enums.PaymentStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class OrderDTO {
    private Long id;
    private Long restaurantId;
    private Long userID;
    private LocalDateTime orderCreatedAt;
    @Min(1)
    private int noOfItems;
    private double totalAmount;
    private PaymentStatus paymentStatus;

}
