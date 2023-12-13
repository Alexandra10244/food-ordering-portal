package com.portal.foodordering.models.dtos;

import lombok.Data;

@Data
public class EditItem {

    private Long id;
    private String name;
    private double price;
    private String description;
    private Integer noOfAvailableItems;
}
