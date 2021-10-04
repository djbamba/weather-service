package com.github.djbamba.service.weather;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.djbamba.service.weather.api.model.City;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.index.TextIndexDefinition.TextIndexDefinitionBuilder;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class WeatherCli implements CommandLineRunner {

  private Logger log = LoggerFactory.getLogger(WeatherCli.class);

  @Value("${populate.cities:false}")
  private boolean populateCityCollection;

  @Autowired private MongoOperations mongoOps;

  @Override
  public void run(String... args) {
    if (populateCityCollection) {
      clear();
      seed();
    }
  }

  private void clear() {
    this.mongoOps.remove(new Query(), City.class);
  }

  private void seed() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
    TextIndexDefinition cityNameAndState =
        new TextIndexDefinitionBuilder().onField("name", 1.1F).onField("state").build();

    try (InputStream is = WeatherCli.class.getClassLoader().getResourceAsStream("city.list.json")) {
      List<City> cities = mapper.readValue(is, new TypeReference<List<City>>() {});
      this.mongoOps.insertAll(cities);
      this.mongoOps.indexOps(City.class).ensureIndex(cityNameAndState);
    } catch (IOException e) {
      log.error("Reading city.list.json", e);
    }
  }
}
