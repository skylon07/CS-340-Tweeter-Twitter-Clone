package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;
import java.util.Random;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.request.UserTargetedRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowingResponse;
import edu.byu.cs.tweeter.model.net.response.Response;

/**
 * Background task that determines if one user is following another.
 */
public class IsFollowerTask extends UserActionTask {

    public static final String IS_FOLLOWER_KEY = "is-follower";

    /**
     * The alleged followee.
     */
    private final User followee;

    private boolean isFollower;

    public IsFollowerTask(AuthToken authToken, User currUser, User followee, Handler messageHandler) {
        super(authToken, currUser, messageHandler);
        this.followee = followee;
    }

    @Override
    protected IsFollowingResponse callApi() throws IOException, TweeterRemoteException {
        FollowsRequest request = new FollowsRequest(getAuthToken(), getCurrUser().getAlias(), followee.getAlias());
        IsFollowingResponse response = getServerFacade().isFollowing(request);
        isFollower = response.getIsFollowing();
        return response;
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putBoolean(IS_FOLLOWER_KEY, isFollower);
    }
}
