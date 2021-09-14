package com.github.djbamba.service.weather.api.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Clouds {

  @JsonAlias("all")
  private Double all;
}
