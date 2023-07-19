package com.example.town_application.service;

import com.example.town_application.utility.JwtUtils;
import com.example.town_application.utility.MessageResponse;
import com.example.town_application.utility.requests.evaluation.AddEvaluationRequest;
import com.example.town_application.utility.requests.evaluation.DeleteEvaluationRequest;
import com.example.town_application.utility.requests.evaluation.EditEvaluationRequest;
import com.example.town_application.model.Evaluation;
import com.example.town_application.model.Motion;
import com.example.town_application.model.PersonalData;
import com.example.town_application.model.Users;
import com.example.town_application.model.dto.evaluationDTOs.EvaluationMadeByUser;
import com.example.town_application.model.dto.evaluationDTOs.EvaluationPublicPersonalDTO;
import com.example.town_application.model.dto.evaluationDTOs.EvaluationPublicWithAvgAndCounter;
import com.example.town_application.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final PersonalDataRepository personalDataRepository;
    private final JwtUtils jwtUtils;
    private final UsersRepository userRepository;
    private final ModelMapper modelMapper;
    private final MotionRepository motionRepository;
    private final MotionStateRepository motionStateRepository;
    private final MotionTypeRepository motionTypeRepository;
    private final ActionTakenInMotionRepository actionTakenInMotionRepository;
    private final ActionTypeRepository actionTypeRepository;
    private final EvaluationRepository evaluationRepository;

    public List<EvaluationPublicPersonalDTO> getAllEvaluationsForWorker(Integer page, Integer personaldataid, HttpServletRequest request) {
        PersonalData worker = personalDataRepository.findById(personaldataid).orElseThrow(() -> new RuntimeException(("Błąd: Pracownik nie istnieje")));
        if (worker.getUsersByPersonalDataId().stream().filter(per -> per.getPermissionLevel().equals("ROLE_ADMIN")).toList().isEmpty()) {
            throw new RuntimeException("Błąd: Osoba nie jest pracownikiem");
        }
        return evaluationRepository.findByPersonalDataForEvaluation_PersonalDataId(personaldataid, PageRequest.of(--page, 20)
                )
                .stream()
                .map(mapItem -> modelMapper.map(mapItem, EvaluationPublicPersonalDTO.class))
                .collect(Collectors.toList());
    }

    public List<EvaluationPublicPersonalDTO> getAllEvaluationsForWorkerFiltered(Integer page, Integer personaldataid, Integer mark) {
        PersonalData worker = personalDataRepository.findById(personaldataid).orElseThrow(() -> new RuntimeException(("Błąd: Pracownik nie istnieje")));
        if (worker.getUsersByPersonalDataId().stream().filter(per -> per.getPermissionLevel().equals("ROLE_ADMIN")).toList().isEmpty()) {
            throw new RuntimeException("Błąd: Osoba nie jest pracownikiem");
        }
        return evaluationRepository.findByPersonalDataForEvaluation_PersonalDataIdAndGrade(personaldataid, mark, PageRequest.of(--page, 20)
                )
                .stream()
                .map(mapItem -> modelMapper.map(mapItem, EvaluationPublicPersonalDTO.class))
                .collect(Collectors.toList());
    }

    public Double getAVGforPersonRound(Integer personaldataid) {
        return Math.round(evaluationRepository.getAVGByPID(personaldataid) * 2) / 2.0;
    }

    public Double getAVGforPerson(Integer personaldataid) {
        return evaluationRepository.getAVGByPID(personaldataid);
    }

    public EvaluationPublicWithAvgAndCounter getEvaluationListForWorkerWithAvg(Integer page, Integer personaldataid, HttpServletRequest request) {
        PersonalData worker = personalDataRepository.findById(personaldataid).orElseThrow(() -> new RuntimeException(("Błąd: Pracownik nie istnieje")));
        if (worker.getUsersByPersonalDataId().stream().filter(per -> per.getPermissionLevel().equals("ROLE_ADMIN")).toList().isEmpty()) {
            throw new RuntimeException("Błąd: Osoba nie jest pracownikiem");
        }
        List<EvaluationPublicPersonalDTO> evaluationPublicPersonalDTOList=
        evaluationRepository.findByPersonalDataForEvaluation_PersonalDataId(personaldataid, PageRequest.of(--page, 20, Sort.by("evaluationId").descending())
                )
                .stream()
                .map(mapItem -> modelMapper.map(mapItem, EvaluationPublicPersonalDTO.class))
                .collect(Collectors.toList());

        Double avg;
        Double avgRounded;

        Long count= Long.valueOf(evaluationPublicPersonalDTOList.size());
        if (count==0){
            avg= Double.valueOf(0);
            avgRounded= Double.valueOf(0);
        }else {
            avg=evaluationRepository.getAVGByPID(personaldataid);
            avgRounded=Math.round(avg * 2) / 2.0;
        }



        return new EvaluationPublicWithAvgAndCounter(worker.getName(),worker.getSurname(),evaluationPublicPersonalDTOList,avg,avgRounded,count);

    }

    public Boolean checkIfWorkerEvaluatedForMotion(@RequestParam Integer motionID, @RequestParam Integer personID, HttpServletRequest request) {
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(JwtUtils.parseJwt(request)))
                .orElseThrow(() -> new RuntimeException(("Błąd: Zły login")));
        PersonalData personalData = users.getPersonalDataForUsers();
        Motion motion = motionRepository.findById(motionID)
                .orElseThrow(() -> new RuntimeException(("Błąd: Zły wniosek")));
        PersonalData worker = personalDataRepository.findById(personID)
                .orElseThrow(() -> new RuntimeException(("Błąd: Pracownik nie istnieje")));
        if (motion.getPersonalDataForMotions().getPersonalDataId() != users.getPersonalDataForUsers().getPersonalDataId()) {
            throw new RuntimeException("Błąd: Zły wniosek dla urzytkownika");
        }
        if (!actionTakenInMotionRepository.existsByPersonalDataForActionTakenInMotion_PersonalDataIdAndMotionForActionInMotions_MotionId(personID, motion.getMotionId())) {
            throw new RuntimeException("Błąd: Pracownik nie wykonał akcji w tym wniosku");
        }

        if (evaluationRepository.existsByMotionForEvaluation_MotionIdAndPersonalDataForEvaluation_PersonalDataId(motionID, personID)) {
            return true;
        } else {
            return false;
        }

    }


    public ResponseEntity<?> addEvaluation(AddEvaluationRequest addEvaluationRequest, HttpServletRequest request) {
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(JwtUtils.parseJwt(request)))
                .orElseThrow(() -> new RuntimeException(("Błąd: Zły login")));
        PersonalData personalData = users.getPersonalDataForUsers();
        Motion motion = motionRepository.findById(addEvaluationRequest.getMotionId())
                .orElseThrow(() -> new RuntimeException(("Błąd: Zły wniosek")));
        PersonalData worker = personalDataRepository.findById(addEvaluationRequest.getWorkerId())
                .orElseThrow(() -> new RuntimeException(("Błąd: Pracownik nie istnieje")));
        if (motion.getPersonalDataForMotions().getPersonalDataId() != users.getPersonalDataForUsers().getPersonalDataId()) {
            throw new RuntimeException("Błąd: Zły wniosek dla uzytkownika");
        }
        if (!actionTakenInMotionRepository.existsByPersonalDataForActionTakenInMotion_PersonalDataIdAndMotionForActionInMotions_MotionId(
                addEvaluationRequest.getWorkerId(), motion.getMotionId())) {
            throw new RuntimeException("Błąd: Pracownik nie wykonał akcji w tym wniosku");
        }
        if (evaluationRepository.existsByMotionForEvaluation_MotionIdAndPersonalDataForEvaluation_PersonalDataId(
                addEvaluationRequest.getMotionId(), addEvaluationRequest.getWorkerId())) {
            throw new RuntimeException("Błąd: Pracownik już oceniony");
        }
        if ((motion.getMotionStateByMotionStateId().getState().equals("Zatwierdzony")
                || motion.getMotionStateByMotionStateId().getState().equals("Odrzucony"))) {
            Evaluation evaluation = new Evaluation(addEvaluationRequest.getGrade(),
                    addEvaluationRequest.getDescription(), motion, worker);
            evaluationRepository.save(evaluation);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Błąd: Wniosek nie zakończony"));
        }
        return ResponseEntity.ok(new MessageResponse("Ocena dodana pomyślnie"));
    }


    public List<EvaluationMadeByUser> getAllEvaluationsMadeByUser(Integer page, HttpServletRequest request) {
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(JwtUtils.parseJwt(request))).orElseThrow(() -> new RuntimeException(("Błąd: Żły login")));
        PersonalData personalData = users.getPersonalDataForUsers();

        return evaluationRepository.findByMotionForEvaluation_PersonalDataForMotions(personalData, PageRequest.of(--page, 20)
                )
                .stream()
                .map(mapItem -> modelMapper.map(mapItem, EvaluationMadeByUser.class))
                .collect(Collectors.toList());
    }


    public ResponseEntity<?> editEvaluation(EditEvaluationRequest editEvaluationRequest, HttpServletRequest request) {
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(JwtUtils.parseJwt(request))).orElseThrow(() -> new RuntimeException(("Błąd: Zły login")));
        PersonalData personalData = users.getPersonalDataForUsers();

        Evaluation evaluation = evaluationRepository.findByPersonalDataForEvaluation_PersonalDataIdAndMotionForEvaluation_MotionId(editEvaluationRequest.getWorkerId(), editEvaluationRequest.getMotionId()).orElseThrow(() -> new RuntimeException(("Wrong Motion or Worker")));
        Motion motion = evaluation.getMotionForEvaluation();
        if (personalData.getPersonalDataId() != motion.getPersonalDataForMotions().getPersonalDataId()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Błąd: Brak Prawa do edycji"));
        }

        evaluation.setGrade(editEvaluationRequest.getGrade());
        evaluation.setDescription(editEvaluationRequest.getDescription());
        evaluationRepository.save(evaluation);
        return ResponseEntity.ok(new MessageResponse("Ocena zaktualizowana"));
    }

    @Transactional
    public ResponseEntity<?> deleteEvaluation(DeleteEvaluationRequest deleteEvaluationRequest, HttpServletRequest request) {
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(JwtUtils.parseJwt(request))).orElseThrow(() -> new RuntimeException(("Błąd: zły login")));
        PersonalData personalData = users.getPersonalDataForUsers();
        Evaluation evaluation = evaluationRepository.findByPersonalDataForEvaluation_PersonalDataIdAndMotionForEvaluation_MotionId(deleteEvaluationRequest.getWorkerId(), deleteEvaluationRequest.getMotionId()).orElseThrow(() -> new RuntimeException(("Błąd: Zły wniosek albo pracownik")));
        Motion motion = evaluation.getMotionForEvaluation();
        if (personalData.getPersonalDataId() != motion.getPersonalDataForMotions().getPersonalDataId()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Błąd: Brak Prawa do edycji"));
        }
        evaluationRepository.delete(evaluation);
        return ResponseEntity.ok(new MessageResponse("Ocena usunięta"));
    }


}
