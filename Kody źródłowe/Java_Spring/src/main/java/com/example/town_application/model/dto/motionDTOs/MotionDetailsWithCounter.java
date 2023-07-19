package com.example.town_application.model.dto.motionDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MotionDetailsWithCounter {


    private List<MotionDetails> motionDetailsList;
    private Integer count;


}
