package com.github.djbamba.service.weather.test.api.route;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.github.djbamba.service.weather.api.response.WeatherResponse;
import com.github.djbamba.service.weather.api.response.onecall.OneCallWeatherResponse;
import com.github.djbamba.service.weather.api.router.WeatherRouter;
import com.github.djbamba.service.weather.api.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Mono;

@SpringBootTest
public class WeatherRouterTest {
  private WebTestClient client;

  @MockBean private WeatherService service;

  @Autowired private WeatherRouter router;

  @BeforeEach
  public void beforeEach() {
    this.client =
        WebTestClient.bindToRouterFunction(
                RouterFunctions.route()
                    .add(router.currentWeatherRoutes())
                    .add(router.oneCallWeatherRoutes())
                    .build())
            .configureClient()
            .build();
  }

  @Test
  @DisplayName("GET /current-weather?zip=66441 - Success")
  public void testGetCurrentWeatherByZipSuccess() throws Exception {
    WeatherResponse mockWeather = WeatherResponse.builder().cityName("Junction City").build();

    when(service.getCurrentWeatherByZip("66441", "imperial"))
        .thenReturn(Mono.just(new ResponseEntity<>(mockWeather, HttpStatus.OK)));

    client
        .get()
        .uri(ub -> ub.path("/current-weather").queryParam("zip", "66441").build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.cityName", is(mockWeather.getCityName()));
  }

  @Test
  @DisplayName("GET /current-weather?zip=00000 - Not Found")
  public void testGetCurrentWeatherByZipNotFound() throws Exception {
    when(service.getCurrentWeatherByZip(any(), any()))
        .thenReturn(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));

    client
        .get()
        .uri(ub -> ub.path("/current-weather").queryParam("zip", "00000").build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isNotFound();
  }

  @Test
  @DisplayName("GET /current-weather? - Bad Request")
  public void testGetCurrentWeatherBadRequest() throws Exception {
    client
        .get()
        .uri("/current-weather")
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .json("{\"reason\": \"zip is a required query parameter\"}");
  }

  @Test
  @DisplayName("GET /onecall?zip=66441 - Success")
  public void testGetCurrentWeatherByZipOneCallSuccess() throws Exception {
    OneCallWeatherResponse oneCall =
        OneCallWeatherResponse.builder().lat(39.0299).lon(-96.8396).build();
    when(service.getCurrentWeatherByZipOneCall(any(), any()))
        .thenReturn(Mono.just(ResponseEntity.ok(oneCall)));

    client
        .get()
        .uri(ub -> ub.path("/onecall").queryParam("zip", "66441").build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$", notNullValue(OneCallWeatherResponse.class));
  }

  @Test
  @DisplayName("GET /onecall?zip=00000 - Not Found")
  public void testGetCurrentWeatherByZipOneCallNotFound() throws Exception {
    when(service.getCurrentWeatherByZipOneCall(any(), any()))
        .thenReturn(Mono.just(ResponseEntity.notFound().build()));

    client
        .get()
        .uri(ub -> ub.path("/onecall").queryParam("zip", "00000").build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectBody()
        .isEmpty();
  }

  @Test
  @DisplayName("GET /onecall? - Bad Request")
  public void testGetCurrentWeatherByZipOneCallBadRequest() throws Exception {
    client
        .get()
        .uri("/onecall")
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .json("{\"reason\": \"zip is a required query parameter\"}");
  }
}
