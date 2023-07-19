package com.example.town_application.utility.requests.personalData;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ViewDataRequestUser {

    @NotBlank (message = "Błąd: Pole nie może być puste")
    private String token;


}
