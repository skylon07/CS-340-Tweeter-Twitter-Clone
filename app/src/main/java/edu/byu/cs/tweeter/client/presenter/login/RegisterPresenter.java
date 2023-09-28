package edu.byu.cs.tweeter.client.presenter.login;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter {
    private View view;
    private UserService userService = new UserService();

    public RegisterPresenter(View view) {
        this.view = view;
    }

    public void onRegisterClick(String firstName, String lastName, String userAlias, String password, Drawable imageToUpload) {
        if (validateRegistration(firstName, lastName, userAlias, password, imageToUpload)) {
            view.setErrorText(null);
            view.displayMessage("Registering...");

            Bitmap image = ((BitmapDrawable) imageToUpload).getBitmap();
            userService.registerUser(firstName, lastName, userAlias, password, image, new UserServiceObserver());
        }
    }

    private boolean validateRegistration(String firstName, String lastName, String userAlias, String password, Drawable imageToUpload) {
        if (firstName.length() == 0) {
            view.setErrorText("First Name cannot be empty.");
            return false;
        }
        if (lastName.length() == 0) {
            view.setErrorText("Last Name cannot be empty.");
            return false;
        }
        if (userAlias.length() == 0) {
            view.setErrorText("Alias cannot be empty.");
            return false;
        }
        if (userAlias.charAt(0) != '@') {
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
        if (imageToUpload == null) {
            view.setErrorText("Profile image must be uploaded.");
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

    private class UserServiceObserver implements UserService.LoginObserver {
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
            view.displayMessage("Failed to register: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to register because of exception: " + ex.getMessage());
        }
    }
}
