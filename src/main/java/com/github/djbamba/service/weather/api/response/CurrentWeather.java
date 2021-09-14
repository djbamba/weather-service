package com.github.djbamba.service.weather.api.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CurrentWeather {
private Double temp;
@JsonAlias("feels_like")
private Double feelsLike;
@JsonAlias("temp_min")
private Double tempMin;
@JsonAlias("temp_max")
private Double tempMax;
private Double pressure;
private Double humidity;
}
