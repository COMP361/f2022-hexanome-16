package com.hexanome16.server;

import java.lang.reflect.Field;

/**
 * Collection of static reflection utility methods.
 */
public class ReflectionUtils {
  //TODO: use where possible

  /**
   * Get field of class and make it public.
   *
   * @param object object containing field
   * @param name   name of field
   * @return field with name or null if field is missing
   */
  public static Field getFieldAndSetPublic(Object object, String name) {
    Field field;
    try {
      field = object.getClass().getDeclaredField(name);

      field.setAccessible(true);
      return field;
    } catch (NoSuchFieldException e) {
      return null;
    }
  }
}
