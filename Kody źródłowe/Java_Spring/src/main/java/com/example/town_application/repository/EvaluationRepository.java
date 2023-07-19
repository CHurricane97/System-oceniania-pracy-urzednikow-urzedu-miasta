package com.example.town_application.repository;

import com.example.town_application.model.Evaluation;
import com.example.town_application.model.MotionType;
import com.example.town_application.model.PersonalData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationRepository  extends JpaRepository<Evaluation, Integer> {
    boolean existsByMotionForEvaluation_MotionIdAndPersonalDataForEvaluation_PersonalDataId(int motionId, int personalDataId);

    Optional<Evaluation> findByPersonalDataForEvaluation_PersonalDataIdAndMotionForEvaluation_MotionId(int personalDataId, int motionId);

    @Query("select (count(e) > 0) from Evaluation e where e.personalDataForEvaluation.personalDataId = ?1")

    boolean existsByPersonalDataForEvaluation_PersonalDataId(int personalDataId);

    List<Evaluation> findByPersonalDataForEvaluation_PersonalDataId(int personalDataId, PageRequest pageRequest);

    List<Evaluation> findByMotionForEvaluation_PersonalDataForMotions(PersonalData personalDataForMotions, PageRequest pageRequest);

    long countByPersonalDataForEvaluation_PersonalDataIdAndGrade(int personalDataId, int grade);

    List<Evaluation> findByPersonalDataForEvaluation_PersonalDataIdAndGrade(int personalDataId, int grade, PageRequest pageRequest);

    @Query(value= "SELECT AVG(e.grade) FROM evaluation e WHERE e.personal_data_ID= :pid",nativeQuery = true)
    Double getAVGByPID(@Param("pid") Integer personalID);


}
