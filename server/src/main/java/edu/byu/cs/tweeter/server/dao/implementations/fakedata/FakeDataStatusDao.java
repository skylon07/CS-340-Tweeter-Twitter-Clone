package edu.byu.cs.tweeter.server.dao.implementations.fakedata;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDao;
import edu.byu.cs.tweeter.util.Pair;

public class FakeDataStatusDao extends FakeDataDao implements StatusDao {
    @Override
    public Pair<List<Status>, Boolean> getFeed(String targetAlias, Integer limit, Long lastStatusTimestamp) {
        // TODO: don't loop statuses by repeatedly returning first page
        Pair<List<Status>, Boolean> pageData = getFakeData().getPageOfStatus(null, limit);
        return pageData;
    }

    @Override
    public Pair<List<Status>, Boolean> getStory(String targetAlias, Integer limit, Long lastStatusTimestamp) {
        return getFeed(targetAlias, limit, lastStatusTimestamp);
    }

    @Override
    public void postStatusToStoryAndFeeds(User poster, List<String> followerAliases, String post, Long timestamp) {
        return; // intentionally left blank (nothing to post to)
    }
}
