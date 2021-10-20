package com.github.djbamba.service.weather.api.handler;

import com.github.djbamba.service.weather.api.service.WeatherService;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class WeatherHandler {
  private final Function<ServerRequest, String> getUnitOrDefault =
      sr -> sr.queryParam("unit").orElse("imperial");
  @Autowired private final WeatherService weatherService;
  private Logger log = LoggerFactory.getLogger(WeatherHandler.class);

  public WeatherHandler(WeatherService weatherService) {
    this.weatherService = weatherService;
  }

  /**
   * Retrieves the current weather using the provided zip
   *
   * @param request
   * @return current weather
   */
  public Mono<ServerResponse> getWeatherByZip(ServerRequest request) {
    String unit = getUnitOrDefault.apply(request);

    return request
        .queryParam("zip")
        .map(
            zip ->
                this.weatherService
                    .getCurrentWeatherByZip(zip, unit)
                    .flatMap(
                        res -> {
                          if (res.getStatusCode().is4xxClientError()) {
                            return ServerResponse.status(res.getStatusCode()).build();
                          }
                          return ServerResponse.ok().bodyValue(res);
                        }))
        .orElseGet(
            () -> ServerResponse.badRequest().bodyValue("zip is a required query parameter"));
  }

  public Mono<ServerResponse> getWeatherByZipOneCall(ServerRequest request) {
    String unit = getUnitOrDefault.apply(request);

    return request
        .queryParam("zip")
        .map(
            zip ->
                this.weatherService
                    .getCurrentWeatherByZipOneCall(zip, unit)
                    .flatMap(res -> ServerResponse.ok().bodyValue(res)))
        .orElseGet(
            () -> ServerResponse.badRequest().bodyValue("zip is a required query parameter"));
  }
}
