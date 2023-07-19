package com.example.town_application.utility.requests.utility;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AddMotionTypeRequest {
    @NotBlank(message = "Błąd: Pole nie może być puste")
    private String type;


}
