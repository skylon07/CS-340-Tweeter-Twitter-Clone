package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.AuthorizedRequest;
import edu.byu.cs.tweeter.model.net.response.Response;

/**
 * Background task that logs out a user (i.e., ends a session).
 */
public class LogoutTask extends AuthenticatedTask {

    public LogoutTask(AuthToken authToken, Handler messageHandler) {
        super(authToken, messageHandler);
    }

    @Override
    protected Response callApi() throws IOException, TweeterRemoteException {
        AuthorizedRequest request = new AuthorizedRequest(getAuthToken());
        return getServerFacade().logout(request);
    }
}
