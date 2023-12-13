package com.portal.foodordering.models.dtos;

import com.portal.foodordering.models.entities.Item;
import com.portal.foodordering.models.enums.PaymentStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private Long userID;
    private LocalDateTime orderCreatedAt;
    private double totalAmount;
    private int noOfItems;
    private PaymentStatus paymentStatus;

}
