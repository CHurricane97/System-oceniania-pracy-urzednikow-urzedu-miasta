package com.example.town_application.repository;

import com.example.town_application.model.PersonalData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PersonalDataRepository extends JpaRepository<PersonalData, Integer>, JpaSpecificationExecutor<PersonalData> {



    Optional<PersonalData> findByPesel(String pesel);

    Optional<PersonalData>findByUsersByPersonalDataId(Integer userID);

    boolean existsByPesel(String pesel);

    Optional<PersonalData> findByPersonalDataId(int personalDataId);

    List<PersonalData> findDistinctByActionTakenInMotionsByPersonalDataId_MotionForActionInMotions_MotionId(int motionId, PageRequest pageRequest);

    List<PersonalData> findDistinctByUsersByPersonalDataId_PermissionLevel(String permissionLevel, PageRequest pageRequest);

    List<PersonalData> findByPeselLike(String pesel, PageRequest pageRequest);

    List<PersonalData> findByPeselLikeIgnoreCase(String pesel, PageRequest pageRequest);

    List<PersonalData> findByPeselContainsIgnoreCase(String pesel, PageRequest pageRequest);









}
