package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetCountTask;
import edu.byu.cs.tweeter.client.model.service.observer.ResultObserver;

public class CountHandler
        extends ResultHandler<Integer, ResultObserver<Integer>> {
    public CountHandler(ResultObserver<Integer> observer) {
        super(observer);
    }

    @Override
    protected Integer getResult(Bundle data) {
        return data.getInt(GetCountTask.COUNT_KEY);
    }
}
