package com.example.town_application.model.dto.evaluationDTOs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class EvaluationPublicWithAvgAndCounter {

    private String personalDataForEvaluationName;
    private String personalDataForEvaluationSurname;
    private List<EvaluationPublicPersonalDTO> evaluationList;
    private Double average;
    private Double averageRounded;
    private Long count;



}
