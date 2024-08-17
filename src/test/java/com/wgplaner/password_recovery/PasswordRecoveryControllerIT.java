package com.wgplaner.password_recovery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.wgplaner.BaseIT;
import com.wgplaner.auth.AuthServerRequester;
import com.wgplaner.common.util.JsonUtils;
import com.wgplaner.core.AuthServer;
import com.wgplaner.core.entity.UserProfile;
import com.wgplaner.core.repository.UserRepository;
import com.wgplaner.email_service.MailService;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PasswordRecoveryControllerIT extends BaseIT {
  @SpyBean
  private AuthServerRequester authServerRequester;

  @Autowired
  private PasswordRecoveryController passwordRecoveryController;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordRecoveryEmailRepository passwordRecoveryEmailRepository;

  @SpyBean
  private MailService mailService;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(passwordRecoveryController).build();
  }

  @Test
  public void shouldSendEmail() throws Exception {
    UserProfile userProfile = userRepository
        .save(new UserProfile("paulo", "diljosepaul@gmail.com", 1L, AuthServer.HOME_BREW));
    mockMvc.perform(MockMvcRequestBuilders.post("/password-recovery/initiate")
        .param("email", userProfile.getEmail())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk());

    PasswordRecoveryEmailEntity entity = passwordRecoveryEmailRepository.findByUserProfile(userProfile);
    System.out.println("XXX"+ entity.getUserProfile());
    System.out.println("XXX"+ entity.getCode());
    
    assertThat(entity).isNotNull();
    assertThat(entity.getUserProfile().getId()).isEqualTo(userProfile.getId());
    assertThat(entity.getCode()).isNotNull();
    assertThat(entity.getCreationDate()).isNotNull();
    verify(mailService).sendAsync(any(), any());
  }

  @Test
  public void shouldReturnEmailNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/password-recovery/initiate")
        .param("email", "xxx@yyy.com")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
        .andExpect(content().string(containsString("Email not found: " + "xxx@yyy.com")));
  }
}
