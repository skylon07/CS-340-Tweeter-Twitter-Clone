package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.PagedRequestByString;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedUserTask {
    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower, Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollower, messageHandler);
    }

    @Override
    protected PagedResponse<User> callApiForPage() throws IOException, TweeterRemoteException {
        String lastAlias = null;
        if (getLastItem() != null) {
            lastAlias = getLastItem().getAlias();
        }
        PagedRequestByString request = new PagedRequestByString(getAuthToken(), getTargetUser().getAlias(), getLimit(), lastAlias);
        return getServerFacade().getFollowers(request);
    }
}
