package com.example.town_application.model.dto.actionDTOs;

import com.example.town_application.model.dto.motionDTOs.MotionIDOnly;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActionName {
    private String type;

    private MotionIDOnly motionForActionInMotions;

}
