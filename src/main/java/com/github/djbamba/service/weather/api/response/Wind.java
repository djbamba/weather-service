package com.github.djbamba.service.weather.api.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Wind {

  private Double speed;
  //direction,degrees (meteorological)
  @JsonAlias("deg")
  private Double degrees;
  private Double gust;
}
