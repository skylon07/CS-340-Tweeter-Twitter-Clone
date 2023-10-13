package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.observer.ResultObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class UserHandler extends ResultHandler<User, ResultObserver<User>> {
    public UserHandler(ResultObserver<User> observer) {
        super(observer);
    }

    @Override
    protected User getResult(Bundle data) {
        return (User) data.getSerializable(GetUserTask.USER_KEY);
    }
}
