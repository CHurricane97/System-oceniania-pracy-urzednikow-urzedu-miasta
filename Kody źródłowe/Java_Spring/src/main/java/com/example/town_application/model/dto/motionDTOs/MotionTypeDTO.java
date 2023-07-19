package com.example.town_application.model.dto.motionDTOs;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;

@Getter
@Setter
public class MotionTypeDTO {
    private int motionTypeId;
    private String type;

}
