package com.github.djbamba.service.weather.api.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class WeatherServiceTest {
  @Autowired private WebTestClient client;

  @Test
  public void locationZip(){
    client.get()
        .uri(uriBuilder -> uriBuilder.path("/current-weather").queryParam("zip", "66441").build())
        .exchange()
        .expectStatus()
        .isOk();
  }
}
