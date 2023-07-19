package com.example.town_application.utility.requests.personalData;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdatePersonRequest {

    @NotBlank (message = "Błąd: Pole nie może być puste")
    private String name;

    @NotBlank (message = "Błąd: Pole nie może być puste")
    private String surname;

    @NotBlank (message = "Błąd: Pole nie może być puste")
    @Size(min = 11, max = 11, message = "Pesel musi mieć 11 znaków")
    private String pesel;

    @NotBlank (message = "Błąd: Pole nie może być puste")
    private String city;

    @NotBlank (message = "Błąd: Pole nie może być puste")
    private String city_code;

    @NotBlank (message = "Błąd: Pole nie może być puste")
    private String street;

    @NotBlank (message = "Błąd: Pole nie może być puste")
    private String house_number;

    @NotBlank (message = "Błąd: Pole nie może być puste")
    private String flat_number;
}
