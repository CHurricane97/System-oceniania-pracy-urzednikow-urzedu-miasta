package com.example.town_application.utility.requests.account;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class SignupRequest {

    @NotBlank (message = "Błąd: Pole nie może być puste")

    private String login;

    @NotBlank(message = "Błąd: Pole nie może być puste")

    private String password;

    @NotBlank(message = "Błąd: Pole nie może być puste")

    private String role;

    @NotBlank (message = "Błąd: Pole nie może być puste")
    @Size(min = 11, max = 11, message = "Pesel musi mieć 11 znaków")
    private String pesel;




}
