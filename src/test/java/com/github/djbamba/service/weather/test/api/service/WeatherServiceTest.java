package com.github.djbamba.service.weather.test.api.service;

import com.github.djbamba.service.weather.api.response.WeatherResponse;
import com.github.djbamba.service.weather.api.response.onecall.OneCallWeatherResponse;
import com.github.djbamba.service.weather.api.service.WeatherService;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class WeatherServiceTest {
  @Autowired private WeatherService service;
  private WireMockServer wireMock;

  @BeforeEach
  public void setupClient() {
    wireMock = new WireMockServer(9090);
    wireMock.start();
  }

  @AfterEach
  public void stopServer() {
    wireMock.stop();
  }

  @Test
  @DisplayName("Test getCurrentWeatherByZip - Success")
  public void locationZip() throws Exception {
    Mono<ResponseEntity<WeatherResponse>> weatherRes =
        service.getCurrentWeatherByZip("66441", "imperial");

    StepVerifier.create(weatherRes)
        .consumeNextWith(
            res -> {
              Assertions.assertTrue(res.getStatusCode().is2xxSuccessful(), "Status should be 2xx");
              Assertions.assertEquals(
                  "Junction City", res.getBody().getCityName(), "City name does not match");
            })
        .expectComplete()
        .verify();
  }

  @Test
  @DisplayName("Test getCurrentWeatherByZip - Not Found")
  public void testGetCurrentWeatherByZipNotFound() throws Exception {
    Mono<ResponseEntity<WeatherResponse>> weatherResNotFound =
        service.getCurrentWeatherByZip("00000", "imperial");
    String errBody = "{\"cod\":\"404\",\"message\":\"city not found\"}";

    StepVerifier.create(weatherResNotFound)
        .expectErrorSatisfies(
            t -> {
              Assertions.assertTrue(t instanceof WebClientResponseException);
              WebClientResponseException resEx = (WebClientResponseException) t;
              Assertions.assertTrue(resEx.getStatusCode().is4xxClientError());
              Assertions.assertNotNull(resEx.getResponseBodyAsString());
              Assertions.assertEquals(errBody, resEx.getResponseBodyAsString());
            })
        .verify();
  }

  @Test
  @DisplayName("Test getCurrentWeatherByZipOneCall - Success")
  public void testGetCurrentWeatherByZipOneCall() throws Exception {
    Mono<ResponseEntity<OneCallWeatherResponse>> oneCallRes =
        service.getCurrentWeatherByZipOneCall("66441", "imperial");

    StepVerifier.create(oneCallRes)
        .consumeNextWith(
            res -> {
              Assertions.assertTrue(res.getStatusCode().is2xxSuccessful(), "Status should be 2xx");
            })
        .expectComplete()
        .verify();
  }

  @Test
  @DisplayName("Test getCurrentWeatherByZipOneCall - Not Found")
  public void testGetCurrentWeatherByZipOneCallNotFound() throws Exception {
    Mono<ResponseEntity<OneCallWeatherResponse>> weatherResNotFound =
        service.getCurrentWeatherByZipOneCall("00000", "imperial");
    String errBody = "{\"cod\":\"404\",\"message\":\"city not found\"}";

    StepVerifier.create(weatherResNotFound)
        .expectErrorSatisfies(
            t -> {
              Assertions.assertTrue(t instanceof WebClientResponseException);
              WebClientResponseException resEx = (WebClientResponseException) t;
              Assertions.assertTrue(resEx.getStatusCode().is4xxClientError());
              Assertions.assertNotNull(resEx.getResponseBodyAsString());
              Assertions.assertEquals(errBody, resEx.getResponseBodyAsString());
            })
        .verify();
  }
}
