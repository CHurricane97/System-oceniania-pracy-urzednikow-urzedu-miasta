package com.example.town_application.controller;

import com.example.town_application.utility.requests.evaluation.AddEvaluationRequest;
import com.example.town_application.utility.requests.evaluation.DeleteEvaluationRequest;
import com.example.town_application.utility.requests.evaluation.EditEvaluationRequest;
import com.example.town_application.model.dto.evaluationDTOs.EvaluationMadeByUser;
import com.example.town_application.model.dto.evaluationDTOs.EvaluationPublicPersonalDTO;
import com.example.town_application.model.dto.evaluationDTOs.EvaluationPublicWithAvgAndCounter;
import com.example.town_application.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/evaluation")
public class EvaluationController {
    EvaluationService evaluationService;

    @Autowired
    public void setEvaluationService(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping("/addEvaluation")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addEvaluation(
            @Valid @RequestBody AddEvaluationRequest addEvaluationRequest,
            HttpServletRequest request) {
        return evaluationService.addEvaluation(addEvaluationRequest, request);
    }

    @GetMapping("/getAllEvaluationsForWorker")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<EvaluationPublicPersonalDTO> getAllEvaluationsForWorker(@RequestParam Integer page, @RequestParam Integer personaldataid, HttpServletRequest request) {
        return evaluationService.getAllEvaluationsForWorker(page, personaldataid, request);
    }

    @GetMapping("/getAllEvaluationsForWorkerFiltered")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<EvaluationPublicPersonalDTO> getAllEvaluationsForWorkerFiltered(@RequestParam Integer page, @RequestParam Integer personaldataid, @RequestParam Integer mark) {
        return evaluationService.getAllEvaluationsForWorkerFiltered(page, personaldataid, mark);
    }


    @GetMapping("/getAVGforPerson")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    Double getAVGforPerson(@RequestParam Integer personaldataid) {
        return evaluationService.getAVGforPerson(personaldataid);
    }

    @GetMapping("/getAVGforPersonRound")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    Double getAVGforPersonRound(@RequestParam Integer personaldataid) {
        return evaluationService.getAVGforPerson(personaldataid);
    }

    @GetMapping("/getEvaluationListForWorkerWithAvg")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    EvaluationPublicWithAvgAndCounter getEvaluationListForWorkerWithAvg(@RequestParam Integer page, @RequestParam Integer personaldataid, HttpServletRequest request) {
        return evaluationService.getEvaluationListForWorkerWithAvg(page,personaldataid,request);
    }


    @GetMapping("/checkIfWorkerEvaluatedForMotion")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Boolean checkIfWorkerEvaluatedForMotion(@RequestParam Integer motionID, @RequestParam Integer personID, HttpServletRequest request) {
        return evaluationService.checkIfWorkerEvaluatedForMotion(motionID, personID, request);
    }


    @GetMapping("/getAllEvaluationsMadeByUser")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<EvaluationMadeByUser> getAllEvaluationsMadeByUser(@RequestParam Integer page, HttpServletRequest request) {
        return evaluationService.getAllEvaluationsMadeByUser(page, request);
    }

    @PutMapping("/editEvaluation")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> editEvaluation(@Valid @RequestBody EditEvaluationRequest editEvaluationRequest, HttpServletRequest request) {
        return evaluationService.editEvaluation(editEvaluationRequest, request);
    }

    @DeleteMapping("/deleteEvaluation")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteEvaluation(@Valid @RequestBody DeleteEvaluationRequest deleteEvaluationRequest, HttpServletRequest request) {
        return evaluationService.deleteEvaluation(deleteEvaluationRequest, request);
    }


}
