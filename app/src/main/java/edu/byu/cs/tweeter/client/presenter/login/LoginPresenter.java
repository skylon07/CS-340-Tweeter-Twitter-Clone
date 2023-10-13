package edu.byu.cs.tweeter.client.presenter.login;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.BasePresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public class LoginPresenter extends BasePresenter<LoginPresenter.View> {
    private UserService userService = new UserService();

    public LoginPresenter(View view) {
        super(view);
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

    public interface View extends BasePresenter.View {
        void setCurrentUser(User user);
        void setErrorText(String message);
        void displayMessage(String message);
        void cancelMessage();
    }

    private class UserServiceLoginObserver extends ServiceResultObserver<Pair<User, AuthToken>> {
        public UserServiceLoginObserver() {
            super("login");
        }

        @Override
        public void onResultLoaded(Pair<User, AuthToken> result) {
            User user = result.getFirst();
            AuthToken authToken = result.getSecond();

            Cache.getInstance().setCurrUser(user);
            Cache.getInstance().setCurrUserAuthToken(authToken);
            view.setCurrentUser(user);
            view.cancelMessage();
            view.displayMessage("Hello " + user.getName());
        }
    }
}
