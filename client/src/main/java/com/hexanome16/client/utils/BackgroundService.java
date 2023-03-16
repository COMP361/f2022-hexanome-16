package com.hexanome16.client.utils;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * This class allows to run a task in a separate background thread using JavaFX's mechanism.
 */
public class BackgroundService extends Service<Void> {
  private final Runnable toRun;
  private final Runnable onSuccess;
  private final Runnable onFailure;
  private final Runnable onInterrupted;

  /**
   * Constructor.
   *
   * @param toRun the function (Runnable) to be executed in the background
   * @param onSuccess the function (Runnable) to be executed when the execution is successful
   * @param onFailure the function (Runnable) to be executed when the execution fails
   * @param onInterrupted the function (Runnable) to be executed when the execution is interrupted
   */
  public BackgroundService(Runnable toRun, Runnable onSuccess, Runnable onFailure,
                           Runnable onInterrupted) {
    this.toRun = toRun;
    this.onSuccess = onSuccess;
    this.onFailure = onFailure;
    this.onInterrupted = onInterrupted;
  }

  /**
   * Alternative constructor.
   *
   * @param toRun the function (Runnable) to be executed in the background
   * @param onSuccess the function (Runnable) to be executed when the execution is successful
   * @param onFailure the function (Runnable) to be executed when the execution fails
   */
  public BackgroundService(Runnable toRun, Runnable onSuccess, Runnable onFailure) {
    this(toRun, onSuccess, onFailure, () -> {});
  }

  /**
   * Alternative constructor.
   *
   * @param toRun the function (Runnable) to be executed in the background
   * @param onSuccess the function (Runnable) to be executed when the execution is successful
   */
  public BackgroundService(Runnable toRun, Runnable onSuccess) {
    this(toRun, onSuccess, () -> {});
  }

  /**
   * Alternative constructor.
   *
   * @param toRun the function (Runnable) to be executed in the background
   */
  public BackgroundService(Runnable toRun) {
    this(toRun, () -> {}, () -> {});
  }

  @Override
  protected Task<Void> createTask() {
    Task<Void> task = new Task<>() {
      @Override
      protected Void call() {
        toRun.run();
        return null;
      }
    };
    task.setOnSucceeded(event -> onSuccess.run());
    task.setOnFailed(event -> {
      System.out.println(event.toString());
      task.getException().printStackTrace();
      if (task.getException() instanceof InterruptedException) {
        onInterrupted.run();
      } else {
        onFailure.run();
      }
    });
    task.setOnCancelled(event -> onInterrupted.run());
    return task;
  }
}
