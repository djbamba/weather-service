package com.github.djbamba.service.weather.api.response.period;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.djbamba.service.weather.api.json.LocalDateTimeDeserializer;
import com.github.djbamba.service.weather.api.response.Weather;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Current extends PeriodBase {

  @JsonProperty("feels_like")
  protected Double feelsLike;

  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(shape = Shape.STRING, pattern = "MM-dd-uuuu HH:mm:ss")
  private LocalDateTime sunrise;

  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(shape = Shape.STRING, pattern = "MM-dd-uuuu HH:mm:ss")
  private LocalDateTime sunset;

  private Double temp;
  private Integer humidity;
  private Double pressure;

  @JsonProperty("dew_point")
  private Double dewPoint;

  private Integer uvi;
  private Double clouds;
  private Double visibility;

  @JsonProperty("wind_speed")
  private Double windSpeed;

  @JsonProperty("wind_deg")
  private Integer windDegrees;

  @JsonProperty("wind_gust")
  private Double windGust;

  private List<Weather> weather;
}
