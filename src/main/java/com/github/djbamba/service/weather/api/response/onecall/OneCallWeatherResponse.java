package com.github.djbamba.service.weather.api.response.onecall;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.djbamba.service.weather.api.response.period.Current;
import com.github.djbamba.service.weather.api.response.period.Day;
import com.github.djbamba.service.weather.api.response.period.Hour;
import com.github.djbamba.service.weather.api.response.period.Minute;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"METERS_IN_MILES"})
public class OneCallWeatherResponse {
  @Getter(AccessLevel.NONE)
  private final double METERS_IN_MILES = 1_609.344D;

  private Double lat;
  private Double lon;

  @JsonProperty("timezone")
  private String timeZone;

  @JsonProperty("timezone_offset")
  private Integer timeZoneOffset;

  private Current current;
  private List<Minute> minutely;
  private List<Hour> hourly;
  private List<Day> daily;
}
