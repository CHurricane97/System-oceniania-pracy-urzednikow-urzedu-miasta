package com.example.town_application.service;


import com.example.town_application.utility.JwtUtils;
import com.example.town_application.utility.MessageResponse;
import com.example.town_application.utility.requests.motion.AddMotionRequest;
import com.example.town_application.utility.requests.motion.UpdateMotionStateRequest;
import com.example.town_application.model.*;
import com.example.town_application.model.dto.motionDTOs.MotionDetails;

import com.example.town_application.model.dto.motionDTOs.MotionDetailsWithCounter;
import com.example.town_application.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MotionService {

    private final PersonalDataRepository personalDataRepository;
    private final JwtUtils jwtUtils;
    private final UsersRepository userRepository;
    private final ModelMapper modelMapper;
    private final MotionRepository motionRepository;
    private final MotionStateRepository motionStateRepository;
    private final MotionTypeRepository motionTypeRepository;
    private final ActionTakenInMotionRepository actionTakenInMotionRepository;
    private final ActionTypeRepository actionTypeRepository;

    public List<MotionDetails> getAll(Integer page) {
        return motionRepository.findAll(PageRequest.of(--page, 20)
                )
                .stream()
                .map(mapItem -> modelMapper.map(mapItem, MotionDetails.class))
                .collect(Collectors.toList());
    }


    public List<MotionDetails> getAllNotFinishedMotions(Integer page) {
        return motionRepository.findByMotionStateByMotionStateId_StateNotAndMotionStateByMotionStateId_StateNot("Zatwierdzony", "Odrzucony", PageRequest.of(--page, 20)
                )
                .stream()
                .map(mapItem -> modelMapper.map(mapItem, MotionDetails.class))
                .collect(Collectors.toList());
    }

    public MotionDetailsWithCounter getAllNotFinishedMotionsByPesel(Integer page, String pesel) {
        List<MotionDetails> listaDetali = motionRepository.findByPersonalDataForMotions_PeselAndMotionStateByMotionStateId_StateNotAndMotionStateByMotionStateId_StateNot(pesel, "Zatwierdzony", "Odrzucony", PageRequest.of(--page, 20, Sort.by("motionId").descending())
                )
                .stream()
                .map(mapItem -> modelMapper.map(mapItem, MotionDetails.class)).toList();
        Long liczbaWnioskow = motionRepository.countByMotionStateByMotionStateId_StateNotAndMotionStateByMotionStateId_StateNotAndPersonalDataForMotions_Pesel("Zatwierdzony", "Odrzucony", pesel);
        return new MotionDetailsWithCounter(listaDetali, liczbaWnioskow.intValue());
    }


    public MotionDetailsWithCounter getAllForUser(Integer page, HttpServletRequest request) {
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(JwtUtils.parseJwt(request))).
                orElseThrow(() -> new RuntimeException(("Zły login")));


        PersonalData userPersonalData = users.getPersonalDataForUsers();

        List<MotionDetails> listaDetali = motionRepository.findAllByPersonalDataForMotions(userPersonalData, PageRequest.of(--page, 20, Sort.by("motionId").descending())
                )
                .stream()
                .map(mapItem -> modelMapper.map(mapItem, MotionDetails.class)).toList();
        Long liczbaWnioskow = motionRepository.countByPersonalDataForMotions_PersonalDataId(userPersonalData.getPersonalDataId());
        return new MotionDetailsWithCounter(listaDetali, liczbaWnioskow.intValue());
    }


    public MotionDetailsWithCounter getAllFinishedForUser(Integer page, HttpServletRequest request) {
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(JwtUtils.parseJwt(request))).
                orElseThrow(() -> new RuntimeException("Błąd: Zły login"));
        MotionState motionState1 = motionStateRepository.findByState("Zatwierdzony").
                orElseThrow(() -> new RuntimeException("Błąd: Błąd stanu wniosku"));
        MotionState motionState2 = motionStateRepository.findByState("Odrzucony").
                orElseThrow(() -> new RuntimeException("Błąd: Błąd stanu wniosku"));
        PersonalData userPersonalData = users.getPersonalDataForUsers();

        List<MotionDetails> listaDetali = motionRepository.
                findByPersonalDataForMotionsAndMotionStateByMotionStateIdOrPersonalDataForMotionsAndMotionStateByMotionStateId
                        (userPersonalData,
                                motionState1, userPersonalData, motionState2, PageRequest.of(--page, 20, Sort.by("motionId").descending())
                        )
                .stream()
                .map(mapItem -> modelMapper.map(mapItem, MotionDetails.class))
                .collect(Collectors.toList());




        Long liczbaWnioskow = motionRepository.countByMotionStateByMotionStateId_StateNotAndMotionStateByMotionStateId_StateNotAndPersonalDataForMotions_Pesel("Zatwierdzony", "Odrzucony", userPersonalData.getPesel());
        return new MotionDetailsWithCounter(listaDetali, liczbaWnioskow.intValue());

    }


    public ResponseEntity<?> addMotion(AddMotionRequest addMotionRequest, HttpServletRequest request) {
        if (!personalDataRepository.existsByPesel(addMotionRequest.getPesel())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Błąd: Zły pesel"));
        }
        PersonalData userPersonalData = personalDataRepository.findByPesel(addMotionRequest.getPesel()).
                orElseThrow(() -> new RuntimeException("Błąd: Zły pesel"));

        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(JwtUtils.parseJwt(request))).
                orElseThrow(() -> new RuntimeException("Błąd: Zły login"));

        PersonalData workerPersonalData = users.getPersonalDataForUsers();

        MotionState motionState = motionStateRepository.findByState("Przyjęty").
                orElseThrow(() -> new RuntimeException("Błąd: Błąd stanu wniosku"));

        ActionType actionType = actionTypeRepository.findByType("Przyjęcie wniosku").
                orElseThrow(() -> new RuntimeException("Błąd: Błąd typu akcji"));

        MotionType motionType = motionTypeRepository.findById(addMotionRequest.getMotiontype()).
                orElseThrow(() -> new RuntimeException("Błąd: Błąd typu wniosku"));
        Motion motion = new Motion(userPersonalData, motionType, motionState);
        ActionTakenInMotion actionTakenInMotion = new ActionTakenInMotion(motion, workerPersonalData, actionType);
        motionRepository.save(motion);
        actionTakenInMotionRepository.save(actionTakenInMotion);
        return ResponseEntity.ok(new MessageResponse("Wniosek dodany"));
    }

    public ResponseEntity<?> updatemotionstatus(@RequestBody UpdateMotionStateRequest updateMotionStateRequest, HttpServletRequest request) {
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(JwtUtils.parseJwt(request))).
                orElseThrow(() -> new RuntimeException("Błąd: Zły login"));

        PersonalData workerPersonalData = users.getPersonalDataForUsers();
        MotionState motionState = motionStateRepository.findById(updateMotionStateRequest.getNewmotionstate()).
                orElseThrow(() -> new RuntimeException("Błąd: Błąd stanu wniosku"));
        Motion motion = motionRepository.findById(updateMotionStateRequest.getMotionid()).
                orElseThrow(() -> new RuntimeException("Błąd: Zły wniosek"));

        MotionState beforestate = motion.getMotionStateByMotionStateId();
        if (beforestate.getState().equals("Zatwierdzony") || beforestate.getState().equals("Odrzucony")) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Błąd: Błąd Wniosek zakończony"));
        }
        if (beforestate.getMotionStateId() == updateMotionStateRequest.getNewmotionstate()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Błąd: Stan wniosku już ustawiony na pożądaną wartośc"));
        }


        motion.setMotionStateByMotionStateId(motionState);
        motionRepository.save(motion);


        if (motionState.getState().equals("Zatwierdzony")) {
            ActionType actionType = actionTypeRepository.findByType("Zatwierdzenie wniosku").
                    orElseThrow(() -> new RuntimeException("Błąd: Błąd typu akcji"));
            ActionTakenInMotion actionTakenInMotion = new ActionTakenInMotion(motion, workerPersonalData, actionType);
            actionTakenInMotionRepository.save(actionTakenInMotion);
            return ResponseEntity.ok(new MessageResponse("Wniosek uaktualniony"));

        } else if (motionState.getState().equals("Odrzucony")) {
            ActionType actionType = actionTypeRepository.findByType("Odrzucenie wniosku").
                    orElseThrow(() -> new RuntimeException("Błąd: Błąd typu akcji"));
            ActionTakenInMotion actionTakenInMotion = new ActionTakenInMotion(motion, workerPersonalData, actionType);
            actionTakenInMotionRepository.save(actionTakenInMotion);
            return ResponseEntity.ok(new MessageResponse("Wniosek uaktualniony"));
        } else {
            ActionType actionType = actionTypeRepository.findByType("Przetwarzanie wniosku").
                    orElseThrow(() -> new RuntimeException("Błąd: Błąd typu akcji"));
            if (!actionTakenInMotionRepository.existsByPersonalDataForActionTakenInMotionAndMotionForActionInMotionsAndActionTypeByActionTypeId(
                    workerPersonalData, motion, actionType
            )) {
                ActionTakenInMotion actionTakenInMotion = new ActionTakenInMotion(motion, workerPersonalData, actionType);
                actionTakenInMotionRepository.save(actionTakenInMotion);
                return ResponseEntity.ok(new MessageResponse("Wniosek uaktualniony"));

            }

        }
        return ResponseEntity.ok(new MessageResponse("Wniosek uaktualniony"));
    }


}
