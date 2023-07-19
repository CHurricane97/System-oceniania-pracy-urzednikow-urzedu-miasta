package com.example.town_application.utility.requests.account;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
@Getter
@Setter
public class ChangePasswordAdminRequest {

    @NotBlank (message = "Błąd: Pole nie może być puste")
    private String login;

    @NotBlank (message = "Błąd: Pole nie może być puste")
    private String newpassword;



}
