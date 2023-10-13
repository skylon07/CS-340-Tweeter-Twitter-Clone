package edu.byu.cs.tweeter.client.presenter.login;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.BasePresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public class RegisterPresenter extends BasePresenter<RegisterPresenter.View> {
    private UserService userService = new UserService();

    public RegisterPresenter(View view) {
        super(view);
    }

    public void onRegisterClick(String firstName, String lastName, String userAlias, String password, Drawable imageToUpload) {
        if (validateRegistration(firstName, lastName, userAlias, password, imageToUpload)) {
            view.setErrorText(null);
            view.displayMessage("Registering...");

            Bitmap image = ((BitmapDrawable) imageToUpload).getBitmap();
            userService.registerUser(firstName, lastName, userAlias, password, image, new UserServiceObserver());
        }
    }

    public void onImageUploadClick() {
        view.openMediaGallery();
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

    public interface View extends BasePresenter.View {
        void setCurrentUser(User user);
        void openMediaGallery();
        void setErrorText(String message);
        void cancelMessage();
    }

    private class UserServiceObserver extends ServiceResultObserver<Pair<User, AuthToken>> {
        public UserServiceObserver() {
            super("register");
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
