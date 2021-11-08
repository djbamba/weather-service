package com.github.djbamba.service.weather.api.handler;

import com.github.djbamba.service.weather.api.service.WeatherService;
import java.util.Objects;
import java.util.function.Function;
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
  private final Function<ServerRequest, String> getUnitOrDefault =
      sr -> sr.queryParam("unit").orElse("imperial");
  @Autowired private final WeatherService weatherService;
  private Logger log = LoggerFactory.getLogger(WeatherHandler.class);
  private final Function<String, Mono<ServerResponse>> badRequest = msg -> ServerResponse.badRequest().bodyValue(String.format("{\"reason\": "+"\"%s\"}", msg));

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
                          if (!res.getStatusCode().isError()) {
                            Objects.requireNonNull(res.getBody(), "Response body cannot be null");
                            return ServerResponse.ok().bodyValue(res.getBody());
                          }
                          return ServerResponse.status(res.getStatusCode()).build();
                        })
                    .onErrorResume(this::handleError))
        .orElse(badRequest.apply("zip is a required query parameter"));
  }

  public Mono<ServerResponse> getWeatherByZipOneCall(ServerRequest request) {
    String unit = getUnitOrDefault.apply(request);

    return request
        .queryParam("zip")
        .map(
            zip ->
                this.weatherService
                    .getCurrentWeatherByZipOneCall(zip, unit)
                    .flatMap(
                        res -> {
                          if (!res.getStatusCode().isError()) {
                            Objects.requireNonNull(res.getBody(), "Response body cannot be null");
                            return ServerResponse.ok().bodyValue(res.getBody());
                          }
                          return ServerResponse.status(res.getStatusCode()).build();
                        })
                    .onErrorResume(this::handleError))
        .orElse(badRequest.apply("zip is a required query parameter"));
  }

  private Mono<ServerResponse> handleError(Throwable t) {
    if (t instanceof WebClientResponseException) {
      WebClientResponseException resEx = ((WebClientResponseException) t);
      if (HttpStatus.NOT_FOUND.equals(resEx.getStatusCode())) {
        return ServerResponse.status(resEx.getStatusCode())
            .bodyValue(resEx.getResponseBodyAsString());
      }
      return ServerResponse.status(resEx.getStatusCode()).build();
    }
    return ServerResponse.unprocessableEntity().bodyValue(t.getLocalizedMessage());
  }
}
