package com.github.djbamba.service.weather.api.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Weather condition codes
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Weather {

  //condition id
  private Long id;
  // weather parameters (rain, snow, extreme)
  private String main;
  // weather condition within the group
  private String description;
  // weather icon id
  private String icon;
}
