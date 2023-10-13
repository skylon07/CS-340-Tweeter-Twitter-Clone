package edu.byu.cs.tweeter.client.presenter.main.story;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.BasePresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public class StoryPresenter extends BasePresenter {
    private static final int PAGE_SIZE = 10;

    private Status lastStatus;
    private boolean hasMorePages;
    private boolean isLoading;

    private final View view;
    private final StatusService statusService = new StatusService();
    private final UserService userService = new UserService();

    public StoryPresenter(View view) {
        super(view);
        this.view = view;
    }

    public boolean isLoading() {
            return isLoading;
        }

    public boolean hasMorePages() {
            return hasMorePages;
}

    public void onStoryItemClick(String userAlias) {
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
            statusService.loadStory(authToken, user, PAGE_SIZE, lastStatus, new StatusServiceObserver());
        }
    }

    public interface View extends BasePresenter.View {
        void addStatuses(List<Status> statuses);
        void setLoadingFooterVisible(boolean visible);
        void setCurrentUser(User user);
    }

    private class StatusServiceObserver extends ServiceResultObserver<Pair<List<Status>, Boolean>> {
        public StatusServiceObserver() {
            super("get story");
        }

        @Override
        public void onResultLoaded(Pair<List<Status>, Boolean> result) {
            List<Status> statuses = result.getFirst();
            Boolean hasMorePages = result.getSecond();

            isLoading = false;
            view.setLoadingFooterVisible(false);
            StoryPresenter.this.hasMorePages = hasMorePages;
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            view.addStatuses(statuses);
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
