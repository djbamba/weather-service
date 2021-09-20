package com.github.djbamba.service.weather;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.djbamba.service.weather.api.model.City;
import com.github.djbamba.service.weather.repo.MongoCityRepository;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class WeatherCli implements CommandLineRunner {

  private Logger log = LoggerFactory.getLogger(WeatherCli.class);

  @Value("${populate.cities:false}")
  private boolean populateCityCollection;

  @Autowired private MongoCityRepository cityRepository;

  @Override
  public void run(String... args) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);

    if (populateCityCollection) {
      try (InputStream is =
          WeatherCli.class.getClassLoader().getResourceAsStream("city.list.json")) {

        List<City> cities = mapper.readValue(is, new TypeReference<List<City>>() {});
        cityRepository.bulkInsert(cities);

      } catch (IOException e) {
        log.error("Reading city.list.json", e);
      }
    }
  }
}
