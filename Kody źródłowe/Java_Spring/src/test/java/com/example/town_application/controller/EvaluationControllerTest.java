package com.example.town_application.controller;

import com.example.town_application.model.*;
import com.example.town_application.repository.*;
import com.example.town_application.utility.JwtResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EvaluationControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MotionRepository motionRepository;

    @Autowired
    private PersonalDataRepository personalDataRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private MotionTypeRepository motionTypeRepository;

    @Autowired
    private MotionStateRepository motionStateRepository;

    @Autowired
    private ActionTypeRepository actionTypeRepository;

    @Autowired
    private ActionTakenInMotionRepository actionTakenInMotionRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    void addEvaluation() throws Exception {

        //GIVEN
        //Personal data
        PersonalData personalData = new PersonalData();
        personalData.setName("Mirosław");
        personalData.setSurname("Łąbądź");
        personalData.setDateOfBirth(Date.valueOf(LocalDate.now()));
        personalData.setPesel("11111111111");
        personalData.setCityCode("58-150");
        personalData.setCity("Strzegom");
        personalData.setStreet("Rybna");
        personalData.setHouseNumber("20");
        personalData.setFlatNumber("2");

        PersonalData personalData2 = new PersonalData();
        personalData2.setName("Maria");
        personalData2.setSurname("Kolonko");
        personalData2.setDateOfBirth(Date.valueOf(LocalDate.now()));
        personalData2.setPesel("11111111112");
        personalData2.setCityCode("58-150");
        personalData2.setCity("Strzegom");
        personalData2.setStreet("Rybna");
        personalData2.setHouseNumber("22");
        personalData2.setFlatNumber("4");


        personalDataRepository.save(personalData);
        personalDataRepository.save(personalData2);
        //Users
        //$2a$10$woyAIb7ohukpbYskdEspY.ATnRYsK63w08wcZEtJxi8KMR5bI1chu - abba

        Users users = new Users();
        users.setLogin("login");
        users.setPassword("$2a$10$woyAIb7ohukpbYskdEspY.ATnRYsK63w08wcZEtJxi8KMR5bI1chu");
        users.setPersonalDataForUsers(personalData);
        users.setPermissionLevel("ROLE_ADMIN");

        Users users2 = new Users();
        users2.setLogin("login2");
        users2.setPassword("$2a$10$woyAIb7ohukpbYskdEspY.ATnRYsK63w08wcZEtJxi8KMR5bI1chu");
        users2.setPersonalDataForUsers(personalData2);
        users2.setPermissionLevel("ROLE_USER");

        usersRepository.save(users);
        usersRepository.save(users2);

        //Action Type
        ActionType actionType1 = new ActionType();
        actionType1.setType("Przyjęcie wniosku");

        actionTypeRepository.save(actionType1);

        //Moton Type

        MotionType motionType = new MotionType();
        motionType.setType("Wniosek o dotację");
        motionTypeRepository.save(motionType);

        //Motion State

        MotionState motionState3 = new MotionState();
        motionState3.setState("Zatwierdzony");

        motionStateRepository.save(motionState3);

        //Motion

        Motion motion = new Motion();
        motion.setMotionStateByMotionStateId(motionState3);
        motion.setMotionTypeByMotionTypeId(motionType);
        motion.setPersonalDataForMotions(personalData2);
        motionRepository.save(motion);

        //Action Taken in Motion

        ActionTakenInMotion actionTakenInMotion = new ActionTakenInMotion();
        actionTakenInMotion.setMotionForActionInMotions(motion);
        actionTakenInMotion.setActionTypeByActionTypeId(actionType1);
        actionTakenInMotion.setPersonalDataForActionTakenInMotion(personalData);

        actionTakenInMotionRepository.save(actionTakenInMotion);

        //WHEN
        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
        MvcResult login = mockMvc.perform(post("/auth/loginUser")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content("""
                                {
                                    "login": "login2",
                                    "password": "abba"
                                }""")
                )
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        JwtResponse response = objectMapper.readValue(login.getResponse().getContentAsString(), JwtResponse.class);


        MvcResult mvcResult = mockMvc.perform(post("/evaluation/addEvaluation")
                        .header("Authorization", response.getType() + " " + response.getToken())
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content(String.format("""
                                        {
                                          "motionId": %s,
                                          "workerId": %s,
                                          "grade": "5",
                                          "description": "test"
                                        }""", motion.getMotionId(),
                                personalData.getPersonalDataId())))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //THEN
        String resp = mvcResult.getResponse().getContentAsString();
        assertThat(resp).isEqualTo("{\"message\":\"Ocena dodana pomyślnie\"}");

        List<Evaluation> evaluations = evaluationRepository.findAll();
        assertThat(evaluations.get(0).getGrade()).isEqualTo(5);
        assertThat(evaluations.get(0).getDescription()).isEqualTo("test");
        assertThat(evaluations.get(0).getPersonalDataForEvaluation().getPersonalDataId()).isEqualTo(personalData.getPersonalDataId());
        assertThat(evaluations.get(0).getMotionForEvaluation().getMotionId()).isEqualTo(motion.getMotionId());


    }

    @Test
    @Transactional
    void deleteEvaluation() throws Exception {


        //GIVEN
        //Personal data
        PersonalData personalData = new PersonalData();
        personalData.setName("Mirosław");
        personalData.setSurname("Łąbądź");
        personalData.setDateOfBirth(Date.valueOf(LocalDate.now()));
        personalData.setPesel("11111111111");
        personalData.setCityCode("58-150");
        personalData.setCity("Strzegom");
        personalData.setStreet("Rybna");
        personalData.setHouseNumber("20");
        personalData.setFlatNumber("2");

        PersonalData personalData2 = new PersonalData();
        personalData2.setName("Maria");
        personalData2.setSurname("Kolonko");
        personalData2.setDateOfBirth(Date.valueOf(LocalDate.now()));
        personalData2.setPesel("11111111112");
        personalData2.setCityCode("58-150");
        personalData2.setCity("Strzegom");
        personalData2.setStreet("Rybna");
        personalData2.setHouseNumber("22");
        personalData2.setFlatNumber("4");


        personalDataRepository.save(personalData);
        personalDataRepository.save(personalData2);
        //Users
        //$2a$10$woyAIb7ohukpbYskdEspY.ATnRYsK63w08wcZEtJxi8KMR5bI1chu - abba

        Users users = new Users();
        users.setLogin("login");
        users.setPassword("$2a$10$woyAIb7ohukpbYskdEspY.ATnRYsK63w08wcZEtJxi8KMR5bI1chu");
        users.setPersonalDataForUsers(personalData);
        users.setPermissionLevel("ROLE_ADMIN");

        Users users2 = new Users();
        users2.setLogin("login2");
        users2.setPassword("$2a$10$woyAIb7ohukpbYskdEspY.ATnRYsK63w08wcZEtJxi8KMR5bI1chu");
        users2.setPersonalDataForUsers(personalData2);
        users2.setPermissionLevel("ROLE_USER");

        usersRepository.save(users);
        usersRepository.save(users2);

        //Action Type
        ActionType actionType1 = new ActionType();
        actionType1.setType("Przyjęcie wniosku");

        actionTypeRepository.save(actionType1);

        //Moton Type

        MotionType motionType = new MotionType();
        motionType.setType("Wniosek o dotację");
        motionTypeRepository.save(motionType);

        //Motion State

        MotionState motionState3 = new MotionState();
        motionState3.setState("Zatwierdzony");

        motionStateRepository.save(motionState3);

        //Motion

        Motion motion = new Motion();
        motion.setMotionStateByMotionStateId(motionState3);
        motion.setMotionTypeByMotionTypeId(motionType);
        motion.setPersonalDataForMotions(personalData2);
        motionRepository.save(motion);

        //Action Taken in Motion

        ActionTakenInMotion actionTakenInMotion = new ActionTakenInMotion();
        actionTakenInMotion.setMotionForActionInMotions(motion);
        actionTakenInMotion.setActionTypeByActionTypeId(actionType1);
        actionTakenInMotion.setPersonalDataForActionTakenInMotion(personalData);

        actionTakenInMotionRepository.save(actionTakenInMotion);

        //Evaluation

        Evaluation evaluation = new Evaluation();
        evaluation.setDescription("test");
        evaluation.setGrade(5);
        evaluation.setMotionForEvaluation(motion);
        evaluation.setPersonalDataForEvaluation(personalData);

        evaluationRepository.save(evaluation);


        //WHEN
        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
        MvcResult login = mockMvc.perform(post("/auth/loginUser")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content("""
                                {
                                    "login": "login2",
                                    "password": "abba"
                                }""")
                )
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        JwtResponse response = objectMapper.readValue(login.getResponse().getContentAsString(), JwtResponse.class);


        MvcResult mvcResult = mockMvc.perform(delete("/evaluation/deleteEvaluation")
                .header("Authorization", response.getType() + " " + response.getToken())
                .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8).content(String.format("""
                                {
                                    "motionId": %s,
                                    "workerId": %s
                                    
                                }""", motion.getMotionId(),
                        personalData.getPersonalDataId()))
        )
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //THEN
        String resp = mvcResult.getResponse().getContentAsString();
        assertThat(resp).isEqualTo("{\"message\":\"Ocena usunięta\"}");

        List<Evaluation> evaluations = evaluationRepository.findAll();
        assertThat(evaluations.size()).isEqualTo(0);


    }


}