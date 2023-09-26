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

    public void onFollowingViewClick(String userName) {
        loadUserProfile(userName);
    }

    public void loadUserProfile(String userName) {
        view.displayUserLoading();
        AuthToken authToken = Cache.getInstance().getCurrUserAuthToken();
        userService.loadUser(authToken, userName, new UserServiceObserver());
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {
            isLoading = true;
            view.setLoadingFooterVisible(true);
            AuthToken authToken = Cache.getInstance().getCurrUserAuthToken();
            followService.loadMoreItems(authToken, user, PAGE_SIZE, lastFollowee, new FollowServiceObserver());
        }
    }

    public interface View {
        void displayUserLoading();
        void setCurrentUser(User user);
        void addFollowees(List<User> followees);
        void setLoadingFooterVisible(boolean visible);
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
