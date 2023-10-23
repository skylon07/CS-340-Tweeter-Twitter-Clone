package edu.byu.cs.tweeter.client.presenter.main;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.BasePresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends BasePresenter<MainPresenter.View> {
    private StatusService statusService;
    private FollowService followService = new FollowService();
    private UserService userService = new UserService();

    public MainPresenter(View view, StatusService statusService) {
        super(view);
        this.statusService = statusService;

    }

    public void onFabClick() {
        view.showStatusDialog();
    }

    public void onLogoutOptionSelected() {
        view.displayMessage("Logging Out...");
        logoutUser();
    }

    public void onStatusPosted(String post) {
        view.displayMessage("Posting Status...");
        AuthToken authToken = Cache.getInstance().getCurrUserAuthToken();
        User user = Cache.getInstance().getCurrUser();
        statusService.createStatus(authToken, user, post, createStatusObserver());
    }

    public void onFollowButtonClick(boolean isFollowingButton, User selectedUser) {
        view.setFollowButtonEnabled(false);

        AuthToken authToken = Cache.getInstance().getCurrUserAuthToken();
        User user = Cache.getInstance().getCurrUser();
        if (isFollowingButton) {
            followService.requestUnfollow(authToken, user, new UnfollowRequestObserver());
            view.displayMessage("Unfollowing " + selectedUser.getName());
        } else {
            followService.requestFollow(authToken, user, new FollowRequestObserver());
            view.displayMessage("Following " + selectedUser.getName());
        }
    }

    public void updateFollowingAndFollowers() {
        AuthToken authToken = Cache.getInstance().getCurrUserAuthToken();
        User user = Cache.getInstance().getCurrUser();
        followService.loadFollowCounts(authToken, user, new FollowersCountObserver(), new FollowingCountObserver());
    }

    public boolean currentUserIsLoggedInUser(User selectedUser) {
        return selectedUser.compareTo(Cache.getInstance().getCurrUser()) == 0;
    }

    public void updateCurrentUserIsFollowerOf(User selectedUser) {
        AuthToken authToken = Cache.getInstance().getCurrUserAuthToken();
        User user = Cache.getInstance().getCurrUser();
        followService.doesUserFollow(authToken, user, selectedUser, new IsFollowerObserver());
    }

    private void logoutUser() {
        AuthToken authToken = Cache.getInstance().getCurrUserAuthToken();
        userService.logoutUser(authToken, new UserServiceLogoutObserver());
    }

    public interface View extends BasePresenter.View {
        void completeLogout();
        void showStatusDialog();
        void setFollowerCount(int count);
        void setFollowingCount(int count);
        void setFollowingSelectedUser(boolean isFollowing);
        void setFollowButtonEnabled(boolean enabled);
        void cancelMessage();
    }

    private class UserServiceLogoutObserver extends ServiceSuccessObserver {
        public UserServiceLogoutObserver() {
            super("logout");
        }

        @Override
        public void handleSuccess() {
            Cache.getInstance().clearCache();
            view.completeLogout();
        }
    }

    public class StatusServiceObserver extends ServiceSuccessObserver {
        public StatusServiceObserver() {
            super("post status");
        }

        @Override
        public void handleFailure(String message) {
            super.handleFailure(message);
            view.cancelMessage();
        }

        @Override
        public void handleException(Exception exception) {
            super.handleException(exception);
            view.cancelMessage();
        }

        @Override
        public void handleSuccess() {
            view.cancelMessage();
            view.displayMessage("Successfully Posted!");
        }
    }

    public StatusServiceObserver createStatusObserver() {
        return new StatusServiceObserver();
    }

    private class FollowersCountObserver extends ServiceResultObserver<Integer> {
        public FollowersCountObserver() {
            super("get followers count");
        }

        @Override
        public void onResultLoaded(Integer followersCount) {
            view.setFollowerCount(followersCount);
        }
    }

    private class FollowingCountObserver extends ServiceResultObserver<Integer> {
        public FollowingCountObserver() {
            super("get following count");
        }

        @Override
        public void onResultLoaded(Integer followingCount) {
            view.setFollowingCount(followingCount);
        }
    }

    private class IsFollowerObserver extends ServiceResultObserver<Boolean> {
        public IsFollowerObserver() {
            super("determine following relationship");
        }

        @Override
        public void onResultLoaded(Boolean isFollower) {
            view.setFollowingSelectedUser(isFollower);
        }
    }

    private class FollowRequestObserver extends ServiceSuccessObserver {
        public FollowRequestObserver() {
            super("follow");
        }

        @Override
        public void handleSuccess() {
            updateFollowingAndFollowers();
            view.setFollowingSelectedUser(true);
            view.setFollowButtonEnabled(true);
        }
    }

    private class UnfollowRequestObserver extends ServiceSuccessObserver {
        public UnfollowRequestObserver() {
            super("unfollow");
        }

        @Override
        public void handleSuccess() {
            updateFollowingAndFollowers();
            view.setFollowingSelectedUser(false);
            view.setFollowButtonEnabled(true);
        }
    }
}
