package com.emergencyblood.util;

import javafx.concurrent.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Utility for running asynchronous database operations off the JavaFX Application Thread.
 * Uses daemon threads so the application can exit cleanly.
 */
public class DatabaseExecutor {

    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool(new ThreadFactory() {
        private int count = 1;
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "db-worker-" + (count++));
            t.setDaemon(true);
            return t;
        }
    });

    /**
     * Executes a runnable on a background worker thread.
     */
    public static void execute(Runnable runnable) {
        EXECUTOR.execute(runnable);
    }

    /**
     * Executes a JavaFX Task on a background worker thread.
     * Task callbacks (onSucceeded, onFailed) will run automatically on the JavaFX Application Thread.
     */
    public static <T> void executeTask(Task<T> task) {
        EXECUTOR.execute(task);
    }

    /**
     * Shuts down the executor service.
     */
    public static void shutdown() {
        EXECUTOR.shutdown();
    }
}
