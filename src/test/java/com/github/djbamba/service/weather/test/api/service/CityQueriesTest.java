package com.github.djbamba.service.weather.test.api.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.github.djbamba.service.weather.api.model.City;
import com.github.djbamba.service.weather.api.model.Coordinate;
import com.github.djbamba.service.weather.api.service.CityQueries;
import com.github.djbamba.service.weather.test.ext.MongoExtension;
import com.github.djbamba.service.weather.test.ext.MongoJsonFile;
import com.github.djbamba.service.weather.test.ext.MongoTemplateProvider;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexDefinition;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.index.TextIndexDefinition.TextIndexDefinitionBuilder;
import org.springframework.test.context.TestPropertySource;

@ExtendWith(MongoExtension.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class CityQueriesTest implements MongoTemplateProvider {
  @Autowired private MongoTemplate mongoTemplate;

  @Autowired private CityQueries cityQueries;

  @Override
  public MongoTemplate getMongoTemplate() {
    return this.mongoTemplate;
  }

  @Override
  public Optional<IndexDefinition> getIndexDefinition() {
    TextIndexDefinition cityNameAndState =
        new TextIndexDefinitionBuilder().onField("name", 1.1F).onField("state").build();

    return Optional.of(cityNameAndState);
  }

  @Test
  @DisplayName("Test findAllByName")
  @MongoJsonFile(value = "test-cities.json", classType = City.class, collectionName = "cities")
  public void citiesByName() {
    List<City> results = cityQueries.findAllByName("Junction");
    assertThat("No cities should be returned", results.isEmpty());

    results = cityQueries.findAllByName("Stockton");
    Coordinate expectedCoord = new Coordinate(-99.265099D, 39.438068D);

    assertThat(results.size(), is(1));
    assertThat(results.get(0).getName(), is("Stockton"));
    assertThat(results.get(0).getState(), is("KS"));
    assertThat(results.get(0).getCountry(), is("US"));
    assertThat(results.get(0).getCoord(), is(expectedCoord));
  }

  @Test
  @DisplayName("Test findAllCities")
  @MongoJsonFile(value = "test-cities.json", classType = City.class, collectionName = "cities")
  public void testFindAllCities() throws Exception {
    List<City> allCities = cityQueries.findAllCities();

    assertThat(allCities.size(), is(30));
  }

  @Test
  @DisplayName("Test findAllCitiesByState - KY")
  @MongoJsonFile(value = "test-cities.json", classType = City.class, collectionName = "cities")
  public void testFindAllCitiesByState() throws Exception {
    List<City> allCities = cityQueries.findAllByState("KY");

    assertThat(allCities.size(), is(4));
    assertThat(
        "All states should be KY",
        allCities.stream().allMatch(city -> city.getState().equalsIgnoreCase("KY")));
  }

  @Test
  @DisplayName("Test findCityByCoordinates - Found")
  @MongoJsonFile(value = "test-cities.json", classType = City.class, collectionName = "cities")
  public void testFindCityByCoordinatesFound() throws Exception {
    Coordinate redChuteCoord = new Coordinate(-93.613228D, 32.555981D);
    Optional<City> cityResult = cityQueries.findByCoord(redChuteCoord);

    assertThat("City was not found", cityResult.isPresent());
    assertThat(cityResult.get().getId(), is(4048888L));
    assertThat(cityResult.get().getName(), is("Red Chute"));
    assertThat(cityResult.get().getState(), is("LA"));
    assertThat(cityResult.get().getCountry(), is("US"));
  }

  @Test
  @DisplayName("Test findCityByCoordinates - Not Found")
  @MongoJsonFile(value = "test-cities.json", classType = City.class, collectionName = "cities")
  public void testFindCityByCoordinatesNotFound() throws Exception {
    Coordinate noCityCoord = new Coordinate(145.2149D, 14.1509D);
    Optional<City> cityResult = cityQueries.findByCoord(noCityCoord);

    assertThat("City was found when it shouldn't be", !cityResult.isPresent());
  }
}
