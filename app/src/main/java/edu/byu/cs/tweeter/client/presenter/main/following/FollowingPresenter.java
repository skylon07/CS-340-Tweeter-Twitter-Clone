package edu.byu.cs.tweeter.client.presenter.main.following;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenter.BasePresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.util.Pair;

public class FollowingPresenter extends BasePresenter {
    private static final int PAGE_SIZE = 10;

    private User lastFollowee;
    private boolean hasMorePages;
    private boolean isLoading;

    private final FollowService followService = new FollowService();
    private final UserService userService = new UserService();

    private final View view;

    public FollowingPresenter(View view) {
        super(view);
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

    public interface View extends BasePresenter.View {
        void addFollowees(List<User> followees);
        void setLoadingFooterVisible(boolean visible);
        void setCurrentUser(User user);
    }

    private class FollowServiceObserver extends ServiceResultObserver<Pair<List<User>, Boolean>> {
        public FollowServiceObserver() {
            super("get following");
        }

        @Override
        public void onResultLoaded(Pair<List<User>, Boolean> result) {
            List<User> followees = result.getFirst();
            Boolean hasMorePages = result.getSecond();

            isLoading = false;
            view.setLoadingFooterVisible(false);
            FollowingPresenter.this.hasMorePages = hasMorePages;
            lastFollowee = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
            view.addFollowees(followees);
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
