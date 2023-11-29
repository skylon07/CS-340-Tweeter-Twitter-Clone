package edu.byu.cs.tweeter.server.dao.interfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public interface FollowDao {
    Pair<List<User>, Boolean> getFollowees(String followerAlias, int limit, String lastFolloweeAlias);
    Integer getFolloweeCount(String followerAlias);
    Pair<List<User>, Boolean> getFollowers(String followeeAlias, int limit, String lastFollowerAlias);
    Integer getFollowerCount(String followeeAlias);
    boolean isFollowing(String followerAlias, String followeeAlias);
    void recordFollow(User follower, User followee);
    void removeFollow(String followerAlias, String followeeAlias);
}
