package edu.byu.cs.tweeter.client.presenter.main.feed;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter {
    private static final int PAGE_SIZE = 10;

    private Status lastStatus;
    private boolean hasMorePages;
    private boolean isLoading;

    private final View view;
    private final StatusService statusService = new StatusService();
    private final UserService userService = new UserService();

    public FeedPresenter(View view) {
        this.view = view;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void onFeedItemClick(String userAlias) {
        loadUserProfile(userAlias);
    }

    public void onMentionClick(String userAlias) {
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
            statusService.loadFeed(authToken, user, PAGE_SIZE, lastStatus, new StatusServiceObserver());
        }
    }

    public interface View {
        void addStatuses(List<Status> statuses);
        void setLoadingFooterVisible(boolean visible);
        void setCurrentUser(User user);
        void displayMessage(String message);
    }

    private class StatusServiceObserver implements StatusService.LoadItemsObserver {
        @Override
        public void onItemsLoaded(List<Status> statuses, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooterVisible(false);
            FeedPresenter.this.hasMorePages = hasMorePages;
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            view.addStatuses(statuses);
        }

        @Override
        public void displayError(String message) {
            isLoading = false;
            view.setLoadingFooterVisible(false);

            view.displayMessage("Failed to get feed: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            isLoading = false;
            view.setLoadingFooterVisible(false);

            view.displayMessage("Failed to get feed because of exception: " + ex.getMessage());
        }
    }

    private class UserServiceObserver implements UserService.LoadItemsObserver {
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
