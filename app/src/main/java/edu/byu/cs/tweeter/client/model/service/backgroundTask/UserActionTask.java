package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class UserActionTask extends AuthenticatedTask {
    private final User currUser;

    protected UserActionTask(AuthToken authToken, User currUser, Handler messageHandler) {
        super(authToken, messageHandler);
        this.currUser = currUser;
    }

    protected User getCurrUser() { return currUser; }
}
