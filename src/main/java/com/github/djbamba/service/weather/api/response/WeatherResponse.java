package com.github.djbamba.service.weather.api.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.djbamba.service.weather.api.json.LocalDateTimeDeserializer;
import com.github.djbamba.service.weather.api.model.Coordinate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
  private Coordinate coord;
  private List<Weather> weather;
  private CurrentWeather main;
  private Double visibility;
  private Wind wind;
  private Clouds clouds;
  //Time of data calculation, unix, UTC
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(shape = Shape.STRING, pattern = "MM-dd-uuuu HH:mm:ss")
  @JsonAlias("dt")
  private LocalDateTime dateTime;
  //Shift in seconds from UTC
  private Integer timezone;
  @JsonAlias("id")
  private Long cityId;
  @JsonAlias("name")
  private String cityName;
  private Integer cod;
}
