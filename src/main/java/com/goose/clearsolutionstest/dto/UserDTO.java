package com.goose.clearsolutionstest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    @Email(message = "email should be valid")
    @NotBlank(message = "email is required")
    private String email;
    @NotBlank(message = "first name is required and not blank")
    private String firstName;
    @NotBlank(message = "last name is required and not blank")
    private String lastName;
    @NotNull(message = "birth date is required")
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
}
