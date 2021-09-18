package com.github.djbamba.service.weather.api.service;

import com.github.djbamba.service.weather.api.response.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;

@Service
public class WeatherService {
  public final Logger log = LoggerFactory.getLogger(WeatherService.class);
  private final WebClient client;

  @Value("${api.key}")
  private String apiKey;

  @Value("${api.base.url}")
  private String apiUrl;

  @Autowired
  public WeatherService(WebClient.Builder wcBuilder) {
    this.client = wcBuilder.baseUrl(apiUrl).build();
  }

  public Mono<WeatherResponse> findWeatherByZip(String zip, String units) {
    ResponseSpec currentWeather =
        this.client
            .get()
            .uri(
                apiUrl,
                uriBuilder ->
                    uriBuilder
                        .queryParam("zip", zip)
                        .queryParam("units", units)
                        .queryParam("appid", apiKey)
                        .build())
            .retrieve();

    log.debug("ZIP: {} KEY: {}", zip, apiKey);
    return currentWeather.bodyToMono(WeatherResponse.class);
  }
}
