package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.AuthenticateTask;
import edu.byu.cs.tweeter.client.model.service.observer.ResultObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public class UserAuthHandler
        extends ResultHandler<Pair<User, AuthToken>, ResultObserver<Pair<User, AuthToken>>> {
    public UserAuthHandler(ResultObserver<Pair<User, AuthToken>> observer) {
        super(observer);
    }

    @Override
    protected Pair<User, AuthToken> getResult(Bundle data) {
        return new Pair<>(
            (User) data.getSerializable(AuthenticateTask.USER_KEY),
            (AuthToken) data.getSerializable(AuthenticateTask.AUTH_TOKEN_KEY)
        );
    }
}
