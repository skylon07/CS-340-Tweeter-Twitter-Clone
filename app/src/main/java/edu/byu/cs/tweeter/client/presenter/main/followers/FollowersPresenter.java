package edu.byu.cs.tweeter.client.presenter.main.followers;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.BasePresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public class FollowersPresenter extends BasePresenter<FollowersPresenter.View> {
    private static final int PAGE_SIZE = 10;

    private User lastFollower;
    private boolean hasMorePages;
    private boolean isLoading;

    private final FollowService followService = new FollowService();
    private final UserService userService = new UserService();

    public FollowersPresenter(View view) {
        super(view);
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void onFollowerItemClick(String userAlias) {
        loadUserProfile(userAlias);
    }

    public void onScrolled(User user) {
        loadMoreItems(user);
    }

    public void loadUserProfile(String userAlias) {
        view.displayMessage("Getting user's profile...");
        AuthToken authToken = Cache.getInstance().getCurrUserAuthToken();
        userService.loadUser(authToken, userAlias, new UserServiceObserver());
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooterVisible(true);
            AuthToken authToken = Cache.getInstance().getCurrUserAuthToken();
            followService.loadFollowers(authToken, user, PAGE_SIZE, lastFollower, new FollowServiceObserver());
        }
    }

    public interface View extends BasePresenter.View {
        void addFollowers(List<User> followers);
        void setLoadingFooterVisible(boolean visible);
        void setCurrentUser(User user);
    }

    private class FollowServiceObserver extends ServiceResultObserver<Pair<List<User>, Boolean>> {
        public FollowServiceObserver() {
            super("get follower");
        }

        @Override
        public void onResultLoaded(Pair<List<User>, Boolean> result) {
            List<User> followees = result.getFirst();
            Boolean hasMorePages = result.getSecond();

            isLoading = false;
            view.setLoadingFooterVisible(false);
            FollowersPresenter.this.hasMorePages = hasMorePages;
            lastFollower = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
            view.addFollowers(followees);
        }

        @Override
        public void handleFailure(String message) {
            super.handleFailure(message);
            isLoading = false;
            view.setLoadingFooterVisible(false);
        }

        @Override
        public void handleException(Exception exception) {
            super.handleException(exception);
            isLoading = false;
            view.setLoadingFooterVisible(false);
        }
    }

    private class UserServiceObserver extends ServiceResultObserver<User> {
        public UserServiceObserver() {
            super("get user");
        }

        @Override
        public void onResultLoaded(User user) {
            view.setCurrentUser(user);
        }
    }
}
