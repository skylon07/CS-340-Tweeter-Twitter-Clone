package edu.byu.cs.tweeter.server.dao.interfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public interface StatusDao {
    Pair<List<Status>, Boolean> getFeed(String targetAlias, Integer limit, Long lastStatusTimestamp);
    Pair<List<Status>, Boolean> getStory(String targetAlias, Integer limit, Long lastStatusTimestamp);
    void postStatusToStory(User poster, String post, Long timestamp);
    void postStatusToFeeds(List<String> followerAliases, User poster, String post, Long timestamp);
}
