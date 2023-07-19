package com.example.town_application.model.dto.personalDataDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class WorkerDataWithCounter {

    Long count;
    List<WorkerData> workerList;
}
