package edu.byu.cs.tweeter.server.dao.interfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public interface FollowDao {
    Pair<List<User>, Boolean> getFollowees(String followerAlias, int limit, User lastFollowee);
    Integer getFolloweeCount(String followerAlias);
    Pair<List<User>, Boolean> getFollowers(String followeeAlias, int limit, User lastFollower);
    Integer getFollowerCount(String followeeAlias);
    boolean isFollowing(String followerAlias, String followeeAlias);
    void recordFollow(String followerAlias, String followeeAlias);
    void removeFollow(String followerAlias, String followeeAlias);
}
