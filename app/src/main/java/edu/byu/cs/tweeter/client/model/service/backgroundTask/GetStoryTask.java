package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.PagedRequestByLong;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedStatusTask {

    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastStatus, Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

    @Override
    protected PagedResponse<Status> callApiForPage() throws IOException, TweeterRemoteException {
        Long lastTimestamp = null;
        if (getLastItem() != null) {
            lastTimestamp = getLastItem().getTimestamp();
        }
        PagedRequestByLong request = new PagedRequestByLong(getAuthToken(), getTargetUser().getAlias(), getLimit(), lastTimestamp);
        return getServerFacade().getStory(request);
    }
}
