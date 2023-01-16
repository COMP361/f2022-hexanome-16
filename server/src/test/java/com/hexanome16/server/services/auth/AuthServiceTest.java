package com.hexanome16.server.services.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.hexanome16.server.util.UrlUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

class AuthServiceTest {

  AuthService authService;

  @BeforeEach
  void setup() {
    // Mock autowired dependencies
    RestTemplateBuilder restTemplateBuilderMock = Mockito.mock(RestTemplateBuilder.class);

    /*
     when(restTemplateBuilderMock.postForEntity()).thenReturn();
     when(restTemplateBuilderMock.getForEntity()).thenReturn();
     when(restTemplateBuilderMock.delete()).thenReturn();
    */

    UrlUtils urlUtilsMock = Mockito.mock(UrlUtils.class);

//    when(urlUtilsMock.createLobbyServiceUri()).thenReturn();

    authService = new AuthService(restTemplateBuilderMock, urlUtilsMock);
  }

  @Test
  void login() {
  }

  @Test
  void testLogin() {
  }

  @Test
  void getPlayer() {
  }

  @Test
  void logout() {
  }

  @Test
  void verifyPlayer() {
  }
}