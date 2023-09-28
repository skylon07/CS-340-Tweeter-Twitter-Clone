package edu.byu.cs.tweeter.client.presenter.main.followers;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter {
    private static final int PAGE_SIZE = 10;

    private User lastFollower;
    private boolean hasMorePages;
    private boolean isLoading;

    private final View view;
    private final FollowService followService = new FollowService();
    private final UserService userService = new UserService();

    public FollowersPresenter(View view) {
        this.view = view;
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

    public interface View {
        void addFollowers(List<User> followers);
        void setLoadingFooterVisible(boolean visible);
        void setCurrentUser(User user);
        void displayMessage(String message);
    }

    private class FollowServiceObserver implements FollowService.LoadItemsObserver {
        @Override
        public void onItemsLoaded(List<User> followees, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooterVisible(false);
            FollowersPresenter.this.hasMorePages = hasMorePages;
            lastFollower = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
            view.addFollowers(followees);
        }

        @Override
        public void displayError(String message) {
            isLoading = false;
            view.setLoadingFooterVisible(false);

            view.displayMessage("Failed to get follower: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            isLoading = false;
            view.setLoadingFooterVisible(false);

            view.displayMessage("Failed to get follower because of exception: " + ex.getMessage());
        }
    }

    private class UserServiceObserver implements UserService.LoadObserver {
        @Override
        public void onUserLoaded(User user) {
            view.setCurrentUser(user);
        }

        @Override
        public void displayError(String message) {
            view.displayMessage("Failed to get user: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to get user because of exception: " + ex.getMessage());
        }
    }
}
