package com.github.djbamba.service.weather.api.response.period;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.djbamba.service.weather.api.json.LocalDateTimeDeserializer;
import com.github.djbamba.service.weather.api.response.Temperature;
import com.github.djbamba.service.weather.api.response.TemperatureRange;
import com.github.djbamba.service.weather.api.response.Weather;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class Day extends PeriodBase {
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(shape = Shape.STRING, pattern = "MM-dd-uuuu HH:mm:ss")
  private LocalDateTime sunrise;

  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(shape = Shape.STRING, pattern = "MM-dd-uuuu HH:mm:ss")
  private LocalDateTime sunset;

  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(shape = Shape.STRING, pattern = "MM-dd-uuuu HH:mm:ss")
  private LocalDateTime moonrise;

  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(shape = Shape.STRING, pattern = "MM-dd-uuuu HH:mm:ss")
  private LocalDateTime moonset;

  @JsonProperty("moon_phase")
  private Double moonPhase;

  private Temperature temp;

  @JsonProperty("feels_like")
  private TemperatureRange feelsLike;

  private Double pressure;
  private Integer humidity;

  @JsonProperty("dew_point")
  private Double dewPoint;

  @JsonProperty("wind_speed")
  private Double windSpeed;

  @JsonProperty("wind_deg")
  private Integer windDegrees;

  @JsonProperty("wind_gust")
  private Double windGust;

  private List<Weather> weather;
  private Double clouds;
  private Double pop;
  private Double rain;

  private Double uvi;
}
