package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.request.UserTargetedRequest;
import edu.byu.cs.tweeter.model.net.response.Response;

/**
 * Background task that establishes a following relationship between two users.
 */
public class FollowTask extends UserActionTask {
    /**
     * The user that is being followed.
     */
    private final User followee;

    public FollowTask(AuthToken authToken, User currUser, User followee, Handler messageHandler) {
        super(authToken, currUser, messageHandler);
        this.followee = followee;
    }

    @Override
    protected Response callApi() throws IOException, TweeterRemoteException {
        FollowsRequest request = new FollowsRequest(getAuthToken(), getCurrUser().getAlias(), followee.getAlias());
        return getServerFacade().follow(request);
    }

    protected User getFollowee() { return followee; }
}
