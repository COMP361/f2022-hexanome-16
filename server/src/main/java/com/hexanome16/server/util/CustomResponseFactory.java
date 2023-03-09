package com.hexanome16.server.util;

import com.hexanome16.common.util.CustomHttpResponses;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Static factory for accessing pre-generated Error Responses.
 */
public class CustomResponseFactory {
  /**
   * Factory for creating ResponseEntity for any of our CustomHttpResponses.
   *
   * <p>Prefer using this when possible,
   * if really out of the ordinary and unique
   * use {@link #getCustomResponse}
   *
   * @param responseType enum to put in ResponseEntity
   * @return the response entity
   */
  public static ResponseEntity<String> getResponse(CustomHttpResponses responseType) {
    return getCustomResponse(responseType, responseType.getBody(), null);
  }

  /**
   * Factory for creating DeferredResult for any of our CustomHttpResponses.
   *
   * <p>Prefer using this when possible,
   * if really out of the ordinary and unique
   * use {@link #getDeferredCustomResponse}
   *
   * @param responseType enum to put in DeferredResult
   * @return the deferred result
   */
  public static DeferredResult<ResponseEntity<String>> getDeferredResponse(
      CustomHttpResponses responseType) {
    var result = new DeferredResult<ResponseEntity<String>>();
    result.setResult(getResponse(responseType));
    return result;
  }

  /**
   * Factory for creating ResponseEntity for any of our CustomHttpResponses.
   *
   * <p>Prioritize using {@link #getResponse} when possible,
   * if really out of the ordinary and unique
   * use this
   *
   * @param responseType enum to put in ResponseEntity
   * @param body         custom body to add instead of the responseType body
   * @param headers      the headers to add to the response
   *                     (leave null to use those in the CustomHttpResponse)
   * @return the response entity
   */
  public static ResponseEntity<String> getCustomResponse(CustomHttpResponses responseType,
                                                         String body,
                                                         Map<String, List<String>> headers) {
    if (headers == null && responseType.getHeaders() != null) {
      headers = new LinkedMultiValueMap<>();
      headers.putAll(responseType.getHeaders());
    }
    return new ResponseEntity<>(body,
        (MultiValueMap<String, String>) headers,
        HttpStatus.valueOf(responseType.getStatus()));
  }

  /**
   * Factory for creating Deferred for any of our CustomHttpResponses.
   *
   * <p>Prioritize using {@link #getDeferredResponse} when possible,
   * if really out of the ordinary and unique
   * use this
   *
   * @param responseType enum to put in ResponseEntity
   * @param body         custom body to add instead of the responseType body
   * @param headers      the headers to add to the response
   * @return the response entity
   */
  public static DeferredResult<ResponseEntity<String>> getDeferredCustomResponse(
      CustomHttpResponses responseType, String body, Map<String, List<String>> headers) {
    var result = new DeferredResult<ResponseEntity<String>>();
    result.setResult(getCustomResponse(responseType, body, headers));
    return result;
  }
}
