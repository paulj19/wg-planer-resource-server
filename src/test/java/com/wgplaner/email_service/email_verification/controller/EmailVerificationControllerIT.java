package com.wgplaner.email_service.email_verification.controller;

import com.wgplaner.BaseIT;
import com.wgplaner.email_service.email_verification.EmailVerificationStatus;
import com.wgplaner.email_service.email_verification.entity.EmailVerificationState;
import com.wgplaner.email_service.email_verification.repository.EmailVerificationStateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;

class EmailVerificationControllerIT extends BaseIT {
    @Autowired
    private EmailVerificationController controller;
    @Autowired
    private EmailVerificationStateRepository repository;
    private MockMvc mockMvc;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void whenCalledWithCorrectUUIDAndEmail_shouldSetAsVerified() throws Exception {
        //given
        EmailVerificationState state = new EmailVerificationState(getTestUser());
        state.setStatus(EmailVerificationStatus.NOT_VERIFIED);
        repository.save(state);
        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/email-verification").param("id", state.getUuid().toString())
                .param("email", "diljosepaul%40gmail.com"));
        //then
        result.andExpect(MockMvcResultMatchers.status().isOk());
        EmailVerificationState updatedState = repository.findById(state.getId()).get();
        assertThat(updatedState.getStatus()).isEqualTo(EmailVerificationStatus.VERIFIED);
    }

    @Test
    public void whenCalledWithInCorrectUUIDAndCorrectEmail_shouldDoNothing() throws Exception {
        //given
        EmailVerificationState state = new EmailVerificationState(getTestUser());
        state.setStatus(EmailVerificationStatus.NOT_VERIFIED);
        repository.save(state);
        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/email-verification").param("id", "24fc3567-285b-4d9f-84bb-959780f383a1")
                .param("email", "diljosepaul%40gmail.com"));
        //then
        result.andExpect(MockMvcResultMatchers.status().isOk());
        EmailVerificationState updatedState = repository.findById(state.getId()).get();
        assertThat(updatedState.getStatus()).isEqualTo(EmailVerificationStatus.NOT_VERIFIED);
    }

    @Test
    public void whenCalledWithCorrectUUIDAndInCorrectEmail_shouldDoNothing() throws Exception {
        //given
        EmailVerificationState state = new EmailVerificationState(getTestUser());
        state.setStatus(EmailVerificationStatus.NOT_VERIFIED);
        repository.save(state);
        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/email-verification").param("id", state.getUuid().toString())
                .param("email", "foobar%40gmail.com"));
        //then
        result.andExpect(MockMvcResultMatchers.status().isOk());
        EmailVerificationState updatedState = repository.findById(state.getId()).get();
        assertThat(updatedState.getStatus()).isEqualTo(EmailVerificationStatus.NOT_VERIFIED);
    }
}