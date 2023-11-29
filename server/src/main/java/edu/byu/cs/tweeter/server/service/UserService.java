package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.AuthorizedRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserTargetedRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.interfaces.DaoFactory;
import edu.byu.cs.tweeter.server.service.exceptions.BadRequestException;
import edu.byu.cs.tweeter.server.service.exceptions.UnauthorizedRequestException;

import java.util.Random;

public class UserService extends BaseService {
    private static final String TOKEN_CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";

    public UserService(DaoFactory daoFactory) {
        super(daoFactory);
    }

    public LoginResponse login(LoginRequest request) {
        validateLoginRequest(request);
        if (!getDaos().getUserDao().isValidPassword(request.getUsername(), request.getPassword())) throw new BadRequestException("Incorrect password given for user '" + request.getUsername() + "'");

        User user = getDaos().getUserDao().getUserForLogin(request.getUsername());
        assert user != null : "User could not be found even though their password was retrieved";

        AuthToken newToken = generateNewAuthTokenFor(request.getUsername());
        return new LoginResponse(user, newToken);
    }

    public Response logout(AuthorizedRequest request) {
        try {
            validateAuthorizedRequest(request);

            getDaos().getSessionDao().revokeSession(request.getAuthToken());
        } catch (UnauthorizedRequestException error) {
            // guess their token already got cleaned up!
        }

        return new Response();
    }

    public LoginResponse register(RegisterRequest request) {
        validateRegisterRequest(request);
        if (getDaos().getUserDao().getUser(request.getUsername()) != null) throw new BadRequestException("User alias already taken");

        User newUser = getDaos().getUserDao().createUser(request.getFirstName(), request.getLastName(), request.getUsername(), request.getImage());

        getDaos().getUserDao().savePassword(request.getUsername(), request.getPassword());
        AuthToken newToken = generateNewAuthTokenFor(request.getUsername());

        return new LoginResponse(newUser, newToken);
    }

    public UserResponse getUser(UserTargetedRequest request) {
        validateUserTargetedRequest(request);

        return new UserResponse(getDaos().getUserDao().getUser(request.getTargetAlias()));
    }

    private AuthToken generateNewAuthTokenFor(String username) {
        String newTokenString = "";
        Random rng = new Random();
        for (int charIdx = 0; charIdx < 16; ++charIdx) {
            char currChar = TOKEN_CHARS.charAt(rng.nextInt(TOKEN_CHARS.length()));
            newTokenString += currChar;
        }
        AuthToken newToken = new AuthToken(newTokenString, System.currentTimeMillis());

        assert username != null : "Cannot create auth token without a username";
        getDaos().getSessionDao().createSession(username, newToken);

        return newToken;
    }
}
