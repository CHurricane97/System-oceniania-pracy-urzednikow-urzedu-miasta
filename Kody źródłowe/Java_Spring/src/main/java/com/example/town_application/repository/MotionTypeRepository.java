package com.example.town_application.repository;

import com.example.town_application.model.MotionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MotionTypeRepository extends JpaRepository<MotionType, Integer> {
    Boolean existsByType(String type);
    Optional<MotionType>findByType(String type);

    boolean existsByTypeAllIgnoreCase(String type);


}
