package edu.byu.cs.tweeter.client.presenter.login;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.AuthenticatingPresenter;

public class LoginPresenter extends AuthenticatingPresenter<LoginPresenter.View> {
    private UserService userService = new UserService();

    public LoginPresenter(View view) {
        super(view);
    }

    public void onLoginClick(String userAlias, String password) {
        if (validateLogin(userAlias, password)) {
            view.setErrorText(null);
            view.displayMessage("Logging In...");
            userService.loginUser(userAlias, password, new UserServiceAuthenticationObserver("login"));
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

    public interface View extends AuthenticatingPresenter.View {
        void setErrorText(String message);
    }
}
