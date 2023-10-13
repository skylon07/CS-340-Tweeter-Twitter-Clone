package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.observer.SuccessObserver;

public class SuccessHandler
        extends BackgroundTaskHandler<SuccessObserver> {
    public SuccessHandler(SuccessObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(SuccessObserver observer, Bundle data) {
        observer.handleSuccess();
    }
}
