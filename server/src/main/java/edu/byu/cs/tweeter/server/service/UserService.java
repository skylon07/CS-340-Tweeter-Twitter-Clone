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
import edu.byu.cs.tweeter.server.dao.interfaces.SessionDao;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDao;
import edu.byu.cs.tweeter.server.service.exceptions.BadRequestException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class UserService extends BaseService {
    private final UserDao userDao;

    public UserService(SessionDao sessionDao, UserDao userDao) {
        super(sessionDao);
        this.userDao = userDao;
    }

    public LoginResponse login(LoginRequest request) {
        validateLoginRequest(request);
        if (!userDao.isValidPassword(request.getUsername(), request.getPassword())) throw new BadRequestException("Incorrect password given for user '" + request.getUsername() + "'");

        User user = userDao.getUserForLogin(request.getUsername());
        assert user != null : "User could not be found even though their password was retrieved";

        AuthToken newToken = generateNewAuthTokenFor(request.getUsername());
        return new LoginResponse(user, newToken);
    }

    public Response logout(AuthorizedRequest request) {
        validateAuthorizedRequest(request);

        sessionDao.revokeSession(request.getAuthToken());
        return new Response();
    }

    public LoginResponse register(RegisterRequest request) {
        validateRegisterRequest(request);
        if (userDao.getUser(request.getUsername()) != null) throw new BadRequestException("User alias already taken");

        User newUser = userDao.createUser(request.getFirstName(), request.getLastName(), request.getUsername(), request.getImage());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String userHash = encoder.encode(request.getPassword());
        userDao.saveUserHash(request.getUsername(), userHash);

        AuthToken newToken = generateNewAuthTokenFor(request.getUsername());
        return new LoginResponse(newUser, newToken);
    }

    public UserResponse getUser(UserTargetedRequest request) {
        validateUserTargetedRequest(request);

        return new UserResponse(userDao.getUser(request.getTargetAlias()));
    }

    private AuthToken generateNewAuthTokenFor(String username) {
        byte[] newTokenBytes = new byte[32];
        new Random().nextBytes(newTokenBytes);
        String newTokenString = new String(newTokenBytes, StandardCharsets.UTF_8);
        AuthToken newToken = new AuthToken(newTokenString, System.currentTimeMillis());

        assert username != null : "Cannot create auth token without a username";
        sessionDao.createSession(username, newToken);

        return newToken;
    }
}
