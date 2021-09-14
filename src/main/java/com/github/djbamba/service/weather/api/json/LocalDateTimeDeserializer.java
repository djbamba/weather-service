package com.github.djbamba.service.weather.api.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

  private final Logger log = LoggerFactory.getLogger(LocalDateTimeDeserializer.class);

  @Override
  public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    JsonNode node = p.getCodec().readTree(p);
    long epochSeconds = node.asLong();
    LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSeconds),
        ZoneId.systemDefault());
    log.info("{}", localDateTime);

    return localDateTime;
  }
}
