package edu.byu.cs.tweeter.client.model.net;

import java.io.IOException;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.AuthorizedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.StatusRequest;
import edu.byu.cs.tweeter.model.net.request.UserTargetedRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.model.net.response.UsersResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowingResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.Response;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {

    public static final String SERVER_URL = "https://vj2ldrkjii.execute-api.us-west-1.amazonaws.com/production";

    private final ClientCommunicator clientCommunicator;

    public ServerFacade() {
        clientCommunicator = new ClientCommunicator(SERVER_URL);
    }

    public ServerFacade(ClientCommunicator clientCommunicator) {
        this.clientCommunicator = clientCommunicator;
    }

    public LoginResponse register(RegisterRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/register"; // ideally would be "/users";
        return clientCommunicator.doPost(urlPath, request, null, LoginResponse.class);
    }

    /**
     * Performs a login and if successful, returns the logged in user and an auth token.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    public LoginResponse login(LoginRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/login"; // ideally would be "/sessions";
        return clientCommunicator.doPost(urlPath, request, null, LoginResponse.class);
    }

    public Response logout(AuthorizedRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/logout"; // ideally would be "/sessions/" + request.getAuthToken().getToken();
        return clientCommunicator.doPost(urlPath, request, null, Response.class); // SHOULD BE DELETE!!!
    }

    public UserResponse getUser(UserTargetedRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/getuser"; // ideally would be "/users/" + sanitizeAlias(request.getTargetAlias());
        return clientCommunicator.doPost(urlPath, request, null, UserResponse.class); // SHOULD BE GET!!!
    }

    public UsersResponse getFollowers(PagedRequest<User> request) throws IOException, TweeterRemoteException {
        String urlPath = "/getfollowers"; // ideally would be "/users/" + sanitizeAlias(request.getTargetAlias()) + "/followers/users";
        return clientCommunicator.doPost(urlPath, request, null, UsersResponse.class); // SHOULD BE GET!!!
    }

    public CountResponse countFollowers(UserTargetedRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/countfollowers"; // ideally would be "/users/" + sanitizeAlias(request.getTargetAlias()) + "/followers/count";
        return clientCommunicator.doPost(urlPath, request, null, CountResponse.class); // SHOULD BE GET!!!
    }

    public UsersResponse getFollowing(PagedRequest<User> request) throws IOException, TweeterRemoteException {
        String urlPath = "/getfollowing"; // ideally would be "/users/" + sanitizeAlias(request.getTargetAlias()) + "/following/users";
        return clientCommunicator.doPost(urlPath, request, null, UsersResponse.class); // SHOULD BE GET!!!
    }

    public CountResponse countFollowing(UserTargetedRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/countfollowing"; // ideally would be "/users/" + sanitizeAlias(request.getTargetAlias()) + "/following/count";
        return clientCommunicator.doPost(urlPath, request, null, CountResponse.class); // SHOULD BE GET!!!
    }

    public IsFollowingResponse isFollowing(FollowsRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/isfollowing"; // ideally would be "/users/" + sanitizeAlias(request.getFollowerAlias()) + "/following/users/" + sanitizeAlias(request.getFolloweeAlias());
        return clientCommunicator.doPost(urlPath, request, null, IsFollowingResponse.class); // SHOULD BE GET!!!
    }

    public Response follow(FollowsRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/follow"; // ideally would be "/users/" + sanitizeAlias(request.getFollowerAlias()) + "/following/users/" + sanitizeAlias(request.getFolloweeAlias());
        return clientCommunicator.doPost(urlPath, request, null, Response.class);
    }

    public Response unfollow(FollowsRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/unfollow"; // ideally would be "/users/" + sanitizeAlias(request.getFollowerAlias()) + "/following/users/" + sanitizeAlias(request.getFolloweeAlias());
        return clientCommunicator.doPost(urlPath, request, null, Response.class); // SHOULD BE DELETE!!!
    }

    public Response postStatus(StatusRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/poststatus"; // ideally would be "/users/" + sanitizeAlias(request.getTargetAlias()) + "/statuses";
        return clientCommunicator.doPost(urlPath, request, null, Response.class);
    }

    public StatusesResponse getStory(PagedRequest<Status> request) throws IOException, TweeterRemoteException {
        String urlPath = "/getstory"; // ideally would be "/users/" + sanitizeAlias(request.getTargetAlias()) + "/statuses";
        return clientCommunicator.doPost(urlPath, request, null, StatusesResponse.class); // SHOULD BE GET!!!
    }

    public StatusesResponse getFeed(PagedRequest<Status> request) throws IOException, TweeterRemoteException {
        String urlPath = "/getfeed"; // ideally would be "/users/" + sanitizeAlias(request.getTargetAlias()) + "/feed";
        return clientCommunicator.doPost(urlPath, request, null, StatusesResponse.class); // SHOULD BE GET!!!
    }

    private Map<String, String> createHeaders(AuthToken authToken) {
        return Map.of("Authorization", "Bearer " + authToken.getToken());
    }

    private String sanitizeAlias(String userAlias) {
        return userAlias.substring(1);
    }
}
