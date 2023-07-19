package com.example.town_application.controller;

import com.example.town_application.model.*;
import com.example.town_application.model.dto.motionDTOs.MotionDetailsWithCounter;
import com.example.town_application.model.dto.motionDTOs.MotionTypeDTO;
import com.example.town_application.repository.*;
import com.example.town_application.utility.JwtResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MotionControllerTest {

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
    void getAllFinishedForUser() throws Exception {

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

        personalDataRepository.save(personalData);

        //Users
        //$2a$10$woyAIb7ohukpbYskdEspY.ATnRYsK63w08wcZEtJxi8KMR5bI1chu - abba

        Users users = new Users();
        users.setLogin("login");
        users.setPassword("$2a$10$woyAIb7ohukpbYskdEspY.ATnRYsK63w08wcZEtJxi8KMR5bI1chu");
        users.setPersonalDataForUsers(personalData);
        users.setPermissionLevel("ROLE_USER");

        usersRepository.save(users);

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
        motion.setMotionStateByMotionStateId(motionState4);
        motion.setMotionTypeByMotionTypeId(motionType);
        motion.setPersonalDataForMotions(personalData);

        Motion motion2 = new Motion();
        motion2.setMotionStateByMotionStateId(motionState3);
        motion2.setMotionTypeByMotionTypeId(motionType);
        motion2.setPersonalDataForMotions(personalData);

        Motion motion3 = new Motion();
        motion3.setMotionStateByMotionStateId(motionState2);
        motion3.setMotionTypeByMotionTypeId(motionType);
        motion3.setPersonalDataForMotions(personalData);

        motionRepository.save(motion);
        motionRepository.save(motion2);
        motionRepository.save(motion3);




        //WHEN
        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
        MvcResult login = mockMvc.perform(post("/auth/loginUser")
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

        MvcResult mvcResult = mockMvc.perform(get("/motion/getAllFinishedForUser").param("page","1")
                        .header("Authorization", response.getType() + " " + response.getToken())
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //THEN
        String resp = mvcResult.getResponse().getContentAsString();


        MotionDetailsWithCounter motionDetailsWithCounter = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MotionDetailsWithCounter.class);
        assertThat(motionDetailsWithCounter.getMotionDetailsList().size()).isEqualTo(2);
        assertThat(motionDetailsWithCounter.getMotionDetailsList().get(0).getMotionStateByMotionStateState()).isEqualTo(motionState3.getState());
        assertThat(motionDetailsWithCounter.getMotionDetailsList().get(1).getMotionStateByMotionStateState()).isEqualTo(motionState4.getState());







    }

    @Test
    @Transactional
    void addMotion() throws Exception {
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

        usersRepository.save(users);

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

        MvcResult mvcResult = mockMvc.perform(post("/motion/add")
                        .header("Authorization", response.getType() + " " + response.getToken())
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content(String.format("""
                                {
                                  "pesel": "%s",
                                  "motiontype": "%s"
                                }""", personalData2.getPesel(), motionType.getMotionTypeId())))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //THEN
        String resp = mvcResult.getResponse().getContentAsString();
        assertThat(resp).isEqualTo("{\"message\":\"Wniosek dodany\"}");

        List<Motion> motions = motionRepository.findAll();
        assertThat(motions.get(0).getMotionStateByMotionStateId().getState()).isEqualTo("Przyjęty");
        assertThat(motions.get(0).getPersonalDataForMotions().getPesel()).isEqualTo(personalData2.getPesel());
        assertThat(motions.get(0).getMotionTypeByMotionTypeId().getType()).isEqualTo(motionType.getType());





    }

}