package com.example.town_application.model.dto.actionDTOs;

import com.example.town_application.model.ActionType;
import com.example.town_application.model.Motion;
import com.example.town_application.model.PersonalData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
public class ActionTakenInMotionDetails {

    private int actionTakenInMotionId;
    private int motionForActionInMotionsId;
    private String motionForActionInMotionsMotionTypeType;
    private String personalDataForActionTakenInMotionName;
    private String personalDataForActionTakenInMotionSurname;
    private String personalDataForActionTakenInMotionID;
    private String actionTypeByActionTypeType;

}
