package com.hexanome16.server.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * This class contains information about a greeting.
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public final class Greeting {
  private final long id;
  private final String content;
}
