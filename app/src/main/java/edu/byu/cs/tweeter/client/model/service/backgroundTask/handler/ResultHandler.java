package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.observer.ResultObserver;

public abstract class ResultHandler<ResultType, ObserverType extends ResultObserver<ResultType>>
        extends BackgroundTaskHandler<ObserverType> {
    public ResultHandler(ObserverType observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(ObserverType observer, Bundle data) {
        ResultType result = getResult(data);
        observer.onResultLoaded(result);
    }

    protected abstract ResultType getResult(Bundle data);
}
