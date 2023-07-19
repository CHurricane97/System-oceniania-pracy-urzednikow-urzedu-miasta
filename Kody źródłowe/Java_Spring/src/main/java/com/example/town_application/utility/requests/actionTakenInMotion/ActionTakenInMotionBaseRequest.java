package com.example.town_application.utility.requests.actionTakenInMotion;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
public class ActionTakenInMotionBaseRequest {
    @Min(value = 1, message = "ID wniosku musi być większe niż 0")
    Integer motionID;
}
