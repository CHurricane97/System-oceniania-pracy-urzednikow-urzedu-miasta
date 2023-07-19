package com.example.town_application.controller;

import com.example.town_application.utility.requests.utility.AddMotionTypeRequest;
import com.example.town_application.model.dto.actionDTOs.ActionTypeDTO;
import com.example.town_application.model.dto.motionDTOs.MotionStateDTO;
import com.example.town_application.model.dto.motionDTOs.MotionTypeDTO;
import com.example.town_application.service.ActionTypeService;
import com.example.town_application.service.MotionStateService;
import com.example.town_application.service.MotionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/utility")
public class UtilityControler {
    ActionTypeService actionTypeService;
    MotionStateService motionStateService;
    MotionTypeService motionTypeService;

    @Autowired
    public void setActionTypeService(ActionTypeService actionTypeService) {
        this.actionTypeService = actionTypeService;
    }

    @Autowired
    public void setMotionStateService(MotionStateService motionStateService) {
        this.motionStateService = motionStateService;
    }

    @Autowired
    public void setMotionTypeService(MotionTypeService motionTypeService) {
        this.motionTypeService = motionTypeService;
    }

    @GetMapping("/getAllMotionStates")

    public List<MotionStateDTO> getAllMotionStates() {
        return motionStateService.getAllMotionStates();
    }

    @GetMapping("/getAllMotionTypes")

    public List<MotionTypeDTO> getAllMotionTypes() {
        return motionTypeService.getAllMotionTypes();
    }

    @GetMapping("/getAllActionTypes")

    public List<ActionTypeDTO> getAllActionTypes() {
        return actionTypeService.getAllActionTypes();
    }

    @PostMapping("/addMotionType")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addMotionType(@Valid @RequestBody AddMotionTypeRequest addMotionTypeRequest) {

        return motionTypeService.addMotionType(addMotionTypeRequest);
    }
}
