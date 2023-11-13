package edu.byu.cs.tweeter.server.service;

import java.util.List;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.AuthorizedRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserTargetedRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class UserService extends BaseService {
    public LoginResponse login(LoginRequest request) {
        validateLoginRequest(request);

        // TODO: Generates dummy data. Replace with a real implementation.
        User user = getFakeData().getFirstUser();
        AuthToken authToken = getFakeData().getAuthToken();
        return new LoginResponse(user, authToken);
    }

    public Response logout(AuthorizedRequest request) {
        validateAuthorizedRequest(request);

        // TODO: actually log out
        return new Response();
    }

    public LoginResponse register(RegisterRequest request) {
        validateRegisterRequest(request);

        // TODO: Generates dummy data. Replace with a real implementation.
        return new LoginResponse(getFakeData().getFirstUser(), getFakeData().getAuthToken());
    }

    public UserResponse getUser(UserTargetedRequest request) {
        validateUserTargetedRequest(request);

        // TODO: Generates dummy data. Replace with a real implementation.
        List<User> users = getFakeData().getFakeUsers();
        int userIdx =
            users
                .stream()
                .map(User::getAlias)
                .collect(Collectors.toList())
                .indexOf(request.getTargetAlias());
        return new UserResponse(users.get(userIdx));
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy users and auth tokens.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
