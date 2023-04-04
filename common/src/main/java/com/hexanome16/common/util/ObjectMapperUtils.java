package com.hexanome16.common.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.Getter;

/**
 * Utility class to create a custom object mapper.
 */
public class ObjectMapperUtils {
  @Getter
  private static final ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(
          JsonInclude.Include.NON_NULL)
      .setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE)
      .registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES))
      .registerModule(new SimpleModule() {
        @Override
        public void setupModule(Module.SetupContext context) {
          super.setupModule(context);
          context.addBeanSerializerModifier(new CustomSerializerModifier());
        }
      });

  private ObjectMapperUtils() {
    super();
  }
}
