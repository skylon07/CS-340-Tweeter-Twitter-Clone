package edu.byu.cs.tweeter.server.dao.interfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.util.Pair;

public interface StatusDao {
    Pair<List<Status>, Boolean> getFeed(String targetAlias, Integer limit, Status lastStatus);
    Pair<List<Status>, Boolean> getStory(String targetAlias, Integer limit, Status lastStatus);
    void postStatusToStoryAndFeeds(String targetAlias, String post, Long timestamp);
}
