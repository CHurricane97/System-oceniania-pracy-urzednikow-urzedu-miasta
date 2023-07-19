package com.example.town_application.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Motion {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "motion_id", nullable = false)
    private int motionId;

    @ManyToOne(targetEntity = PersonalData.class)
    @JoinColumn(name = "personal_data_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PersonalData personalDataForMotions;

    @ManyToOne(targetEntity = MotionType.class)
    @JoinColumn(name = "motion_type_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private MotionType motionTypeByMotionTypeId;

    @ManyToOne(targetEntity = MotionState.class)
    @JoinColumn(name = "motion_state_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private MotionState motionStateByMotionStateId;

    public Motion(PersonalData personalDataForMotions, MotionType motionTypeByMotionTypeId, MotionState motionStateByMotionStateId) {
        this.personalDataForMotions = personalDataForMotions;
        this.motionTypeByMotionTypeId = motionTypeByMotionTypeId;
        this.motionStateByMotionStateId = motionStateByMotionStateId;
    }
    public Motion(){

    }
}
