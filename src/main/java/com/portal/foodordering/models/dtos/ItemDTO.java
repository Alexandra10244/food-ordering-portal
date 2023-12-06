package com.portal.foodordering.models.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemDTO {
    private Long id;

    @NotBlank(message = "This field must not be empty!")
    private String name;

    @Min(0)
    private double price;

    @NotBlank(message = "This field must not be empty!")
    private String description;

    @NotNull(message = "This field must not be empty!")
    private Integer noOfAvailableItems;
}

