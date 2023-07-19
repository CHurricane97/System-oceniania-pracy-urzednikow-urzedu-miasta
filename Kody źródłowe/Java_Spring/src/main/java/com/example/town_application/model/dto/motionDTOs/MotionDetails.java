package com.example.town_application.model.dto.motionDTOs;

import com.example.town_application.model.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Collection;
@Getter
@Setter
public class MotionDetails {

    private int motionId;
    private String personalDataForMotionsName;
    private String personalDataForMotionsSurname;
    private String personalDataForMotionsID;
    private String motionTypeByMotionTypeType;
    private String motionStateByMotionStateState;

}
