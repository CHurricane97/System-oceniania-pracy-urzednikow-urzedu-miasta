package com.example.town_application.controller;


import com.example.town_application.utility.requests.motion.AddMotionRequest;
import com.example.town_application.utility.requests.motion.UpdateMotionStateRequest;
import com.example.town_application.model.dto.motionDTOs.MotionDetails;
import com.example.town_application.model.dto.motionDTOs.MotionDetailsWithCounter;
import com.example.town_application.service.MotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/motion")
public class MotionController {
    MotionService motionService;

    @Autowired
    public void setMotionService(MotionService motionService) {
        this.motionService = motionService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getalldata")
    public List<MotionDetails> getAll(@RequestParam Integer page) {
        return motionService.getAll(page);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllNotFinishedMotions")
    public List<MotionDetails> getAllNotFinishedMotions(@RequestParam Integer page) {
        return motionService.getAllNotFinishedMotions(page);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllNotFinishedMotionsByPesel")
    public MotionDetailsWithCounter getAllNotFinishedMotionsByPesel(@RequestParam Integer page, String pesel) {
        return motionService.getAllNotFinishedMotionsByPesel(page, pesel);
    }


    @GetMapping("/getAllForUser")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public MotionDetailsWithCounter getAllForUser(@RequestParam Integer page, HttpServletRequest request) {
        return motionService.getAllForUser(page, request);
    }


    @GetMapping("/getAllFinishedForUser")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public MotionDetailsWithCounter getAllFinishedForUser(@RequestParam Integer page, HttpServletRequest request) {
        return motionService.getAllFinishedForUser(page, request);
    }


    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addMotion(@Valid @RequestBody AddMotionRequest addMotionRequest, HttpServletRequest request) {
        return motionService.addMotion(addMotionRequest, request);
    }


    @PutMapping("/updatestatus")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updatemotionstatus(@RequestBody UpdateMotionStateRequest updateMotionStateRequest, HttpServletRequest request) {
        return motionService.updatemotionstatus(updateMotionStateRequest, request);
    }


}
