package com.example.town_application.repository;

import com.example.town_application.model.Motion;
import com.example.town_application.model.MotionState;
import com.example.town_application.model.MotionType;
import com.example.town_application.model.PersonalData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotionRepository  extends JpaRepository<Motion, Integer> {

    List<Motion>findAllByPersonalDataForMotionsAndMotionStateByMotionStateIdOrMotionStateByMotionStateId(PersonalData personalData,
    MotionState motionState, MotionState motionState2, PageRequest pageRequest);
    List<Motion>findAllByPersonalDataForMotions(PersonalData personalData, PageRequest pageRequest);
    long countByMotionStateByMotionStateId_StateNotAndMotionStateByMotionStateId_StateNotAndPersonalDataForMotions_Pesel(String state, String state1, String pesel);
    List<Motion> findByMotionStateByMotionStateId_StateNotAndMotionStateByMotionStateId_StateNot(String state, String state1, PageRequest pageRequest);

    long countByPersonalDataForMotions_PersonalDataId(int personalDataId);

    List<Motion> findByPersonalDataForMotions_PeselAndMotionStateByMotionStateId_StateNotAndMotionStateByMotionStateId_StateNot(String pesel, String state, String state1, PageRequest pageRequest);

    List<Motion> findByPersonalDataForMotionsAndMotionStateByMotionStateIdOrPersonalDataForMotionsAndMotionStateByMotionStateId(PersonalData personalDataForMotions, MotionState motionStateByMotionStateId, PersonalData personalDataForMotions1, MotionState motionStateByMotionStateId1, PageRequest pageRequest);




}
