package com.github.djbamba.service.weather.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cities")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class City {
private Long id;
private String name;
private String state;
private String country;
private Coordinate coord;
}
