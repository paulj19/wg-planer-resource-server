package com.wgplaner.auth;

import com.wgplaner.common.httpclient.HttpClient;
import com.wgplaner.common.util.JsonUtils;
import com.wgplaner.config.AuthServerConfig;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class AuthServerRequester {
  private final AuthServerConfig authServerConfig;

  public AuthServerRequester(AuthServerConfig authServerConfig) {
    this.authServerConfig = authServerConfig;
  }

  public Long registerUserAndFetchOid(String username, String password) {
    try {
      String body = JsonUtils.toJsonString(Map.of("username", username, "password", password));
      Long oid = HttpClient.makeRequest(authServerConfig.getUri() + "/register/new", getHeaders(), body, Long.class);
      if(oid == null) {
        throw new RuntimeException();
      }
      return oid;
    } catch (WebClientResponseException e) {
      throw new RuntimeException("User registration with authentication server failed. " + e.getMessage() + ". " + e.getResponseBodyAsString(), e);
    } catch (RuntimeException e) {
      throw new RuntimeException("User registration with authentication server failed. ", e);
    }
  }

  private MultiValueMap<String, String> getHeaders() {
      return new LinkedMultiValueMap<>(
              Map.of(
                      HttpHeaders.CONTENT_TYPE,
                      List.of(MediaType.APPLICATION_JSON_VALUE),
                      "Authorization",
                      List.of(
                              "Basic "
                                      + HttpHeaders.encodeBasicAuth(
                                      authServerConfig.getClientId(),
                                      authServerConfig.getClientSecret(),
                                      null))));
  }
}
