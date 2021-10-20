package com.github.djbamba.service.weather.api.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class Temperature extends TemperatureRange {
  private Double min;
  private Double max;
}
