package com.github.djbamba.service.weather.test.ext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.djbamba.service.weather.test.util.TestFileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexDefinition;

@Slf4j
public class MongoExtension implements BeforeEachCallback, AfterEachCallback {
  private final Path DATA_PATH = Paths.get( "data", "json");
  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    context.getTestMethod().ifPresent(testMethod -> {
      Optional<MongoJsonFile> mongoJsonFile = Optional.ofNullable(
          testMethod.getAnnotation(MongoJsonFile.class));
      mongoJsonFile.ifPresent(testFile -> getMongoTemplate(context).ifPresent(template -> {
        try {
          String testFileContents = TestFileReader.readResourceFileAsString(DATA_PATH.resolve(testFile.value()));

          List<?> objects = mapper.readValue(testFileContents, mapper.getTypeFactory()
                  .constructCollectionType(List.class, testFile.classType()));

          objects.forEach(template::save);
          getIndexDefinition(context).ifPresent(idx -> template.indexOps(testFile.classType()).ensureIndex(idx));
        } catch (Exception e) {
          log.error("Error mapping JSON in beforeEach", e);
        }
      }));
    });
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    context.getTestMethod().ifPresent(testMethod -> {
      Optional<MongoJsonFile> mongoJsonFile = Optional.ofNullable(
          testMethod.getAnnotation(MongoJsonFile.class));
      mongoJsonFile.ifPresent(testFile -> getMongoTemplate(context).ifPresent(template -> {
        template.dropCollection(testFile.collectionName());
      }));
    });
  }


  private Optional<MongoTemplate> getMongoTemplate(ExtensionContext context) {
    return getMongoProvider(context).map(MongoTemplateProvider::getMongoTemplate);
  }

  private Optional<IndexDefinition> getIndexDefinition(ExtensionContext context) {
    return getMongoProvider(context).flatMap(MongoTemplateProvider::getIndexDefinition);
  }

  /**
   * Convenience method converting the test instance into MongoTemplateProvider.
   * @param context current test context
   * @return MongoTemplateProvider
   */
  private Optional<MongoTemplateProvider> getMongoProvider(ExtensionContext context) {
    return context.getTestClass().map(testClass -> {
      if (MongoTemplateProvider.class.isAssignableFrom(testClass)) {
        return context.getTestInstance().map(testInstance -> ((MongoTemplateProvider)testInstance));
      }
      return Optional.<MongoTemplateProvider>empty();
    }).orElse(Optional.empty());
  }
}
