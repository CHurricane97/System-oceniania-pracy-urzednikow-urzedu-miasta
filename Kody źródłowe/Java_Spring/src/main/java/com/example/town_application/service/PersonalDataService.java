package com.example.town_application.service;

import com.example.town_application.utility.JwtUtils;
import com.example.town_application.utility.MessageResponse;
import com.example.town_application.utility.requests.personalData.AddPersonRequest;
import com.example.town_application.utility.requests.personalData.UpdatePersonRequest;
import com.example.town_application.model.Motion;
import com.example.town_application.model.PersonalData;
import com.example.town_application.model.Users;
import com.example.town_application.model.dto.loginRegisterDTOs.LoginReg;
import com.example.town_application.model.dto.personalDataDTOs.PersonalDataWithoutID;
import com.example.town_application.model.dto.personalDataDTOs.WorkerData;
import com.example.town_application.model.dto.personalDataDTOs.WorkerDataWithCounter;
import com.example.town_application.model.dto.personalDataDTOs.WorkerPersonalDataWithMotionAction;
import com.example.town_application.repository.LoginRegisterRepository;
import com.example.town_application.repository.MotionRepository;
import com.example.town_application.repository.PersonalDataRepository;
import com.example.town_application.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.GenericValidator;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PersonalDataService {
    private final PersonalDataRepository personalDataRepository;
    private final JwtUtils jwtUtils;
    private final UsersRepository userRepository;
    private final ModelMapper modelMapper;
    private final MotionRepository motionRepository;
    private final LoginRegisterRepository loginRegisterRepository;


    public List<PersonalDataWithoutID> getAll(Integer page) {
        return personalDataRepository.findAll(PageRequest.of(--page, 20)
                )
                .stream()
                .map(mapItem -> modelMapper.map(mapItem, PersonalDataWithoutID.class))
                .collect(Collectors.toList());
    }


    public PersonalDataWithoutID getPersonalDataPesel(String pesel) {
        PersonalData personalData = personalDataRepository.findByPesel(pesel).orElseThrow(() -> new RuntimeException("Błąd: Zły pesel"));
        return modelMapper.map(personalData, PersonalDataWithoutID.class);
    }


    public List<String> getPeselAutocomplete(String pesel, Integer page) {
        return personalDataRepository.findByPeselContainsIgnoreCase(pesel, PageRequest.of(--page, 5)
                )
                .stream()
                .map(new Function<PersonalData, String>() {
                    @Override
                    public String apply(PersonalData mapItem) {
                        return mapItem.getPesel();
                    }
                })
                .collect(Collectors.toList());
    }


    public PersonalDataWithoutID getPersonalDataToken(HttpServletRequest request) {
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(JwtUtils.parseJwt(request))).orElseThrow(() -> new RuntimeException(("Błąd: Zły login")));
        PersonalData personalData = users.getPersonalDataForUsers();
        return modelMapper.map(personalData, PersonalDataWithoutID.class);
    }

    public ResponseEntity<?> registerUser(AddPersonRequest addPersonRequest) {

        if (personalDataRepository.existsByPesel(addPersonRequest.getPesel())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Błąd: Osoba z tym peselem już istnieje"));
        }

        if (!addPersonRequest.getPesel().matches("[0-9]+")){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Błąd: Pesel musi mieć w sobie tylko cyfry"));
        }

        if (!GenericValidator.isDate(addPersonRequest.getDate_of_birth(), "yyyy-MM-dd", true)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Błąd: zły format daty"));
        }
        LocalDate dateOfBirth = LocalDate.parse(addPersonRequest.getDate_of_birth());
        PersonalData personalData = new PersonalData(
                addPersonRequest.getName(),
                addPersonRequest.getSurname(),
                addPersonRequest.getPesel(),
                java.sql.Date.valueOf(dateOfBirth),
                addPersonRequest.getCity(),
                addPersonRequest.getCity_code(),
                addPersonRequest.getStreet(),
                addPersonRequest.getHouse_number(),
                addPersonRequest.getFlat_number());
        personalDataRepository.save(personalData);

        return ResponseEntity.ok(new MessageResponse("Dodano osobę"));

    }

    public ResponseEntity<?> updateUser(UpdatePersonRequest updatePersonRequest) {
        if (!personalDataRepository.existsByPesel(updatePersonRequest.getPesel())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Błąd: Osoba z tym peselem już istnieje"));
        }

        PersonalData personalData = personalDataRepository.findByPesel(updatePersonRequest.getPesel())
                .orElseThrow(() -> new RuntimeException(("Błąd: Brak osoby z podanym peselem " + updatePersonRequest.getPesel())));
        personalData.setName(updatePersonRequest.getName());
        personalData.setCity(updatePersonRequest.getCity());
        personalData.setCityCode(updatePersonRequest.getCity_code());
        personalData.setSurname(updatePersonRequest.getSurname());
        personalData.setHouseNumber(updatePersonRequest.getHouse_number());
        personalData.setFlatNumber(updatePersonRequest.getFlat_number());
        personalData.setStreet(updatePersonRequest.getStreet());
        personalDataRepository.save(personalData);

        return ResponseEntity.ok(new MessageResponse("Zmodyfikowano osobę"));
    }

    public List<WorkerPersonalDataWithMotionAction> getAllWorkersForMotion(Integer page, Integer motionId, HttpServletRequest request) {
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(JwtUtils.parseJwt(request))).orElseThrow(() -> new RuntimeException(("Błąd: Zły login")));
        PersonalData personalData = users.getPersonalDataForUsers();
        Motion motion = motionRepository.findById(motionId).orElseThrow(() -> new RuntimeException(("Błąd: Zły wniosek")));

        if (motion.getPersonalDataForMotions().getPersonalDataId() != personalData.getPersonalDataId()) {
            throw new RuntimeException("Błąd: Zły wniosek");
        }

        List<WorkerPersonalDataWithMotionAction> workerPersonalDataWithMotionActionList = personalDataRepository
                .findDistinctByActionTakenInMotionsByPersonalDataId_MotionForActionInMotions_MotionId(motionId, PageRequest.of(--page, 20))
                .stream()
                .map(mapItem -> modelMapper.map(mapItem, WorkerPersonalDataWithMotionAction.class))
                .toList();

        workerPersonalDataWithMotionActionList.forEach(per ->
                per.setActionTakenInMotionsByPersonalDataId(per
                        .getActionTakenInMotionsByPersonalDataId()
                        .stream().filter(action -> action.getMotionForActionInMotions().getMotionId() == motionId)
                        .toList()
                )
        );

        return workerPersonalDataWithMotionActionList;
    }







    public WorkerDataWithCounter getWorkersFiltered(String name, String surname, Integer page) {

        List<WorkerData> workers = userRepository
                .findAll(
                        Specification
                                .where(name == null ? null : nameContains(name))
                                .and(surname == null ? null : surNameContains(surname))
                                .and(role()),
                        PageRequest.of(--page, 20))
                .stream()
                .map(mapItem -> modelMapper.map(mapItem.getPersonalDataForUsers(), WorkerData.class))
                .collect(Collectors.toList());

        Long count = Long.valueOf(workers.size());

        return new WorkerDataWithCounter(count, workers);


    }


    private static Specification<Users> nameContains(String expression) {
        return (root, query, builder) -> builder
                .like(
                        builder.upper(
                                root.join("personalDataForUsers").get("name")
                        ),
                        contains(expression).toUpperCase()
                );
    }

    private static Specification<Users> surNameContains(String expression) {
        return (root, query, builder) -> builder
                .like(
                        builder.upper(
                                root.join("personalDataForUsers").get("surname")
                        ),
                        contains(expression).toUpperCase()
                );
    }

    private static Specification<Users> role() {
        return (root, query, builder) -> builder.equal(root.get("permissionLevel"), "ROLE_ADMIN");
    }


    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }


    public WorkerDataWithCounter getAllWorkers(@RequestParam Integer page) {

        List<WorkerData> workerDataList = personalDataRepository.findDistinctByUsersByPersonalDataId_PermissionLevel("ROLE_ADMIN", PageRequest.of(--page, 20)
                )
                .stream()
                .map(mapItem -> modelMapper.map(mapItem, WorkerData.class))
                .collect(Collectors.toList());

        Long size = Long.valueOf(workerDataList.size());
        return new WorkerDataWithCounter(size, workerDataList);
    }


    public long getLoginRegisterCounter() {
        return loginRegisterRepository.count();
    }

    public List<LoginReg> getLoginRegister(@RequestParam Integer page) {
        return loginRegisterRepository.findAll(PageRequest.of(--page, 20, Sort.by("dateOfLogging").descending())).stream()
                .map(mapItem -> modelMapper.map(mapItem, LoginReg.class))
                .collect(Collectors.toList());

    }


}
