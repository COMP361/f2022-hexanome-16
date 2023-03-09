package com.hexanome16.server.controllers;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Configuration class that converts snake case parameters to camel case.
 * Used for consistency between the server and Lobby Service.
 * (taken from <a href="https://www.beyondjava.net/spring-boot-snake-case">here</a>)
 */
@Configuration
public class SnakeCaseApplicationConfiguration {
  /**
   * Converts snake case parameters to camel case.
   *
   * @return request filter that converts snake case parameters to camel case.
   */
  @Bean
  public OncePerRequestFilter snakeCaseConverterFilter() {
    return new OncePerRequestFilter() {
      @Override
      protected void doFilterInternal(@NonNull HttpServletRequest request,
                                      @NonNull HttpServletResponse response,
                                      @NonNull FilterChain filterChain) throws
          ServletException, IOException {
        final Map<String, String[]> parameters = new ConcurrentHashMap<>();

        for (String param : request.getParameterMap().keySet()) {
          String camelCaseParam = snakeToCamel(param);

          parameters.put(camelCaseParam, request.getParameterValues(param));
          parameters.put(param, request.getParameterValues(param));
        }

        filterChain.doFilter(new HttpServletRequestWrapper(request) {
          @Override
          public String getParameter(String name) {
            return parameters.containsKey(name) ? parameters.get(name)[0] : null;
          }

          @Override
          public Enumeration<String> getParameterNames() {
            return Collections.enumeration(parameters.keySet());
          }

          @Override
          public String[] getParameterValues(String name) {
            return parameters.get(name);
          }

          @Override
          public Map<String, String[]> getParameterMap() {
            return parameters;
          }
        }, response);
      }
    };
  }

  // found at https://www.geeksforgeeks.org/convert-snake-case-string-to-camel-case-in-java/#:~:text=replaceFirst()%20method%20to%20convert,next%20letter%20of%20the%20underscore.
  // Function to convert the string
  // from snake case to camel case
  private static String snakeToCamel(String str) {
    // Run a loop till string contains underscore
    while (str.contains("_")) {

      // Replace the first occurrence
      // of letter that present after
      // the underscore, to capitalize
      // form of next letter of underscore
      str = str
          .replaceFirst(
              "_[a-z]",
              String.valueOf(
                  Character.toUpperCase(
                      str.charAt(
                          str.indexOf("_") + 1))));
    }

    // Return string
    return str;
  }
}
