package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.Response;

public abstract class GetCountTask extends AuthenticatedTask {
    public static final String COUNT_KEY = "count";

    /**
     * The user whose count is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    private final User targetUser;

    private int count;

    protected GetCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, messageHandler);
        this.targetUser = targetUser;
    }
    protected User getTargetUser() {
        return targetUser;
    }

    @Override
    protected CountResponse callApi() throws IOException, TweeterRemoteException {
        CountResponse response = callCountApi();
        count = response.getCount();
        return response;
    }

    protected abstract CountResponse callCountApi() throws IOException, TweeterRemoteException;

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putInt(COUNT_KEY, count);
    }
}
