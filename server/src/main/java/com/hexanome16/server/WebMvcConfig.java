package com.hexanome16.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * TODO.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    configurer.addPathPrefix(
        "api",
        HandlerTypePredicate.forAnnotation(RestController.class).and(
            HandlerTypePredicate.forAnnotation(Service.class)
        )
    );
  }
}
