package com.casumo.races.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Full name is mandatory")
    private String fullName;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8)
    private String password;

    @NotBlank(message = "Email is mandatory")
    private String email;
}
