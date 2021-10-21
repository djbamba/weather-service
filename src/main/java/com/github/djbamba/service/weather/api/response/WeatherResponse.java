package com.github.djbamba.service.weather.api.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.djbamba.service.weather.api.json.LocalDateTimeDeserializer;
import com.github.djbamba.service.weather.api.model.Coordinate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Getter
@Setter
@ToString(exclude = {"METERS_IN_MILES"})
public class WeatherResponse {
  @Getter(AccessLevel.NONE)
  private final double METERS_IN_MILES = 1_609.344D;

  private Coordinate coord;
  private List<Weather> weather;
  private CurrentWeather main;
  // meters
  private Double visibility;
  private Wind wind;
  private Clouds clouds;
  // Time of data calculation, unix, UTC
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(shape = Shape.STRING, pattern = "MM-dd-uuuu HH:mm:ss")
  @JsonAlias("dt")
  private LocalDateTime dateTime;
  // Shift in seconds from UTC
  private Integer timezone;

  @JsonAlias("id")
  private Long cityId;

  @JsonAlias("name")
  private String cityName;

  private Integer cod;

  public Double getVisibility() {
    return (this.visibility == null) ? 0D : this.visibility / METERS_IN_MILES;
  }
}
