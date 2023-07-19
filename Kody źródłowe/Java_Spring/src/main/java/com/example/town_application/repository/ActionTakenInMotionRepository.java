package com.example.town_application.repository;

import com.example.town_application.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionTakenInMotionRepository  extends JpaRepository<ActionTakenInMotion, Integer> {
    Boolean existsByPersonalDataForActionTakenInMotionAndMotionForActionInMotionsAndActionTypeByActionTypeId(PersonalData personalData, Motion motion, ActionType actionType);
    Boolean existsByPersonalDataForActionTakenInMotion_PersonalDataIdAndMotionForActionInMotions_MotionId(int personalDataId, int motionId);
}
