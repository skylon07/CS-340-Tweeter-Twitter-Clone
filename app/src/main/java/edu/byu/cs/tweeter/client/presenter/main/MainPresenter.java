package edu.byu.cs.tweeter.client.presenter.main;

import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {
    private View view;
    private StatusService statusService = new StatusService();
    private FollowService followService = new FollowService();
    private UserService userService = new UserService();

    public MainPresenter(View view) {
        this.view = view;
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
        statusService.createStatus(authToken, user, post, new StatusServiceObserver());
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

    public interface View {
        void completeLogout();
        void showStatusDialog();
        void setFollowerCount(int count);
        void setFollowingCount(int count);
        void setFollowingSelectedUser(boolean isFollowing);
        void setFollowButtonEnabled(boolean enabled);
        void displayMessage(String message);
        void cancelMessage();
    }

    private class UserServiceLogoutObserver implements UserService.LogoutObserver {
        @Override
        public void onUserLoggedOut() {
            Cache.getInstance().clearCache();
            view.completeLogout();
        }

        @Override
        public void displayError(String message) {
            view.displayMessage("Failed to logout: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to logout because of exception: " + ex.getMessage());
        }
    }

    private class StatusServiceObserver implements StatusService.CreateObserver {
        @Override
        public void onStatusCreated() {
            view.cancelMessage();
            view.displayMessage("Successfully Posted!");
        }

        @Override
        public void displayError(String message) {
            view.cancelMessage();
            view.displayMessage("Failed to post status: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.cancelMessage();
            view.displayMessage("Failed to post status because of exception: " + ex.getMessage());
        }
    }

    private class FollowersCountObserver implements FollowService.LoadCountObserver {
        @Override
        public void onCountLoaded(int followersCount) {
            view.setFollowerCount(followersCount);
        }

        @Override
        public void displayError(String message) {
            view.displayMessage("Failed to get followers count: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to get followers count because of exception: " + ex.getMessage());
        }
    }

    private class FollowingCountObserver implements FollowService.LoadCountObserver {
        @Override
        public void onCountLoaded(int followingCount) {
            view.setFollowingCount(followingCount);
        }

        @Override
        public void displayError(String message) {
            view.displayMessage("Failed to get following count: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to get following count because of exception: " + ex.getMessage());
        }
    }

    private class IsFollowerObserver implements FollowService.TruthObserver {
        @Override
        public void onLoaded(boolean isFollower) {
            view.setFollowingSelectedUser(isFollower);
        }

        @Override
        public void displayError(String message) {
            view.displayMessage("Failed to determine following relationship: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to determine following relationship because of exception: " + ex.getMessage());
        }
    }

    private class FollowRequestObserver implements FollowService.TruthObserver {
        @Override
        public void onLoaded(boolean loaded) {
            updateFollowingAndFollowers();
            view.setFollowingSelectedUser(true);
            view.setFollowButtonEnabled(true);
        }

        @Override
        public void displayError(String message) {
            view.displayMessage("Failed to follow: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to follow because of exception: " + ex.getMessage());
        }
    }

    private class UnfollowRequestObserver implements FollowService.TruthObserver {
        @Override
        public void onLoaded(boolean loaded) {
            updateFollowingAndFollowers();
            view.setFollowingSelectedUser(false);
            view.setFollowButtonEnabled(true);
        }

        @Override
        public void displayError(String message) {
            view.displayMessage("Failed to unfollow: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to unfollow because of exception: " + ex.getMessage());
        }
    }
}
