package com.github.djbamba.service.weather.repo;

import com.github.djbamba.service.weather.api.model.City;
import com.github.djbamba.service.weather.api.model.Coordinate;
import com.mongodb.bulk.BulkWriteResult;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class CityQueries {
  private final Logger log = LoggerFactory.getLogger(CityQueries.class);
  @Autowired private final MongoOperations ops;

  public CityQueries(MongoOperations ops) {
    Assert.notNull(ops, "MongoOperations cannot be null");

    this.ops = ops;
  }

  public void bulkInsert(List<City> cities) {
    BulkOperations bulkOps = ops.bulkOps(BulkMode.UNORDERED, City.class);
    BulkWriteResult result = bulkOps.insert(cities).execute();
    log.debug("Bulk Insert: {}", result);
  }

  private void bulkUpdate(List<Pair<Query, Update>> updates) {
    BulkOperations bulkOps = ops.bulkOps(BulkMode.UNORDERED, City.class);
    bulkOps.upsert(updates);
    BulkWriteResult result = bulkOps.execute();
    log.debug("Bulk Update: {}", result);
  }

  public City save(City city) {
    return ops.save(city);
  }

  public List<City> findAllByName(String name) {
    TextCriteria textCriteria =
        TextCriteria.forDefaultLanguage().matching(name).caseSensitive(false);

    Query query = TextQuery.queryText(textCriteria).sortByScore().with(PageRequest.of(0, 5));

    return ops.find(query, City.class);
  }

  public List<City> findAllByState(String state) {
    Query query = Query.query(Criteria.where("state").is(state));

    return ops.find(query, City.class);
  }

  public Optional<City> findByCoord(Coordinate coord) {
    Query query = Query.query(Criteria.where("coord").is(coord));

    return Optional.ofNullable(ops.findOne(query, City.class));
  }
}
