package com.example.town_application.controller;


import com.example.town_application.utility.requests.personalData.AddPersonRequest;
import com.example.town_application.utility.requests.personalData.UpdatePersonRequest;
import com.example.town_application.model.dto.loginRegisterDTOs.LoginReg;
import com.example.town_application.model.dto.personalDataDTOs.PersonalDataWithoutID;
import com.example.town_application.model.dto.personalDataDTOs.WorkerDataWithCounter;
import com.example.town_application.model.dto.personalDataDTOs.WorkerPersonalDataWithMotionAction;
import com.example.town_application.service.PersonalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/person")
public class PersonController {

    PersonalDataService personalDataService;

    @Autowired
    public void setPersonalDataService(PersonalDataService personalDataService) {
        this.personalDataService = personalDataService;
    }

    @GetMapping("/getalldata")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PersonalDataWithoutID> getAll(@RequestParam Integer page) {
        return personalDataService.getAll(page);
    }

    @GetMapping("/getdatapesel")
    @PreAuthorize("hasRole('ADMIN')")
    public PersonalDataWithoutID getPersonalDataPesel(@RequestParam String pesel) {
        return personalDataService.getPersonalDataPesel(pesel);
    }


    @GetMapping("/getPeselAutocomplete")
    @PreAuthorize("hasRole('ADMIN')")
    List<String> getPeselAutocomplete(@RequestParam String pesel, @RequestParam Integer page) {
        return personalDataService.getPeselAutocomplete(pesel, page);
    }


    @GetMapping("/getdatatoken")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public PersonalDataWithoutID getPersonalDataToken(HttpServletRequest request) {
        return personalDataService.getPersonalDataToken(request);
    }


    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody AddPersonRequest addPersonRequest) {
        return personalDataService.registerUser(addPersonRequest);
    }


    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdatePersonRequest updatePersonRequest) {
        return personalDataService.updateUser(updatePersonRequest);
    }

    @GetMapping("/getAllWorkersForMotion")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<WorkerPersonalDataWithMotionAction> getAllWorkersForMotion(@RequestParam Integer page, @RequestParam Integer motionId, HttpServletRequest request) {
        return personalDataService.getAllWorkersForMotion(page, motionId, request);
    }


    @GetMapping("/getWorkersFiltered")
    @PreAuthorize("hasRole('USER')")
    public WorkerDataWithCounter getWorkersFiltered( String name,  String surname, @RequestParam Integer page) {
        return personalDataService.getWorkersFiltered(name, surname, page);
    }

    @GetMapping("/getAllWorkers")
    @PreAuthorize("hasRole('USER')")
    public WorkerDataWithCounter getAllWorkers(@RequestParam Integer page) {
        return personalDataService.getAllWorkers(page);
    }

    @GetMapping("/getLoginRegister")
    @PreAuthorize("hasRole('ADMIN')")
    public List<LoginReg> getLoginRegister(@RequestParam Integer page) {
        return personalDataService.getLoginRegister(page);
    }

    @GetMapping("/getLoginRegisterCounter")
    @PreAuthorize("hasRole('ADMIN')")
    public Long getLoginRegisterCounter() {
        return personalDataService.getLoginRegisterCounter();
    }


}
