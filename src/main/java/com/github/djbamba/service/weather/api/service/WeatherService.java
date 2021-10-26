package com.github.djbamba.service.weather.api.service;

import com.github.djbamba.service.weather.api.response.WeatherResponse;
import com.github.djbamba.service.weather.api.response.onecall.OneCallWeatherResponse;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
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
                    .path("/weather")
                    .queryParam("zip", zip)
                    .queryParam("units", units)
                    .build())
        .retrieve()
        .toEntity(WeatherResponse.class);
  }

  public Mono<ResponseEntity<OneCallWeatherResponse>> getCurrentWeatherByZipOneCall(
      String zip, String units) {
    Mono<ResponseEntity<WeatherResponse>> singleApi = getCurrentWeatherByZip(zip, units);

    return singleApi.flatMap(
        res -> {
          if (!res.getStatusCode().isError()) {
            Objects.requireNonNull(res.getBody(), "WeatherResponse body cannot be null");
            return this.client
                .get()
                .uri(
                    uriBuilder ->
                        uriBuilder
                            .path("/onecall")
                            .queryParam("lat", "{lat}")
                            .queryParam("lon", "{lon}")
                            .queryParam("units", "{unit}")
                            .build(
                                res.getBody().getCoord().getLatitude(),
                                res.getBody().getCoord().getLongitude(),
                                units))
                .retrieve()
                .toEntity(OneCallWeatherResponse.class);
          }
          return Mono.just(ResponseEntity.status(res.getStatusCode()).build());
        });
  }
}
