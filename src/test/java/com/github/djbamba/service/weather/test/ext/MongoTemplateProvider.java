package com.github.djbamba.service.weather.test.ext;

import java.util.Optional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexDefinition;

public interface MongoTemplateProvider {
  MongoTemplate getMongoTemplate();

  Optional<IndexDefinition> getIndexDefinition();
}
