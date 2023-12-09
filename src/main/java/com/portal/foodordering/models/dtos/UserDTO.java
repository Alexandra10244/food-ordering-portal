package com.portal.foodordering.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;

    @NotBlank(message = "This field must not be empty!")
    @Size(min = 2, message = "Invalid first name")
    private String firstName;

    @NotBlank(message = "This field must not be empty!")
    @Size(min = 2, message = "Invalid last name")
    private String lastName;

    @NotBlank(message = "This field must not be empty!")
    private String phoneNumber;

    @NotBlank(message = "This field must not be empty!")
    private String email;

    @NotBlank(message = "This field must not be empty!")
    private String address;
}

