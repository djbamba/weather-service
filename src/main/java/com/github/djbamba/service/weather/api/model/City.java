package com.github.djbamba.service.weather.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cities")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class City {
  @Id private Long id;
  @TextIndexed(weight = 1.1F) private String name;
  @TextIndexed private String state;
  private String country;
  private Coordinate coord;
}
