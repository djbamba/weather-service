package com.github.djbamba.service.weather.api.controller;

import com.github.djbamba.service.weather.api.response.WeatherResponse;
import com.github.djbamba.service.weather.api.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WeatherController {

  public final Logger log = LoggerFactory.getLogger(WeatherController.class);
  @Autowired private final WeatherService weatherService;

  @GetMapping("/current-weather")
  public WeatherResponse currentWeather(
      @RequestParam String zip,
      @RequestParam(required = false, defaultValue = "imperial") String units) {
    return weatherService.findWeatherByZip(zip, units).block();
  }
}
