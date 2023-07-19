package com.example.town_application.utility.requests.evaluation;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Getter
@Setter
public class EditEvaluationRequest {
    private Integer workerId;
    private Integer motionId;
    @Min(value = 1, message = "Ocena musi być w skali 1-10")
    @Max(value = 10,message ="Ocena musi być w skali 1-10")
    private Integer grade;
    @Size(max = 1023, message = "Zbyt długi tekst")
    private String description;
}
