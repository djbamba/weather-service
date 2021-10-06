package com.github.djbamba.service.weather.api.response.period;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.djbamba.service.weather.api.json.LocalDateTimeDeserializer;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class PeriodBase {
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(shape = Shape.STRING, pattern = "MM-dd-uuuu HH:mm:ss")
  @JsonProperty("dt")
  protected LocalDateTime dateTime;
}
