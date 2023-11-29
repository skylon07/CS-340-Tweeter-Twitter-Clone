package edu.byu.cs.tweeter.client.model.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.StatusPageHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SuccessHandler;
import edu.byu.cs.tweeter.client.model.service.observer.ResultObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SuccessObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService extends BaseService {
    public void loadFeed(AuthToken authToken, User user, int pageSize, Status lastStatus, ResultObserver<Pair<List<Status>, Boolean>> observer) {
        GetFeedTask getFeedTask = new GetFeedTask(
            authToken,
            user,
            pageSize,
            lastStatus,
            new StatusPageHandler(observer)
        );
        executeTask(getFeedTask);
    }

    public void loadStory(AuthToken authToken, User user, int pageSize, Status lastStatus, ResultObserver<Pair<List<Status>, Boolean>> observer) {
        GetStoryTask getStoryTask = new GetStoryTask(
            authToken,
            user,
            pageSize,
            lastStatus,
            new StatusPageHandler(observer)
        );
        executeTask(getStoryTask);
    }

    public void createStatus(AuthToken authToken, User user, String post, SuccessObserver observer) {
        Status newStatus = new Status(post, user, System.currentTimeMillis(), Status.parseURLs(post), Status.parseMentions(post));
        PostStatusTask statusTask = new PostStatusTask(authToken, newStatus, new SuccessHandler(observer));
        executeTask(statusTask);
    }
}
