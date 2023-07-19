package com.example.town_application.controller;

import com.example.town_application.model.Motion;
import com.example.town_application.model.PersonalData;
import com.example.town_application.model.Users;
import com.example.town_application.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

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
    @WithMockUser(roles = "ADMIN")
    void registerUser() throws Exception {

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


        //WHEN
        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
        MvcResult mvcResult = mockMvc.perform(post("/auth/register")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content(String.format("""
                                {
                                  "pesel": %s,
                                  "login": "test",
                                  "password": "abba",
                                  "role": "ROLE_ADMIN"
                                }""", personalData.getPesel())))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //THEN
        String resp = mvcResult.getResponse().getContentAsString();
        assertThat(resp).isEqualTo("{\"message\":\"Użytkownik zarejestrowany\"}");



        List<Users> users = usersRepository.findAll();
        assertThat(users.get(0).getPermissionLevel()).isEqualTo("ROLE_ADMIN");
        assertThat(users.get(0).getLogin()).isEqualTo("test");
        assertThat(users.get(0).getPersonalDataForUsers().getPesel()).isEqualTo(personalData.getPesel());




    }


}