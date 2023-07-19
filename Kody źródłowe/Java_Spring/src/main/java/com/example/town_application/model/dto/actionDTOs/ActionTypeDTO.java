package com.example.town_application.model.dto.actionDTOs;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
@Getter
@Setter
public class ActionTypeDTO {
    private int actionTypeId;

    private String type;
}
