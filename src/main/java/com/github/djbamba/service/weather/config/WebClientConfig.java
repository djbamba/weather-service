package com.github.djbamba.service.weather.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class WebClientConfig {
  private final String KEY = "appid";

  @Value("${api.base.url}")
  private String apiUrl;

  @Value("${api.key}")
  private String apiKey;

  @Bean
  public WebClient webClient() {
    DefaultUriBuilderFactory defaultUriBuilderFactory =
        new DefaultUriBuilderFactory(
            UriComponentsBuilder.fromHttpUrl(apiUrl).queryParam(KEY, apiKey));

    return WebClient.builder().uriBuilderFactory(defaultUriBuilderFactory).build();
  }
}
