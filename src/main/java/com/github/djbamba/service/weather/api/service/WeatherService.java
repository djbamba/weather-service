package com.github.djbamba.service.weather.api.service;

import com.github.djbamba.service.weather.api.response.WeatherResponse;
import com.github.djbamba.service.weather.api.response.onecall.OneCallWeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class WeatherService {
  public final Logger log = LoggerFactory.getLogger(WeatherService.class);

  @Autowired private final WebClient client;

  public WeatherService(WebClient client) {
    this.client = client;
  }

  public Mono<ResponseEntity<WeatherResponse>> getCurrentWeatherByZip(String zip, String units) {
    log.debug("ZIP: {}", zip);

    return this.client
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("weather")
                    .queryParam("zip", zip)
                    .queryParam("units", units)
                    .build())
        .retrieve()
        .toEntity(WeatherResponse.class)
        .onErrorResume(this::handleError);
  }

  public Mono<ResponseEntity<OneCallWeatherResponse>> getCurrentWeatherByZipOneCall(
      String zip, String units) {
    Mono<ResponseEntity<WeatherResponse>> singleApi = getCurrentWeatherByZip(zip, units);

    return singleApi.flatMap(
        res ->
            this.client
                .get()
                .uri(
                    uriBuilder ->
                        uriBuilder
                            .path("onecall")
                            .queryParam("lat", "{lat}")
                            .queryParam("lon", "{lon}")
                            .queryParam("units", "{unit}")
                            .build(
                                res.getBody().getCoord().getLatitude(),
                                res.getBody().getCoord().getLongitude(),
                                units))
                .retrieve()
                .toEntity(OneCallWeatherResponse.class));
  }

  private Mono<ResponseEntity<WeatherResponse>> handleError(Throwable t) {
    if (t instanceof WebClientResponseException) {
      WebClientResponseException resEx = ((WebClientResponseException) t);
      if (resEx.getRawStatusCode() == 404) {
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
      }
    }
    return Mono.just(ResponseEntity.unprocessableEntity().build());
  }
}
