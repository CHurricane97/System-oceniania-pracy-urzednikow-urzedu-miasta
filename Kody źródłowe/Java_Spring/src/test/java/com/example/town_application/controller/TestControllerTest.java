package com.example.town_application.controller;

import com.example.town_application.repository.*;
import com.example.town_application.utility.JwtResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TestControllerTest {
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
    @WithMockUser(roles = "USER")
    void userAccess() throws Exception {
        //GIVEN
        //WHEN

        MvcResult mvcResult = mockMvc.perform(get("/test/user"))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //THEN
        String resp = mvcResult.getResponse().getContentAsString();
        assertThat(resp).isEqualTo("User Content.");
    }

    @Test
    @Transactional
    @WithMockUser(roles = "ADMIN")
    void adminAccess() throws Exception {
        //GIVEN
        //WHEN

        MvcResult mvcResult = mockMvc.perform(get("/test/admin"))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //THEN
        String resp = mvcResult.getResponse().getContentAsString();
        assertThat(resp).isEqualTo("Admin Board.");
    }

}