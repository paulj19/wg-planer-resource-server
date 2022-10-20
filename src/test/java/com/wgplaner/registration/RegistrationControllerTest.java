package com.wgplaner.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wgplaner.BaseIT;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RegistrationControllerTest extends BaseIT {
    @Autowired
    private RegistrationController registrationController;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
        String controllerResponse = resultActions.andReturn().getResponse().getContentAsString();
        String passwordFromResponse = StringUtils.substringBetween(controllerResponse, "\"password\":\"", "\"");

        assertThat(passwordEncoder.matches(registrationDto.password(), passwordFromResponse)).isTrue();
        assertThat(controllerResponse).contains("\"id\":1");
        assertThat(controllerResponse).contains("\"username\":" + "\"" + registrationDto.username() + "\"");
        assertThat(controllerResponse).contains("\"email\":" + "\"" + registrationDto.email() + "\"");
        assertThat(controllerResponse).contains("\"enabled\":true");
        assertThat(controllerResponse).contains("\"authorities\":[{\"authority\":\"ROLE_USER\"}]");
        assertThat(controllerResponse).contains("\"accountNonExpired\":false");
        assertThat(controllerResponse).contains("\"accountNonLocked\":true");
        assertThat(controllerResponse).contains("\"credentialsNonExpired\":true");
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
        for (String expectedErrorMessage : expectedErrorMessages) {
            resultActions.andExpect(content().string(containsString(expectedErrorMessage)));
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
                //username null
                Arguments.of(
                        new RegistrationDto(null, "foo@email.com", "Password123!", "Password123!"),
                        List.of("username must be between 4 and 32 chars, only letters numbers and . with a word prefix and suffix")
                ),
                //username empty
                Arguments.of(
                        new RegistrationDto("", "foo@email.com", "Password123!", "Password123!"),
                        List.of("username must be between 4 and 32 chars, only letters numbers and . with a word prefix and suffix")
                ),
                //invalid email
                Arguments.of(
                        new RegistrationDto("username", "fooemail.com", "Password123!", "Password123!"),
                        List.of("Invalid email")
                ),
                //email null
                Arguments.of(
                        new RegistrationDto("username", null, "Password123!", "Password123!"),
                        List.of("Invalid email")
                ),
                //email empty
                Arguments.of(
                        new RegistrationDto("username", "", "Password123!", "Password123!"),
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
                //pw null
                Arguments.of(
                        new RegistrationDto("username", "foo@email.com", null, "Password123!"),
                        List.of(pwErrorExpectedMessage)
                ),
                //pw empty
                Arguments.of(
                        new RegistrationDto("username", "foo@email.com", "", "Password123!"),
                        List.of(pwErrorExpectedMessage)
                ),
                //confirm-pw null
                Arguments.of(
                        new RegistrationDto("username", "foo@email.com", "Password123!", null),
                        List.of(pwErrorExpectedMessage)
                ),
                //confirm-pw empty
                Arguments.of(
                        new RegistrationDto("username", "foo@email.com", "Password123!", ""),
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

    @ParameterizedTest
    @ValueSource(strings = {"@abcd", "!abcd", ".abcd", "ab^cd", "a&aaaa", ".abdc", "ab..cd", "abdc."})
    public void whenPostWithInValidUserName_shouldNotCreateUserAndRespondWith422(String username) throws Exception {
        RegistrationDto registrationDto = new RegistrationDto(username, "foo@email.com", "Password!", "Password123!");
        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(registrationDto))
        );
        //assert
        resultActions.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
        resultActions.andExpect(content().string(containsString("username must be between 4 and 32 chars, only letters numbers and . with a word prefix and suffix")));
    }

    @Test
    public void whenPostWithNonUniqueUsername_shouldNotCreateUserAndRespondWith422() throws Exception {
        RegistrationDto registrationDto = new RegistrationDto("user123", "foo@email.com", "Password123!", "Password123!");
        //pre-condition when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(registrationDto))
        );
        //pre-condition assert
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
        //everything except username different
        registrationDto = new RegistrationDto("user123", "bar@email.com", "Berlin123!", "Berlin123!");
        //when
        resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(registrationDto))
        );
        //pre-condition assert
        resultActions.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
        resultActions.andExpect(content().string(containsString("username and email must be unique")));
    }

    @Test
    public void whenPostWithNonUniqueEmail_shouldNotCreateUserAndRespondWith422() throws Exception {
        RegistrationDto registrationDto = new RegistrationDto("userABC", "foo@email.com", "Password123!", "Password123!");
        //pre-condition when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(registrationDto))
        );
        //pre-condition assert
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
        //everything except username different
        registrationDto = new RegistrationDto("user123", "foo@email.com", "Berlin123!", "Berlin123!");
        //when
        resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(registrationDto))
        );
        //pre-condition assert
        resultActions.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
        resultActions.andExpect(content().string(containsString("username and email must be unique")));
    }

    @Test
    public void whenGetWithNotRegisteredUsername_shouldRespondAsAvailable() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/register/username-available")
                .param("username", "user1")
                .contentType(MediaType.APPLICATION_JSON)
        );
        //assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(content().string(containsStringIgnoringCase("true")));
    }

    @Test
    public void whenGetWithRegisteredUsername_shouldRespondAsNotAvailable() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/register/username-available")
                .param("username", "user1")
                .contentType(MediaType.APPLICATION_JSON)
        );
        //assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(content().string(containsStringIgnoringCase("true")));
        resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).content(asJsonString(registrationDto)));
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
        //when
        resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/register/username-available")
                .param("username", "user1")
                .contentType(MediaType.APPLICATION_JSON)
        );
        //assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(content().string(containsStringIgnoringCase("false")));
    }

//    @ParameterizedTest
//    @ValueSource(strings = {"@abcd", "!abcd", ".abcd", "ab^cd", "a&aaaa", ".abdc", "ab..cd", "abdc."})
//    public void whenGetWithInvalidUsername_shouldRespondAsInvalid(String username) throws Exception {
//        //when
//        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/register/username-available")
//                .param("username", "user1")
//                .contentType(MediaType.APPLICATION_JSON)
//        );
        //assert
//        resultActions.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
//        resultActions.andExpect(content().string(containsString("username must be between 4 and 32 chars, only letters numbers and . with a word prefix and suffix")));
//    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private RegistrationDto getValidRegistrationDto() {
        return new RegistrationDto("user1", "foo@email.com", "Password123!", "Password123!");
    }
}
