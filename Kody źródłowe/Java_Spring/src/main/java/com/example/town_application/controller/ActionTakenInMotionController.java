package com.example.town_application.controller;

import com.example.town_application.utility.requests.actionTakenInMotion.ActionTakenInMotionBaseRequest;
import com.example.town_application.service.ActionTakenInMotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/action")
public class ActionTakenInMotionController {
    ActionTakenInMotionService actionTakenInMotionService;

    @Autowired
    public void setActionTakenInMotionService(ActionTakenInMotionService actionTakenInMotionService) {
        this.actionTakenInMotionService = actionTakenInMotionService;
    }

    @PostMapping("/Approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveMotion(
            @Valid @RequestBody ActionTakenInMotionBaseRequest actionTakenInMotionBaseRequest,
            HttpServletRequest request) {
        return actionTakenInMotionService.approveMotion(actionTakenInMotionBaseRequest, request );
    }

    @PostMapping("/Deny")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> denyMotion(
            @Valid @RequestBody ActionTakenInMotionBaseRequest actionTakenInMotionBaseRequest,
            HttpServletRequest request) {
        return actionTakenInMotionService.denyMotion(actionTakenInMotionBaseRequest, request );
    }

    @PostMapping("/Consult")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> consultMotion(
            @Valid @RequestBody ActionTakenInMotionBaseRequest actionTakenInMotionBaseRequest,
            HttpServletRequest request) {
        return actionTakenInMotionService.consultMotion(actionTakenInMotionBaseRequest, request );
    }

    @PostMapping("/Proced")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> proceedMotion(
            @Valid @RequestBody ActionTakenInMotionBaseRequest actionTakenInMotionBaseRequest,
            HttpServletRequest request) {
        return actionTakenInMotionService.proceedMotion(actionTakenInMotionBaseRequest, request );
    }



}
