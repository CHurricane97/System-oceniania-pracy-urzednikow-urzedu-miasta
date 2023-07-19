package com.example.town_application.utility.requests.personalData;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@Getter
@Setter
public class ViewDataRequestAdmin {


    @NotBlank (message = "Błąd: Pole nie może być puste")
    @Size(min = 11, max = 11, message = "Pesel musi mieć 11 znaków")
    private String pesel;


}
