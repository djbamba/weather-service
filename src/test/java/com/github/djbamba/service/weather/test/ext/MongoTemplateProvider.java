package com.github.djbamba.service.weather.test.ext;

import org.springframework.data.mongodb.core.MongoTemplate;

public interface MongoTemplateProvider {
  MongoTemplate getMongoTemplate();
}
