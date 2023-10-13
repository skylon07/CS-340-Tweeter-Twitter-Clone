package edu.byu.cs.tweeter.client.model.service.observer;

public interface ResultObserver<ResultType> extends ServiceObserver {
    void onResultLoaded(ResultType result);
}
