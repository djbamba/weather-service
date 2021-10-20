package com.github.djbamba.service.weather.api.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TemperatureRange {
  protected Double morn;
  protected Double day;
  protected Double eve;
  protected Double night;
}
