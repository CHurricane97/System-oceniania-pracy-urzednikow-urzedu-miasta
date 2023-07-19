package com.example.town_application.utility.requests.evaluation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteEvaluationRequest {
    private Integer workerId;
    private Integer motionId;
}
