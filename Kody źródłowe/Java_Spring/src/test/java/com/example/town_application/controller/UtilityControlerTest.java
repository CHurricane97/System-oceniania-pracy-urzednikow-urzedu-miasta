package com.example.town_application.controller;

import com.example.town_application.model.*;
import com.example.town_application.model.dto.motionDTOs.MotionStateDTO;
import com.example.town_application.model.dto.motionDTOs.MotionTypeDTO;
import com.example.town_application.repository.*;
import com.example.town_application.utility.JwtResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
class UtilityControlerTest {
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
    @WithMockUser(roles = "ADMIN")
    @Transactional
    void getAllMotionTypes() throws Exception {
        //GIVEN


        //Moton Type

        MotionType motionType = new MotionType();
        motionType.setType("test");

        MotionType motionType2 = new MotionType();
        motionType2.setType("test2");

        motionTypeRepository.save(motionType);
        motionTypeRepository.save(motionType2);


        //WHEN


        MvcResult mvcResult = mockMvc.perform(get("/utility/getAllMotionTypes"))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //THEN
        MotionTypeDTO[] motionDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MotionTypeDTO[].class);
        assertThat(motionDTO[0].getMotionTypeId()).isEqualTo(motionType.getMotionTypeId());
        assertThat(motionDTO[0].getType()).isEqualTo(motionType.getType());
        assertThat(motionDTO[1].getMotionTypeId()).isEqualTo(motionType2.getMotionTypeId());
        assertThat(motionDTO[1].getType()).isEqualTo(motionType2.getType());

    }


    @Test
    @Transactional
    @WithMockUser(roles = "ADMIN")
    void addMotionType() throws Exception {
        //GIVEN
        //WHEN

        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
        MvcResult mvcResult = mockMvc.perform(post("/utility/addMotionType")
                .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                .content("""
                                {
                                  "type": "test"
                                }"""))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //THEN
        String resp = mvcResult.getResponse().getContentAsString();
        assertThat(resp).isEqualTo("{\"message\":\"Typ wniosku dodany\"}");


        List<MotionType> motionTypes = motionTypeRepository.findAll();
        assertThat(motionTypes.get(0).getType()).isEqualTo("test");
    }
}