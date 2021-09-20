package com.github.djbamba.service.weather.repo;

import com.github.djbamba.service.weather.api.model.City;
import com.github.djbamba.service.weather.api.model.Coordinate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

/**
 * Repo interface to access Cities
 */
public interface CityRepository extends Repository<City, Long> {
  City save(City city);

  List<City> findAllByName(String name);

  List<City> findAllByState(String state);

  Optional<City> findByCoord(Coordinate coord);
}
