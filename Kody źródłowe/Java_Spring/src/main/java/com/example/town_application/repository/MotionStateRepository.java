package com.example.town_application.repository;

import com.example.town_application.model.MotionState;
import com.example.town_application.model.MotionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MotionStateRepository extends JpaRepository<MotionState, Integer> {
    Boolean existsByState(String state);
    Optional<MotionState> findByState(String state);

}
