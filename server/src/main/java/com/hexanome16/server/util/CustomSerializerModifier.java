package com.hexanome16.server.util;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import java.util.List;

/**
 * Custom Jackson serializer modifier to remove Mockito mocks from JSON.
 * Taken from <a href="https://stackoverflow.com/a/51172616">here</a>
 */
public class CustomSerializerModifier extends BeanSerializerModifier {

  @Override
  public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
                                                   BeanDescription beanDesc,
                                                   List<BeanPropertyWriter> beanProperties) {
    // In this method you can add, remove or replace any of passed properties
    return beanProperties.stream().filter(p -> !p.getName().equals("mockitoInterceptor"))
        .collect(java.util.stream.Collectors.toList());
  }
}
