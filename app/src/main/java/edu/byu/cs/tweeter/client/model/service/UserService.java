package edu.byu.cs.tweeter.client.model.service;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SuccessHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.UserHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.UserAuthHandler;
import edu.byu.cs.tweeter.client.model.service.observer.ResultObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SuccessObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public class UserService {
    public void loadUser(AuthToken authToken, String userAlias, ResultObserver<User> observer) {
        GetUserTask getUserTask = new GetUserTask(
            authToken,
            userAlias,
            new UserHandler(observer)
        );
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }

    public void loginUser(String userAlias, String password, ResultObserver<Pair<User, AuthToken>> observer) {
        LoginTask loginTask = new LoginTask(
            userAlias,
            password,
            new UserAuthHandler(observer)
        );
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(loginTask);
    }

    public void registerUser(String firstName, String lastName, String userAlias, String password, Bitmap imageToUpload, ResultObserver<Pair<User, AuthToken>> observer) {
        // Convert image to byte array.
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        imageToUpload.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imageBytes = bos.toByteArray();
        // Intentionally, Use the java Base64 encoder so it is compatible with M4.
        String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);

        RegisterTask registerTask = new RegisterTask(
            firstName,
            lastName,
            userAlias,
            password,
            imageBytesBase64,
            new UserAuthHandler(observer)
        );

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(registerTask);
    }

    public void logoutUser(AuthToken authToken, SuccessObserver observer) {
        LogoutTask logoutTask = new LogoutTask(
            authToken,
            new SuccessHandler(observer)
        );
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(logoutTask);
    }
}
