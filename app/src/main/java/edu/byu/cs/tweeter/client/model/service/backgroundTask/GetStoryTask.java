package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedStatusTask {

    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastStatus, Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

    @Override
    protected PagedResponse<Status> callApiForPage() throws IOException, TweeterRemoteException {
        PagedRequest request = new PagedRequest(getAuthToken(), getTargetUser().getAlias(), getLimit(), getLastItem().asId());
        return getServerFacade().getStory(request);
    }
}
