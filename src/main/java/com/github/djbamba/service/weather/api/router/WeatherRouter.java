package com.github.djbamba.service.weather.api.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import com.github.djbamba.service.weather.api.handler.WeatherHandler;
import com.github.djbamba.service.weather.api.response.WeatherResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
public class WeatherRouter {
  @RouterOperations({
    @RouterOperation(
        operation =
            @Operation(
                operationId = "getWeatherByZip",
                summary = "Find current weather by zipcode",
                tags = {"CurrentWeather"},
                parameters = {
                  @Parameter(
                      in = ParameterIn.QUERY,
                      name = "zip",
                      description = "zipcode to lookup City")
                },
                responses = {
                  @ApiResponse(
                      responseCode = "200",
                      description = "successful operation",
                      content =
                          @Content(
                              schema = @Schema(implementation = WeatherResponse.class),
                              mediaType = "application/json")),
                  @ApiResponse(
                      responseCode = "404",
                      description = "City of zipcode not found/supported"),
                  @ApiResponse(responseCode = "400", description = "zip not provided")
                }))
  })
  @Bean
  public RouterFunction<ServerResponse> routes(WeatherHandler weatherHandler) {
    return RouterFunctions.route(
        GET("/current-weather").and(RequestPredicates.accept(MediaType.ALL)),
        weatherHandler::getWeatherByZip);
  }
}
