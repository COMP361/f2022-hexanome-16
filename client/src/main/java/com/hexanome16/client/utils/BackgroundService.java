package com.hexanome16.client.utils;

/**
 * This class allows to run a task in a separate background thread using JavaFX's mechanism.
 */
public class BackgroundService extends Thread {
  private final Runnable toRun;
  private final Runnable onSuccess;
  private final Runnable onFailure;

  /**
   * Constructor.
   *
   * @param toRun     the function (Runnable) to be executed in the background
   * @param onSuccess the function (Runnable) to be executed when the execution is successful
   * @param onFailure the function (Runnable) to be executed when the execution fails
   */
  public BackgroundService(Runnable toRun, Runnable onSuccess, Runnable onFailure) {
    this.toRun = toRun;
    this.onSuccess = onSuccess;
    this.onFailure = onFailure;
    this.setDaemon(true);
    this.setUncaughtExceptionHandler((t, e) -> e.printStackTrace());
  }

  /**
   * Alternative constructor.
   *
   * @param toRun     the function (Runnable) to be executed in the background
   * @param onSuccess the function (Runnable) to be executed when the execution is successful
   */
  public BackgroundService(Runnable toRun, Runnable onSuccess) {
    this(toRun, onSuccess, () -> {
    });
  }

  /**
   * Alternative constructor.
   *
   * @param toRun the function (Runnable) to be executed in the background
   */
  public BackgroundService(Runnable toRun) {
    this(toRun, () -> {
    }, () -> {
    });
  }

  @Override
  public void run() {
    try {
      toRun.run();
      onSuccess.run();
    } catch (Exception e) {
      e.printStackTrace();
      onFailure.run();
    }
  }
}
