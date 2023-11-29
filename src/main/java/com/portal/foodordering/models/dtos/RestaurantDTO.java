package com.portal.foodordering.models.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RestaurantDTO {
    private Long id;

    @NotBlank(message = "This field must not be empty!")
    private String name;

    @NotBlank(message = "This field must not be empty!")
    private String cuisine;
}

