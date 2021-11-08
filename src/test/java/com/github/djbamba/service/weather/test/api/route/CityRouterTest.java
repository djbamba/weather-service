package com.github.djbamba.service.weather.test.api.route;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.djbamba.service.weather.api.model.City;
import com.github.djbamba.service.weather.api.router.CityRouter;
import com.github.djbamba.service.weather.api.service.CityQueries;
import com.github.djbamba.service.weather.test.util.TestFileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunctions;

@SpringBootTest
public class CityRouterTest {
  private WebTestClient client;
  @Autowired private CityRouter router;
  @MockBean private CityQueries cityService;

  @BeforeEach
  public void beforeEach() {
    this.client =
        WebTestClient.bindToRouterFunction(
                RouterFunctions.route()
                    .add(router.getCitiesByNameRoute())
                    .add(router.getCityByCoordinateRoute())
                    .build())
            .configureClient()
            .build();
  }

  @Test
  @DisplayName("GET /cities?name=County - Success")
  public void testGetCitiesByNameSuccess() throws Exception {
    when(cityService.findAllByName("County"))
        .thenReturn(
            getMockCities().stream()
                .filter(c -> c.getName().contains("County"))
                .collect(Collectors.toList()));

    this.client
        .get()
        .uri(ub -> ub.path("/cities").queryParam("name", "County").build())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(City.class)
        .hasSize(3);
  }

  @Test
  @DisplayName("GET /cities?name=Junkerton - Not Found")
  public void testGetCitiesByNameNotFound() throws Exception {
    when(cityService.findAllByName(Mockito.any())).thenReturn(Collections.emptyList());

    this.client
        .get()
        .uri(ub -> ub.path("/cities").queryParam("name", "Junkerton").build())
        .exchange()
        .expectStatus()
        .isNoContent()
        .expectBody()
        .isEmpty();
  }

  @Test
  @DisplayName("GET /cities? - Bad Request")
  public void testGetCitiesByNameBadRequest() throws Exception {
    this.client
        .get()
        .uri("/cities")
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .json("{\"reason\": \"name is a required query parameter\"}");
  }

  @Test
  @DisplayName("GET /cities?lat=28.180849&lon=-82.68177 - Success")
  public void testGetCityByCoordinateSuccess() throws Exception {
    City trinity =
        getMockCities().stream()
            .filter(city -> city.getName().equalsIgnoreCase("Trinity"))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Shouldn't be thrown"));
    when(cityService.findByCoord(any())).thenReturn(Optional.of(trinity));

    this.client
        .get()
        .uri(
            ub ->
                ub.path("/cities")
                    .queryParam("lat", "28.180849")
                    .queryParam("lon", "-82.68177")
                    .build())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.state")
        .value(is("FL"))
        .jsonPath("$.name")
        .value(is("Trinity"))
        .jsonPath("$.country")
        .value(is("US"))
        .jsonPath("$.coord.longitude")
        .value(is(-82.68177D))
        .jsonPath("$.coord.latitude")
        .value(is(28.180849D));
  }

  @Test
  @DisplayName("GET /cities?lat=000&lon=000 - Not Found")
  public void testGetCityByCoordinateNotFound() throws Exception {
    when(cityService.findByCoord(any())).thenReturn(Optional.empty());

    this.client
        .get()
        .uri(ub -> ub.path("/cities").queryParam("lat", "000").queryParam("lon", "000").build())
        .exchange()
        .expectStatus()
        .isNotFound();
  }

  @Test
  @DisplayName("GET /cities? - Bad Request")
  public void testGetCityByCoordinateBadRequest() throws Exception {
    this.client
        .get()
        .uri(ub -> ub.path("/cities").queryParam("lat", "000").build())
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .json("{\"reason\": \"lat and lon are both required\"}");
  }

  private List<City> getMockCities() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    String citiesString =
        TestFileReader.readResourceFileAsString(Paths.get("data", "json", "test-cities.json"));

    return mapper.readValue(citiesString, new TypeReference<List<City>>() {});
  }
}
