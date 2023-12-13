package com.portal.foodordering.models.dtos;

import lombok.Data;

@Data
public class EditUserDTO {

    private String firstName;
    private String lastName;

    private String phoneNumber;

    private String email;

    private String address;
}
