package com.wgplaner.registration;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegistrationControllerTest {
    @Autowired
    private RegistrationController registrationController;

    private MockMvc mockMvc;
    private RegistrationDto registrationDto;

    @BeforeEach
    public void setup() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
        registrationDto = getRegistrationDto();
    }

    @Test
    public void whenPostWithValidData_shouldCreateAndRespondWithNewCreatedUser() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/register" ).contentType(MediaType.APPLICATION_JSON).content(asJsonString(registrationDto)));

        //assert
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
        resultActions.andExpect(content().string(asJsonString(registrationDto)));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private RegistrationDto getRegistrationDto() {
        return new RegistrationDto("username", "foo@email.com", "Password123!", "Password123!");
    }

}