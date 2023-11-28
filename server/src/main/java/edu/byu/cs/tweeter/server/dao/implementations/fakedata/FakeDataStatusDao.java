package edu.byu.cs.tweeter.server.dao.implementations.fakedata;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDao;
import edu.byu.cs.tweeter.util.Pair;

public class FakeDataStatusDao extends FakeDataDao implements StatusDao {
    @Override
    public Pair<List<Status>, Boolean> getFeed(String targetAlias, Integer limit, Status lastStatus) {
        Pair<List<Status>, Boolean> pageData = getFakeData().getPageOfStatus(lastStatus, limit);
        return pageData;
    }

    @Override
    public Pair<List<Status>, Boolean> getStory(String targetAlias, Integer limit, Status lastStatus) {
        return getFeed(targetAlias, limit, lastStatus);
    }

    @Override
    public void postStatusToStoryAndFeeds(String targetAlias, String post, Long timestamp) {
        return; // intentionally left blank (nothing to post to)
    }
}
