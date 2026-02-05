package com.mingc.subhub;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@EnabledIfEnvironmentVariable(named = "RUN_IT", matches = "1")
class SubhubIntegrationTest {

  @Container
  static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
      .withDatabaseName("subhub")
      .withUsername("subhub")
      .withPassword("subhub");

  @DynamicPropertySource
  static void props(DynamicPropertyRegistry r) {
    r.add("spring.datasource.url", postgres::getJdbcUrl);
    r.add("spring.datasource.username", postgres::getUsername);
    r.add("spring.datasource.password", postgres::getPassword);
  }

  @Autowired
  TestRestTemplate rest;

  @Test
  void happyPath_createUser_createSubscription_listActive() {
    // health
    var health = rest.getForEntity("/api/health", Map.class);
    assertEquals(200, health.getStatusCode().value());
    assertEquals("ok", health.getBody().get("status"));

    // create user
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    var createUser = new HttpEntity<>("{\"name\":\"Test\",\"email\":\"test@example.com\"}", headers);
    ResponseEntity<Map> userResp = rest.postForEntity("/api/users", createUser, Map.class);
    assertEquals(201, userResp.getStatusCode().value());
    Number userId = (Number) userResp.getBody().get("id");
    assertNotNull(userId);

    // create subscription
    var createSub = new HttpEntity<>(
        "{\"userId\":" + userId.longValue() + ",\"plan\":\"BASIC\"}",
        headers
    );
    ResponseEntity<Map> subResp = rest.postForEntity("/api/subscriptions", createSub, Map.class);
    assertEquals(201, subResp.getStatusCode().value());
    Number subId = (Number) subResp.getBody().get("id");
    assertNotNull(subId);
    assertEquals("TRIAL", subResp.getBody().get("status"));

    // activate
    ResponseEntity<Map> actResp = rest.postForEntity("/api/subscriptions/" + subId.longValue() + "/activate", null, Map.class);
    assertEquals(200, actResp.getStatusCode().value());
    assertEquals("ACTIVE", actResp.getBody().get("status"));

    // list active
    ResponseEntity<Map> listResp = rest.getForEntity("/api/subscriptions?status=ACTIVE&page=0&size=10", Map.class);
    assertEquals(200, listResp.getStatusCode().value());
    assertNotNull(listResp.getBody().get("items"));

    // cancel via PUT to test that endpoint still works
    var cancelReq = new HttpEntity<>("{\"status\":\"CANCELED\"}", headers);
    ResponseEntity<Map> cancelResp = rest.exchange(
        "/api/subscriptions/" + subId.longValue(),
        HttpMethod.PUT,
        cancelReq,
        Map.class
    );
    assertEquals(200, cancelResp.getStatusCode().value());
    assertEquals("CANCELED", cancelResp.getBody().get("status"));
  }
}
