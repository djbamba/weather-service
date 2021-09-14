package com.github.djbamba.service.weather.api.controller;

import com.github.djbamba.service.weather.api.response.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

@RestController
public class WeatherController {

  public final Logger log = LoggerFactory.getLogger(WeatherController.class);

  @Value("${api.key}")
  private String apiKey;

  @Value("${api.base.url}")
  private String apiUrl;

  @GetMapping("/current-weather")
  public WeatherResponse currentWeather(
      @RequestParam String zip,
      @RequestParam(required = false, defaultValue = "imperial") String units) {
    WebClient client = WebClient.create(apiUrl);
    ResponseSpec currentWeather =
        client
            .get()
            .uri(
                uriBuilder ->
                    uriBuilder
                        .queryParam("zip", zip)
                        .queryParam("units", units)
                        .queryParam("appid", apiKey)
                        .build())
            .retrieve();

    log.debug("ZIP: {} KEY: {}", zip, apiKey);
    return currentWeather.bodyToMono(WeatherResponse.class).block();
  }
}
