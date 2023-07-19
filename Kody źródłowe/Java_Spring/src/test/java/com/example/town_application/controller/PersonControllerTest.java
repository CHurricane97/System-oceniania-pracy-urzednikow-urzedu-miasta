package com.example.town_application.controller;

import com.example.town_application.model.Motion;
import com.example.town_application.model.PersonalData;
import com.example.town_application.model.Users;
import com.example.town_application.model.dto.personalDataDTOs.PersonalDataWithoutID;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PersonControllerTest {
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
    void getPersonalDataPesel() throws Exception {
        //GIVEN
        //Personal data
        PersonalData personalData = new PersonalData();
        personalData.setName("Marek");
        personalData.setSurname("Dzik");
        personalData.setDateOfBirth(Date.valueOf(LocalDate.now()));
        personalData.setPesel("11111111111");
        personalData.setCityCode("58-150");
        personalData.setCity("Strzegom");
        personalData.setStreet("Rybna");
        personalData.setHouseNumber("20");
        personalData.setFlatNumber("2");

        personalDataRepository.save(personalData);

        //WHEN


        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_16);

        MvcResult mvcResult = mockMvc.perform(get("/person/getdatapesel").param("pesel", personalData.getPesel()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //THEN
        PersonalDataWithoutID personalDataWithoutIDS = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PersonalDataWithoutID.class);

        assertThat(personalDataWithoutIDS.getName()).isEqualTo(personalData.getName());
        assertThat(personalDataWithoutIDS.getStreet()).isEqualTo(personalData.getStreet());
        assertThat(personalDataWithoutIDS.getSurname()).isEqualTo(personalData.getSurname());
        assertThat(personalDataWithoutIDS.getDateOfBirth().toLocalDate()).isEqualTo(personalData.getDateOfBirth().toLocalDate());
        assertThat(personalDataWithoutIDS.getPesel()).isEqualTo(personalData.getPesel());
        assertThat(personalDataWithoutIDS.getCity()).isEqualTo(personalData.getCity());
        assertThat(personalDataWithoutIDS.getCityCode()).isEqualTo(personalData.getCityCode());
        assertThat(personalDataWithoutIDS.getHouseNumber()).isEqualTo(personalData.getHouseNumber());
        assertThat(personalDataWithoutIDS.getFlatNumber()).isEqualTo(personalData.getFlatNumber());


    }


    @Test
    @Transactional
    void getPersonalDataToken() throws Exception {
        //GIVEN
        //Personal data
        PersonalData personalData = new PersonalData();
        personalData.setName("Marek");
        personalData.setSurname("Dzik");
        personalData.setDateOfBirth(Date.valueOf("2023-02-02"));
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
        users.setPermissionLevel("ROLE_ADMIN");

        usersRepository.save(users);

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


        MvcResult mvcResult = mockMvc.perform(get("/person/getdatatoken").header("Authorization", response.getType() + " " + response.getToken()))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //THEN
        PersonalDataWithoutID personalDataWithoutIDS = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PersonalDataWithoutID.class);

        assertThat(personalDataWithoutIDS.getName()).isEqualTo(personalData.getName());
        assertThat(personalDataWithoutIDS.getStreet()).isEqualTo(personalData.getStreet());
        assertThat(personalDataWithoutIDS.getSurname()).isEqualTo(personalData.getSurname());
        assertThat(personalDataWithoutIDS.getDateOfBirth().toLocalDate()).isEqualTo(personalData.getDateOfBirth().toLocalDate());
        assertThat(personalDataWithoutIDS.getPesel()).isEqualTo(personalData.getPesel());
        assertThat(personalDataWithoutIDS.getCity()).isEqualTo(personalData.getCity());
        assertThat(personalDataWithoutIDS.getCityCode()).isEqualTo(personalData.getCityCode());
        assertThat(personalDataWithoutIDS.getHouseNumber()).isEqualTo(personalData.getHouseNumber());
        assertThat(personalDataWithoutIDS.getFlatNumber()).isEqualTo(personalData.getFlatNumber());

    }

    @Test
    @Transactional
    @WithMockUser(roles = "ADMIN")
    void registerUser() throws Exception {
        //GIVEN
        //WHEN

        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);


        MvcResult mvcResult = mockMvc.perform(post("/person/add")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content("""
                                {
                                  "date_of_birth": "2023-02-02",
                                  "name": "test",
                                  "surname": "test2",
                                  "pesel": "22222222222",
                                  "city": "city",
                                  "city_code": "ccode",
                                  "street": "rybna",
                                  "house_number": "22",
                                  "flat_number": "5"
                                }"""))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //THEN
        String resp = mvcResult.getResponse().getContentAsString();
        assertThat(resp).isEqualTo("{\"message\":\"Dodano osobę\"}");


        List<PersonalData> personaldatas = personalDataRepository.findAll();


        assertThat(personaldatas.get(0).getName()).isEqualTo("test");
        assertThat(personaldatas.get(0).getStreet()).isEqualTo("rybna");
        assertThat(personaldatas.get(0).getSurname()).isEqualTo("test2");
        assertThat(personaldatas.get(0).getDateOfBirth().toLocalDate()).isEqualTo(Date.valueOf("2023-02-02").toLocalDate());
        assertThat(personaldatas.get(0).getPesel()).isEqualTo("22222222222");
        assertThat(personaldatas.get(0).getCity()).isEqualTo("city");
        assertThat(personaldatas.get(0).getCityCode()).isEqualTo("ccode");
        assertThat(personaldatas.get(0).getHouseNumber()).isEqualTo("22");
        assertThat(personaldatas.get(0).getFlatNumber()).isEqualTo("5");


    }

    @Test
    @Transactional
    @WithMockUser(roles = "ADMIN")
    void updateUser() throws Exception {

        //GIVEN

        PersonalData personalData = new PersonalData();
        personalData.setName("Mirosław");
        personalData.setSurname("Łąbądź");
        personalData.setDateOfBirth(Date.valueOf("2023-02-02"));
        personalData.setPesel("11111111111");
        personalData.setCityCode("58-150");
        personalData.setCity("Strzegom");
        personalData.setStreet("Morska");
        personalData.setHouseNumber("20");
        personalData.setFlatNumber("2");

        personalDataRepository.save(personalData);

        //WHEN

        MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);


        MvcResult mvcResult = mockMvc.perform(put("/person/update")
                        .accept(MEDIA_TYPE_JSON_UTF8).contentType(MEDIA_TYPE_JSON_UTF8)
                        .content("""
                                {
                                  "name": "test",
                                  "surname": "test2",
                                  "pesel": "11111111111",
                                  "city": "city",
                                  "city_code": "ccode",
                                  "street": "rybna",
                                  "house_number": "22",
                                  "flat_number": "5"
                                }"""))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        //THEN
        String resp = mvcResult.getResponse().getContentAsString();
        assertThat(resp).isEqualTo("{\"message\":\"Zmodyfikowano osobę\"}");


        List<PersonalData> personaldatas = personalDataRepository.findAll();


        assertThat(personaldatas.get(0).getName()).isEqualTo("test");
        assertThat(personaldatas.get(0).getStreet()).isEqualTo("rybna");
        assertThat(personaldatas.get(0).getSurname()).isEqualTo("test2");
        assertThat(personaldatas.get(0).getDateOfBirth().toLocalDate()).isEqualTo(Date.valueOf("2023-02-02").toLocalDate());
        assertThat(personaldatas.get(0).getPesel()).isEqualTo("11111111111");
        assertThat(personaldatas.get(0).getCity()).isEqualTo("city");
        assertThat(personaldatas.get(0).getCityCode()).isEqualTo("ccode");
        assertThat(personaldatas.get(0).getHouseNumber()).isEqualTo("22");
        assertThat(personaldatas.get(0).getFlatNumber()).isEqualTo("5");
    }

}