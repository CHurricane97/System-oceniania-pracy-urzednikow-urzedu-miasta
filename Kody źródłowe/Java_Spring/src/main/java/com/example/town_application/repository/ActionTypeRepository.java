package com.example.town_application.repository;

import com.example.town_application.model.ActionType;
import com.example.town_application.model.MotionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ActionTypeRepository  extends JpaRepository<ActionType, Integer> {
    Boolean existsByType(String type);
    Optional<ActionType> findByType(String type);
}
