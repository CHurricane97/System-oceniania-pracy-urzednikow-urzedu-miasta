package com.example.town_application.utility.requests.account;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
@Getter
@Setter
public class ChangePasswordUserReqest {


    @NotBlank (message = "Błąd: Pole nie może być puste")
    private String password;

    @NotBlank (message = "Błąd: Pole nie może być puste")
    private String newpassword;


}
