package com.github.djbamba.service.weather.api.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import com.github.djbamba.service.weather.api.handler.CityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
public class CityRouter {
  @Autowired private CityHandler cityHandler;

  @Bean
  public RouterFunction<ServerResponse> getCitiesByNameRoute() {
    return RouterFunctions.route(
        GET("/cities")
            .and(accept(MediaType.ALL))
            .and(req -> !req.queryParam("lat").isPresent() && !req.queryParam("lon").isPresent()),
        cityHandler::findCitiesByName);
  }

  @Bean
  public RouterFunction<ServerResponse> getCityByCoordinateRoute() {
    return RouterFunctions.route(
        GET("/cities")
            .and(
                request ->
                    request.queryParam("lat").isPresent() || request.queryParam("lon").isPresent()),
        cityHandler::findCityByCoordinates);
  }
}
