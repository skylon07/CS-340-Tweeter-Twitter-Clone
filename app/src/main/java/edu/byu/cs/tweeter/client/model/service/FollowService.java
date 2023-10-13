package edu.byu.cs.tweeter.client.model.service;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.CountHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SuccessHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.UserPageHandler;
import edu.byu.cs.tweeter.client.model.service.observer.ResultObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SuccessObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public class FollowService extends BaseService {
    public void loadFollowees(AuthToken authToken, User user, int pageSize, User lastFollowee, ResultObserver<Pair<List<User>, Boolean>> observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(
            authToken,
            user,
            pageSize,
            lastFollowee,
            new UserPageHandler(observer)
        );
        executeTask(getFollowingTask);
    }

    public void loadFollowers(AuthToken authToken, User user, int pageSize, User lastFollower, ResultObserver<Pair<List<User>, Boolean>> observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(
            authToken,
            user,
            pageSize,
            lastFollower,
            new UserPageHandler(observer)
        );
        executeTask(getFollowersTask);
    }

    public void loadFollowCounts(AuthToken authToken, User user, ResultObserver<Integer> followersObserver, ResultObserver<Integer> followingObserver) {
        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(
            authToken,
            user,
            new CountHandler(followersObserver)
        );

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(
            authToken,
            user,
            new CountHandler(followingObserver)
        );

        executeTaskPair(followersCountTask, followingCountTask);
    }

    public void doesUserFollow(AuthToken authToken, User userFollowing, User userFollowed, ResultObserver<Boolean> observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken, userFollowing, userFollowed, new IsFollowerHandler(observer));
        executeTask(isFollowerTask);
    }

    public void requestFollow(AuthToken authToken, User user, SuccessObserver observer) {
        FollowTask followTask = new FollowTask(authToken, user, new SuccessHandler(observer));
        executeTask(followTask);
    }

    public void requestUnfollow(AuthToken authToken, User user, SuccessObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(authToken, user, new SuccessHandler(observer));
        executeTask(unfollowTask);
    }
}
