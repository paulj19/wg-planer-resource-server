package com.wgplaner.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegistrationControllerTest {
    @Autowired
    private RegistrationController registrationController;

    private MockMvc mockMvc;
    private RegistrationDto registrationDto;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
        registrationDto = getValidRegistrationDto();
    }

    @Test
    public void whenPostWithValidData_shouldCreateAndRespondWithNewCreatedUser() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).content(asJsonString(registrationDto)));

        //assert
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
        resultActions.andExpect(content().string(asJsonString(registrationDto)));
    }

    @ParameterizedTest
    @MethodSource(value = "data")
    public void whenPostWithInValidData_shouldNotCreateUserAndRespondWith422(RegistrationDto invalidRegistrationDto, List<String> expectedErrorMessages) throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(invalidRegistrationDto))
        );
        //assert
        resultActions.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
        for(String expectedErrorMessage : expectedErrorMessages) {
            resultActions.andExpect(MockMvcResultMatchers.content().string(containsString(expectedErrorMessage)));
        }
    }

    private static Stream<Arguments> data() {
        final String pwErrorExpectedMessage = """
            password must meet the following criteria must be start-of-string
             a digit must occur at least once
             a lower case letter must occur at least once
             an upper case letter must occur at least once
             a special character must occur at least once
             no whitespace allowed in the entire string
             anything, at least eight places though end-of-string
            """;
        return Stream.of(
                //username < 5
                Arguments.of(
                        new RegistrationDto("a", "foo@email.com", "Password123!", "Password123!"),
                        List.of("username must be between 4 and 32 chars, only letters numbers and . with a word prefix and suffix")
                ),
                //username > 32
                Arguments.of(
                        new RegistrationDto("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "foo@email.com", "Password123!", "Password123!"),
                        List.of("username must be between 4 and 32 chars, only letters numbers and . with a word prefix and suffix")
                ),
                //invalid email
                Arguments.of(
                        new RegistrationDto("username", "fooemail.com", "Password123!", "Password123!"),
                        List.of("Invalid email")
                ),
                //pw regex should be well tested
                //pw regex from https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
                //pw no capital
                Arguments.of(
                        new RegistrationDto("username", "foo@email.com", "password123!", "password123!"),
                        List.of(pwErrorExpectedMessage)
                ),
                //pw no numerical
                Arguments.of(
                        new RegistrationDto("username", "foo@email.com", "Password!", "Password!"),
                        List.of(pwErrorExpectedMessage)
                ),
                //pw no special chars
                Arguments.of(
                        new RegistrationDto("username", "foo@email.com", "Password123", "Password123"),
                        List.of(pwErrorExpectedMessage)
                ),
                //pw not match
                Arguments.of(
                        new RegistrationDto("username", "foo@email.com", "Password123!", "PAssword123!"),
                        List.of("The password fields must match")
                ),
                //username > 32 && invalid email && pw no numerical
                Arguments.of(
                        new RegistrationDto("a", "fooemail.com", "Password!", "Password123!"),
                        List.of("username must be between 4 and 32 chars, only letters numbers and . with a word prefix and suffix", "Invalid email", pwErrorExpectedMessage, "The password fields must match")
                )
        );
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private RegistrationDto getValidRegistrationDto() {
        return new RegistrationDto("username", "foo@email.com", "Password123!", "Password123!");
    }
}
