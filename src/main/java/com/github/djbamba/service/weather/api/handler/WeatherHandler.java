package com.github.djbamba.service.weather.api.handler;

import com.github.djbamba.service.weather.api.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class WeatherHandler {
  @Autowired private final WeatherService weatherService;
  private Logger LOG = LoggerFactory.getLogger(WeatherHandler.class);

  public WeatherHandler(WeatherService weatherService) {
    this.weatherService = weatherService;
  }

  public Mono<ServerResponse> getWeatherByZip(ServerRequest request) {
    String unit = request.queryParam("units").orElse("imperial");

    request
        .queryParam("zip")
        .ifPresent(
            zip ->
                this.weatherService
                    .findWeatherByZip(zip, unit)
                    .flatMap(weatherResponse -> ServerResponse.ok().bodyValue(weatherResponse)));

    return request
        .queryParam("zip")
        .map(
            zip ->
                this.weatherService
                    .findWeatherByZip(zip, unit)
                    .flatMap(weatherResponse -> ServerResponse.ok().bodyValue(weatherResponse))
                    .onErrorResume(this::handleError))
        .orElseGet(
            () -> ServerResponse.badRequest().bodyValue("zip is a required query parameter"));
  }

  private Mono<ServerResponse> handleError(Throwable t) {
    if (t instanceof WebClientResponseException) {
      WebClientResponseException resEx = ((WebClientResponseException)t);
      if (resEx.getRawStatusCode() == 404) {
        return ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(resEx.getResponseBodyAsString());
      }
    }
    return ServerResponse.badRequest().bodyValue(t.getMessage());
  }
}
