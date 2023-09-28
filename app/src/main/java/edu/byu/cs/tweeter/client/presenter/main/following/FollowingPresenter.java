package edu.byu.cs.tweeter.client.presenter.main.following;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;

public class FollowingPresenter {
    private static final int PAGE_SIZE = 10;

    private User lastFollowee;
    private boolean hasMorePages;
    private boolean isLoading;

    private final FollowService followService = new FollowService();
    private final UserService userService = new UserService();

    private final View view;

    public FollowingPresenter(View view) {
        this.view = view;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void onFollowingViewClick(String userAlias) {
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
        if (!isLoading) {
            isLoading = true;
            view.setLoadingFooterVisible(true);
            AuthToken authToken = Cache.getInstance().getCurrUserAuthToken();
            followService.loadFollowees(authToken, user, PAGE_SIZE, lastFollowee, new FollowServiceObserver());
        }
    }

    public interface View {
        void addFollowees(List<User> followees);
        void setLoadingFooterVisible(boolean visible);
        void setCurrentUser(User user);
        void displayMessage(String message);
    }

    private class FollowServiceObserver implements FollowService.Observer {
        @Override
        public void onItemsLoaded(List<User> followees, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooterVisible(false);
            FollowingPresenter.this.hasMorePages = hasMorePages;
            lastFollowee = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
            view.addFollowees(followees);
        }

        @Override
        public void displayError(String message) {
            isLoading = false;
            view.setLoadingFooterVisible(false);

            view.displayMessage("Failed to get following: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            isLoading = false;
            view.setLoadingFooterVisible(false);

            view.displayMessage("Failed to get following because of exception: " + ex.getMessage());
        }
    }

    private class UserServiceObserver implements UserService.Observer {
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
