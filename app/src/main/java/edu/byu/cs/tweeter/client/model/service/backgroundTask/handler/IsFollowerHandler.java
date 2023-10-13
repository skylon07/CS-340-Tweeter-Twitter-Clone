package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.observer.ResultObserver;

public class IsFollowerHandler
        extends ResultHandler<Boolean, ResultObserver<Boolean>> {
    public IsFollowerHandler(ResultObserver<Boolean> observer) {
        super(observer);
    }

    @Override
    protected Boolean getResult(Bundle data) {
        return data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
    }
}
