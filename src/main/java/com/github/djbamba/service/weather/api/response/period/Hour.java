package com.github.djbamba.service.weather.api.response.period;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.djbamba.service.weather.api.response.Weather;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hour extends PeriodBase {
  private Double temp;

  @JsonProperty("feels_like")
  private Double feelsLike;

  private Integer pressure;
  private Integer humidity;

  @JsonProperty("dew_point")
  private Double dewPoint;

  private Double uvi;
  private Double clouds;
  private Integer visibility;

  @JsonProperty("wind_speed")
  private Double windSpeed;

  @JsonProperty("wind_deg")
  private Integer windDegrees;

  @JsonProperty("wind_gust")
  private Double windGust;

  private List<Weather> weather;

  @JsonProperty("pop")
  private Double precipitationProbability;
}
