package com.github.djbamba.service.weather.test.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.djbamba.service.weather.api.model.City;
import com.github.djbamba.service.weather.api.model.Coordinate;
import com.github.djbamba.service.weather.api.service.CityQueries;
import com.github.djbamba.service.weather.test.ext.MongoExtension;
import com.github.djbamba.service.weather.test.ext.MongoJsonFile;
import com.github.djbamba.service.weather.test.ext.MongoTemplateProvider;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;

@ExtendWith(MongoExtension.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class CityQueriesTest implements MongoTemplateProvider {
  @Autowired private MongoTemplate mongoOps;

  @Autowired private CityQueries cityQueries;

  @Override
  public MongoTemplate getMongoTemplate() {
    return this.mongoOps;
  }

  @Test
  @DisplayName("Test findAllByName")
  @MongoJsonFile(value = "test-cities.json", classType = City.class, collectionName = "cities")
  public void citiesByName() {
    List<City> results = cityQueries.findAllByName("Junction");
    assertTrue(results.isEmpty());

    results = cityQueries.findAllByName("Stockton");
    Coordinate expectedCoord = new Coordinate(-99.265099D, 39.438068D);

    assertEquals(1, results.size());
    assertEquals("Stockton", results.get(0).getName());
    assertEquals("KS", results.get(0).getState());
    assertEquals("US", results.get(0).getCountry());
    assertEquals(expectedCoord, results.get(0).getCoord());
  }

  @Test
  @DisplayName("Test findAllCities")
  @MongoJsonFile(value = "test-cities.json", classType = City.class, collectionName = "cities")
  public void testFindAllCities() throws Exception {
    List<City> allCities = cityQueries.findAllCities();

    assertEquals(30, allCities.size());
  }

  @Test
  @DisplayName("Test findAllCitiesByState - KY")
  @MongoJsonFile(value = "test-cities.json", classType = City.class, collectionName = "cities")
  public void testFindAllCitiesByState() throws Exception {
    List<City> allCities = cityQueries.findAllByState("KY");

    assertEquals(4, allCities.size());
    assertTrue(allCities.stream().allMatch(city -> city.getState().equalsIgnoreCase("KY")));
  }
}
