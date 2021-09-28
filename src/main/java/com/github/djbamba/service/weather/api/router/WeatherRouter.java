package com.github.djbamba.service.weather.api.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import com.github.djbamba.service.weather.api.handler.WeatherHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
public class WeatherRouter {
  @Bean
  RouterFunction<ServerResponse> routes(WeatherHandler weatherHandler) {
    return RouterFunctions.route(
        GET("/current-weather").and(RequestPredicates.accept(MediaType.ALL)),
        weatherHandler::getWeatherByZip);
  }
}
