package com.example.town_application.model.dto.evaluationDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluationMadeByUser {

    private int evaluationId;
    private int grade;
    private String description;
    private String motionForEvaluationMotionType;
    private int personalDataForEvaluationID;
    private String personalDataForEvaluationName;
    private String personalDataForEvaluationSurname;

}
