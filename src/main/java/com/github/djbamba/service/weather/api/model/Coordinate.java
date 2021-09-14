package com.github.djbamba.service.weather.api.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Coordinate {

  @JsonAlias(value = "lon")
  private Double longitude;
  @JsonAlias(value = "lat")
  private Double latitude;
}
