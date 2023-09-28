package edu.byu.cs.tweeter.client.presenter.login;

import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.view.login.LoginFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter {
    private View view;
    private UserService userService = new UserService();

    public LoginPresenter(View view) {
        this.view = view;
    }

    public void onLoginClick(String userAlias, String password) {
        if (validateLogin(userAlias, password)) {
            view.setErrorText(null);
            view.displayMessage("Logging In...");
            userService.loginUser(userAlias, password, new UserServiceLoginObserver());
        }
    }

    private boolean validateLogin(String userAlias, String password) {
        if (userAlias.length() > 0 && userAlias.charAt(0) != '@') {
            view.setErrorText("Alias must begin with @.");
            return false;
        }
        if (userAlias.length() < 2) {
            view.setErrorText("Alias must contain 1 or more characters after the @.");
            return false;
        }
        if (password.length() == 0) {
            view.setErrorText("Password cannot be empty.");
            return false;
        }
        return true;
    }

    public interface View {
        void setCurrentUser(User user);
        void setErrorText(String message);
        void displayMessage(String message);
        void cancelMessage();
    }

    private class UserServiceLoginObserver implements UserService.LoginObserver {
        @Override
        public void onUserLoggedIn(User user, AuthToken authToken) {
            Cache.getInstance().setCurrUser(user);
            Cache.getInstance().setCurrUserAuthToken(authToken);
            view.setCurrentUser(user);
            view.cancelMessage();
            view.displayMessage("Hello " + user.getName());
        }

        @Override
        public void displayError(String message) {
            view.displayMessage("Failed to login: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to login because of exception: " + ex.getMessage());
        }
    }
}
