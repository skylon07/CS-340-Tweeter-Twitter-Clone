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
        Status newStatus = new Status(post, user, System.currentTimeMillis(), parseURLs(post), parseMentions(post));
        PostStatusTask statusTask = new PostStatusTask(authToken, newStatus, new SuccessHandler(observer));
        executeTask(statusTask);
    }

    private List<String> parseURLs(String post) {
        // TODO: should this be in its own service/utils class?
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    private int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    private List<String> parseMentions(String post) {
        // TODO: should this be in its own service/utils class?
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }
}
