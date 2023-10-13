package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BaseService {
    protected void executeTask(Runnable task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    protected void executeTaskPair(Runnable task1, Runnable task2) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(task1);
        executor.execute(task2);
    }
}
