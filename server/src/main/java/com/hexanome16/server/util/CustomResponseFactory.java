package com.hexanome16.server.util;

import com.hexanome16.common.util.CustomHttpResponses;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
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
    return getCustomResponse(responseType, responseType.getBody(), responseType.getHeaders());
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
   * @return the response entity
   */
  public static ResponseEntity<String> getCustomResponse(CustomHttpResponses responseType,
                                                         String body,
                                                         Map<String, List<String>> headers) {
    return new ResponseEntity<>(body, headers == null ? null : new LinkedMultiValueMap<>(headers),
        HttpStatus.valueOf(responseType.getStatus()));
  }

  /**
   * Factory for creating ResponseEntity for any of our Error Responses.
   *
   * <p>Prefer using this when possible,
   * if really out of the ordinary and unique
   * use {@link #getCustomErrorResponse}
   *
   * @param errorType enum ErrorResponse to put in ResponseEntity
   * @return the response entity
   */
  public static ResponseEntity<String> getErrorResponse(CustomHttpResponses errorType) {
    return new ResponseEntity<>(errorType.getBody(), HttpStatus.valueOf(errorType.getStatus()));
  }

  /**
   * Factory for creating DeferredResult for any of our Error Responses.
   *
   * <p>Prefer using this when possible,
   * if really out of the ordinary and unique
   * use {@link #getDeferredCustomErrorResponse}
   *
   * @param errorType enum ErrorResponse to put in DeferredResult
   * @return the deferred result
   */
  public static DeferredResult<ResponseEntity<String>> getDeferredErrorResponse(
      CustomHttpResponses errorType) {
    var result = new DeferredResult<ResponseEntity<String>>();
    result.setErrorResult(getErrorResponse(errorType));
    return result;
  }

  /**
   * Factory for creating ResponseEntity for any of our Error Responses.
   *
   * <p>Prioritize using {@link #getErrorResponse} when possible,
   * if really out of the ordinary and unique
   * use this
   *
   * @param errorType enum ErrorResponse to put in ResponseEntity
   * @param body      custom body to add instead of the errorType body
   * @return the response entity
   */
  public static ResponseEntity<String> getCustomErrorResponse(CustomHttpResponses errorType,
                                                              String body) {
    return new ResponseEntity<>(body, HttpStatus.valueOf(errorType.getStatus()));
  }

  /**
   * Factory for creating Deferred for any of our Error Responses.
   *
   * <p>Prioritize using {@link #getDeferredErrorResponse} when possible,
   * if really out of the ordinary and unique
   * use this
   *
   * @param errorType enum ErrorResponse to put in ResponseEntity
   * @param body      custom body to add instead of the errorType body
   * @return the response entity
   */
  public static DeferredResult<ResponseEntity<String>> getDeferredCustomErrorResponse(
      CustomHttpResponses errorType, String body) {
    var result = new DeferredResult<ResponseEntity<String>>();
    result.setErrorResult(getCustomErrorResponse(errorType, body));
    return result;
  }
}
