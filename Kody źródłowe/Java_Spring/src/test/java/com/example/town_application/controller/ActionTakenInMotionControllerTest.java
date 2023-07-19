package com.example.town_application.controller;

import com.example.town_application.model.*;
import com.example.town_application.repository.*;
import com.example.town_application.utility.JwtResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ActionTakenInMotionControllerTest {

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

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    void approveMotion() throws Exception {
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
        users2.setPersonalDataForUsers(personalData);
        users2.setPermissionLevel("ROLE_USER");

        usersRepository.save(users);
        usersRepository.save(users2);

        //Action Type
        ActionType actionType1 = new ActionType();
        actionType1.setType("Przyjęcie wniosku");

        ActionType actionType2 = new ActionType();
        actionType2.setType("Przetwarzanie wniosku");

        ActionType actionType3 = new ActionType();
        actionType3.setType("Konsultacje z wnioskodawcą");

        ActionType actionType4 = new ActionType();
        actionType4.setType("Zatwierdzenie wniosku");

        ActionType actionType5 = new ActionType();
        actionType5.setType("Odrzucenie wniosku");

        actionTypeRepository.save(actionType1);
        actionTypeRepository.save(actionType2);
        actionTypeRepository.save(actionType3);
        actionTypeRepository.save(actionType4);
        actionTypeRepository.save(actionType5);

        //Moton Type

        MotionType motionType = new MotionType();
        motionType.setType("Wniosek o dotację");
        motionTypeRepository.save(motionType);

        //Motion State

        MotionState motionState1 = new MotionState();
        motionState1.setState("Przyjęty");

        MotionState motionState2 = new MotionState();
        motionState2.setState("Przetwarzany");

        MotionState motionState3 = new MotionState();
        motionState3.setState("Zatwierdzony");

        MotionState motionState4 = new MotionState();
        motionState4.setState("Odrzucony");

        motionStateRepository.save(motionState1);
        motionStateRepository.save(motionState2);
        motionStateRepository.save(motionState3);
        motionStateRepository.save(motionState4);

        //Motion
        Motion motion = new Motion();
        motion.setMotionStateByMotionStateId(motionState1);
        motion.setMotionTypeByMotionTypeId(motionType);
        motion.setPersonalDataForMotions(personalData2);
        motionRepository.save(motion);


        //WHEN
        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
        MvcResult login = mockMvc.perform(post("/auth/loginAdmin")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content("""
                                {
                                    "login": "login",
                                    "password": "abba"
                                }""")
                )
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        JwtResponse response = objectMapper.readValue(login.getResponse().getContentAsString(), JwtResponse.class);

        MvcResult mvcResult = mockMvc.perform(post("/action/Approve")
                        .header("Authorization", response.getType() + " " + response.getToken())
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content(String.format("""
                                {
                                  "motionID": %s
                                }""", motion.getMotionId())))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //THEN
        String resp = mvcResult.getResponse().getContentAsString();
        assertThat(resp).isEqualTo("{\"message\":\"Wniosek zatwierdzony\"}");

        List<Motion> motions = motionRepository.findAll();
        assertThat(motions.get(0).getMotionStateByMotionStateId().getState()).isEqualTo("Zatwierdzony");

    }

    @Test
    @Transactional
    void denyMotion() throws Exception {
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
        users2.setPersonalDataForUsers(personalData);
        users2.setPermissionLevel("ROLE_USER");

        usersRepository.save(users);
        usersRepository.save(users2);

        //Action Type
        ActionType actionType1 = new ActionType();
        actionType1.setType("Przyjęcie wniosku");

        ActionType actionType2 = new ActionType();
        actionType2.setType("Przetwarzanie wniosku");

        ActionType actionType3 = new ActionType();
        actionType3.setType("Konsultacje z wnioskodawcą");

        ActionType actionType4 = new ActionType();
        actionType4.setType("Zatwierdzenie wniosku");

        ActionType actionType5 = new ActionType();
        actionType5.setType("Odrzucenie wniosku");

        actionTypeRepository.save(actionType1);
        actionTypeRepository.save(actionType2);
        actionTypeRepository.save(actionType3);
        actionTypeRepository.save(actionType4);
        actionTypeRepository.save(actionType5);

        //Moton Type

        MotionType motionType = new MotionType();
        motionType.setType("Wniosek o dotację");
        motionTypeRepository.save(motionType);

        //Motion State

        MotionState motionState1 = new MotionState();
        motionState1.setState("Przyjęty");

        MotionState motionState2 = new MotionState();
        motionState2.setState("Przetwarzany");

        MotionState motionState3 = new MotionState();
        motionState3.setState("Zatwierdzony");

        MotionState motionState4 = new MotionState();
        motionState4.setState("Odrzucony");

        motionStateRepository.save(motionState1);
        motionStateRepository.save(motionState2);
        motionStateRepository.save(motionState3);
        motionStateRepository.save(motionState4);

        //Motion
        Motion motion = new Motion();
        motion.setMotionStateByMotionStateId(motionState1);
        motion.setMotionTypeByMotionTypeId(motionType);
        motion.setPersonalDataForMotions(personalData2);
        motionRepository.save(motion);


        //WHEN
        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
        MvcResult login = mockMvc.perform(post("/auth/loginAdmin")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content("""
                                {
                                    "login": "login",
                                    "password": "abba"
                                }""")
                )
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        JwtResponse response = objectMapper.readValue(login.getResponse().getContentAsString(), JwtResponse.class);

        MvcResult mvcResult = mockMvc.perform(post("/action/Deny")
                        .header("Authorization", response.getType() + " " + response.getToken())
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content(String.format("""
                                {
                                  "motionID": %s
                                }""", motion.getMotionId())))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //THEN
        String resp = mvcResult.getResponse().getContentAsString();
        assertThat(resp).isEqualTo("{\"message\":\"Wniosek odrzucony\"}");

        List<Motion> motions = motionRepository.findAll();
        assertThat(motions.get(0).getMotionStateByMotionStateId().getState()).isEqualTo("Odrzucony");

    }

    @Test
    @Transactional
    void consultMotion() throws Exception {
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
        users2.setPersonalDataForUsers(personalData);
        users2.setPermissionLevel("ROLE_USER");

        usersRepository.save(users);
        usersRepository.save(users2);

        //Action Type
        ActionType actionType1 = new ActionType();
        actionType1.setType("Przyjęcie wniosku");

        ActionType actionType2 = new ActionType();
        actionType2.setType("Przetwarzanie wniosku");

        ActionType actionType3 = new ActionType();
        actionType3.setType("Konsultacje z wnioskodawcą");

        ActionType actionType4 = new ActionType();
        actionType4.setType("Zatwierdzenie wniosku");

        ActionType actionType5 = new ActionType();
        actionType5.setType("Odrzucenie wniosku");

        actionTypeRepository.save(actionType1);
        actionTypeRepository.save(actionType2);
        actionTypeRepository.save(actionType3);
        actionTypeRepository.save(actionType4);
        actionTypeRepository.save(actionType5);

        //Moton Type

        MotionType motionType = new MotionType();
        motionType.setType("Wniosek o dotację");
        motionTypeRepository.save(motionType);

        //Motion State

        MotionState motionState1 = new MotionState();
        motionState1.setState("Przyjęty");

        MotionState motionState2 = new MotionState();
        motionState2.setState("Przetwarzany");

        MotionState motionState3 = new MotionState();
        motionState3.setState("Zatwierdzony");

        MotionState motionState4 = new MotionState();
        motionState4.setState("Odrzucony");

        motionStateRepository.save(motionState1);
        motionStateRepository.save(motionState2);
        motionStateRepository.save(motionState3);
        motionStateRepository.save(motionState4);

        //Motion
        Motion motion = new Motion();
        motion.setMotionStateByMotionStateId(motionState1);
        motion.setMotionTypeByMotionTypeId(motionType);
        motion.setPersonalDataForMotions(personalData2);
        motionRepository.save(motion);


        //WHEN
        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
        MvcResult login = mockMvc.perform(post("/auth/loginAdmin")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content("""
                                {
                                    "login": "login",
                                    "password": "abba"
                                }""")
                )
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        JwtResponse response = objectMapper.readValue(login.getResponse().getContentAsString(), JwtResponse.class);

        MvcResult mvcResult = mockMvc.perform(post("/action/Consult")
                        .header("Authorization", response.getType() + " " + response.getToken())
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content(String.format("""
                                {
                                  "motionID": %s
                                }""", motion.getMotionId())))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //THEN
        String resp = mvcResult.getResponse().getContentAsString();
        assertThat(resp).isEqualTo("{\"message\":\"Wniosek procedowany pomyślnie\"}");

        List<Motion> motions = motionRepository.findAll();
        assertThat(motions.get(0).getMotionStateByMotionStateId().getState()).isEqualTo("Przetwarzany");

    }


    @Test
    @Transactional
    void proceedMotion() throws Exception {
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
        users2.setPersonalDataForUsers(personalData);
        users2.setPermissionLevel("ROLE_USER");

        usersRepository.save(users);
        usersRepository.save(users2);

        //Action Type
        ActionType actionType1 = new ActionType();
        actionType1.setType("Przyjęcie wniosku");

        ActionType actionType2 = new ActionType();
        actionType2.setType("Przetwarzanie wniosku");

        ActionType actionType3 = new ActionType();
        actionType3.setType("Konsultacje z wnioskodawcą");

        ActionType actionType4 = new ActionType();
        actionType4.setType("Zatwierdzenie wniosku");

        ActionType actionType5 = new ActionType();
        actionType5.setType("Odrzucenie wniosku");

        actionTypeRepository.save(actionType1);
        actionTypeRepository.save(actionType2);
        actionTypeRepository.save(actionType3);
        actionTypeRepository.save(actionType4);
        actionTypeRepository.save(actionType5);

        //Moton Type

        MotionType motionType = new MotionType();
        motionType.setType("Wniosek o dotację");
        motionTypeRepository.save(motionType);

        //Motion State

        MotionState motionState1 = new MotionState();
        motionState1.setState("Przyjęty");

        MotionState motionState2 = new MotionState();
        motionState2.setState("Przetwarzany");

        MotionState motionState3 = new MotionState();
        motionState3.setState("Zatwierdzony");

        MotionState motionState4 = new MotionState();
        motionState4.setState("Odrzucony");

        motionStateRepository.save(motionState1);
        motionStateRepository.save(motionState2);
        motionStateRepository.save(motionState3);
        motionStateRepository.save(motionState4);

        //Motion
        Motion motion = new Motion();
        motion.setMotionStateByMotionStateId(motionState1);
        motion.setMotionTypeByMotionTypeId(motionType);
        motion.setPersonalDataForMotions(personalData2);
        motionRepository.save(motion);

        //WHEN
        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
        MvcResult login = mockMvc.perform(post("/auth/loginAdmin")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content("""
                                {
                                    "login": "login",
                                    "password": "abba"
                                }""")
                )
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        JwtResponse response = objectMapper.readValue(login.getResponse().getContentAsString(), JwtResponse.class);

        MvcResult mvcResult = mockMvc.perform(post("/action/Proced")
                        .header("Authorization", response.getType() + " " + response.getToken())
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content(String.format("""
                                {
                                  "motionID": %s
                                }""", motion.getMotionId())))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //THEN
        String resp = mvcResult.getResponse().getContentAsString();
        assertThat(resp).isEqualTo("{\"message\":\"Wniosek procedowany pomyślnie\"}");

        List<Motion> motions = motionRepository.findAll();
        assertThat(motions.get(0).getMotionStateByMotionStateId().getState()).isEqualTo("Przetwarzany");

    }

}