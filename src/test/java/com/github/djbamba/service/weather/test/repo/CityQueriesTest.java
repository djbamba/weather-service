package com.github.djbamba.service.weather.repo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CityQueriesTest {
  private Logger log = LoggerFactory.getLogger(CityQueriesTest.class);

  @Autowired private CityQueries cityQueries;

  @Test
  public void citiesByName() {
    this.cityQueries.findAllByName("Junction").forEach(city -> Assertions.assertTrue(city.getName().contains("Junction")));
  }
}
