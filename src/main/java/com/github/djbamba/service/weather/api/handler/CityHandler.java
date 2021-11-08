package com.github.djbamba.service.weather.api.handler;

import com.github.djbamba.service.weather.api.model.City;
import com.github.djbamba.service.weather.api.model.Coordinate;
import com.github.djbamba.service.weather.api.service.CityQueries;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CityHandler {
  private final Function<String, Mono<ServerResponse>> badRequest =
      msg -> ServerResponse.badRequest().bodyValue(String.format("{\"reason\": " + "\"%s\"}", msg));
  @Autowired private CityQueries cityService;

  public Mono<ServerResponse> findCitiesByName(ServerRequest request) {
    Optional<String> cityName = request.queryParam("name");
    return cityName
        .map(
            name -> {
              List<City> allByName = cityService.findAllByName(name);

              return request
                  .queryParam("state")
                  .map(
                      st ->
                          listToResponse(
                              allByName.stream()
                                  .filter(city -> st.equalsIgnoreCase(city.getState()))
                                  .collect(Collectors.toList())))
                  .orElse(listToResponse(allByName));
            })
        .orElse(badRequest.apply("name is a required query parameter"));
  }

  public Mono<ServerResponse> findCityByCoordinates(ServerRequest request) {
    Optional<String> latitude = request.queryParam("lat");
    Optional<String> longitude = request.queryParam("lon");

    if (!latitude.isPresent() || !longitude.isPresent()) {
      return badRequest.apply("lat and lon are both required");
    }

    return cityService
        .findByCoord(
            new Coordinate(Double.valueOf(longitude.get()), Double.valueOf(latitude.get())))
        .map(city -> ServerResponse.ok().bodyValue(city))
        .orElse(ServerResponse.notFound().build());
  }

  private Mono<ServerResponse> listToResponse(List<?> content) {
    if (content.isEmpty()) {
      return ServerResponse.noContent().build();
    }
    return ServerResponse.ok().bodyValue(content);
  }
}
