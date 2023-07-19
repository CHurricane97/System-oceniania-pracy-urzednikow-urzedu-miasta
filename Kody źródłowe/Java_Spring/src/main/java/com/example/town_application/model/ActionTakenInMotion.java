package com.example.town_application.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "action_taken_in_motion", schema = "public", catalog = "Town_Database")
public class ActionTakenInMotion {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "action_taken_in_motion_id", nullable = false)
    private int actionTakenInMotionId;
    @ManyToOne(targetEntity = Motion.class)
    @JoinColumn(name = "motion_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Motion motionForActionInMotions;
    @ManyToOne(targetEntity = PersonalData.class)
    @JoinColumn(name = "personal_data_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PersonalData personalDataForActionTakenInMotion;
    @ManyToOne(targetEntity = ActionType.class)
    @JoinColumn(name = "action_type_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ActionType actionTypeByActionTypeId;

    public ActionTakenInMotion(Motion motionForActionInMotions, PersonalData personalDataForActionTakenInMotion, ActionType actionTypeByActionTypeId) {
        this.motionForActionInMotions = motionForActionInMotions;
        this.personalDataForActionTakenInMotion = personalDataForActionTakenInMotion;
        this.actionTypeByActionTypeId = actionTypeByActionTypeId;
    }

    public ActionTakenInMotion() {

    }
}