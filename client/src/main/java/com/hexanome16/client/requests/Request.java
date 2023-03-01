package com.hexanome16.client.requests;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * This class contains information about a request.
 */
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class Request<T> {
  private final RequestMethod method;
  private final RequestDest dest;
  private final String path;
  private final Class<T> responseClass;
  private Map<String, Object> queryParams;
  private Map<String, String> headers;
  @Setter
  private Object body;
  @Setter
  private int status;

  /**
   * Constructor with query params.
   *
   * @param method        The method of the request.
   * @param dest          The destination of the request.
   * @param path          The path of the request.
   * @param queryParams   The query params of the request.
   * @param responseClass The class of the response.
   */
  public Request(RequestMethod method, RequestDest dest, String path,
                 Map<String, Object> queryParams,
                 Class<T> responseClass) {
    this.method = method;
    this.dest = dest;
    this.path = path;
    this.queryParams = queryParams;
    this.responseClass = responseClass;
  }

  /**
   * Constructor with query params and headers.
   *
   * @param method        The method of the request.
   * @param dest          The destination of the request.
   * @param path          The path of the request.
   * @param queryParams   The query params of the request.
   * @param headers       The headers of the request.
   * @param responseClass The class of the response.
   */
  public Request(RequestMethod method, RequestDest dest, String path,
                 Map<String, Object> queryParams,
                 Map<String, String> headers,
                 Class<T> responseClass) {
    this.method = method;
    this.dest = dest;
    this.path = path;
    this.queryParams = queryParams;
    this.headers = headers;
    this.responseClass = responseClass;
  }

  /**
   * Constructor with query params and body.
   *
   * @param method        The method of the request.
   * @param dest          The destination of the request.
   * @param path          The path of the request.
   * @param queryParams   The query params of the request.
   * @param body          The body of the request.
   * @param responseClass The class of the response.
   */
  public Request(RequestMethod method, RequestDest dest, String path,
                 Map<String, Object> queryParams,
                 Object body,
                 Class<T> responseClass) {
    this.method = method;
    this.dest = dest;
    this.path = path;
    this.queryParams = queryParams;
    this.body = body;
    this.responseClass = responseClass;
  }

  /**
   * Constructor with query params, headers and body.
   *
   * @param method        The method of the request.
   * @param dest          The destination of the request.
   * @param path          The path of the request.
   * @param queryParams   The query params of the request.
   * @param headers       The headers of the request.
   * @param body          The body of the request.
   * @param responseClass The class of the response.
   */
  public Request(RequestMethod method, RequestDest dest, String path,
                 Map<String, Object> queryParams,
                 Map<String, String> headers,
                 Object body,
                 Class<T> responseClass) {
    this.method = method;
    this.dest = dest;
    this.path = path;
    this.queryParams = queryParams;
    this.headers = headers;
    this.body = body;
    this.responseClass = responseClass;
  }
}
