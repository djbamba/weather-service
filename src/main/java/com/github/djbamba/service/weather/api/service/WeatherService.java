package com.github.djbamba.service.weather.api.service;

import com.github.djbamba.service.weather.api.response.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;

@Service
public class WeatherService {
  public final Logger log = LoggerFactory.getLogger(WeatherService.class);

  @Autowired private final WebClient client;

  public WeatherService(WebClient client) {
    this.client = client;
  }

  public Mono<WeatherResponse> findWeatherByZip(String zip, String units) {
    ResponseSpec currentWeather =
        this.client
            .get()
            .uri(uriBuilder -> uriBuilder.queryParam("zip", zip).queryParam("units", units).build())
            .retrieve();

    log.debug("ZIP: {}", zip);

    return currentWeather.bodyToMono(WeatherResponse.class);
  }
}
