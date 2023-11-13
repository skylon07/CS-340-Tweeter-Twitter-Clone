package edu.byu.cs.tweeter.client.model.net;

import java.io.IOException;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.AuthToken;
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

    private static final String SERVER_URL = "https://vj2ldrkjii.execute-api.us-west-1.amazonaws.com/production";

    private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);

    public LoginResponse register(RegisterRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/users";
        return clientCommunicator.doPost(urlPath, request, null, LoginResponse.class);
    }

    /**
     * Performs a login and if successful, returns the logged in user and an auth token.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    public LoginResponse login(LoginRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/sessions";
        return clientCommunicator.doPost(urlPath, request, null, LoginResponse.class);
    }

    public Response logout(AuthorizedRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/sessions/" + request.getAuthToken().getToken();
        return clientCommunicator.doDelete(urlPath, request, createHeaders(request.getAuthToken()), Response.class);
    }

    public UserResponse getUser(UserTargetedRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/users/" + sanitizeAlias(request.getTargetAlias());
        return clientCommunicator.doGet(urlPath, createHeaders(request.getAuthToken()), UserResponse.class);
    }

    public UsersResponse getFollowers(PagedRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/users/" + sanitizeAlias(request.getTargetAlias()) + "/followers/users";
        return clientCommunicator.doGet(urlPath, createHeaders(request.getAuthToken()), UsersResponse.class);
    }

    public CountResponse countFollowers(UserTargetedRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/users/" + sanitizeAlias(request.getTargetAlias()) + "/followers/count";
        return clientCommunicator.doGet(urlPath, createHeaders(request.getAuthToken()), CountResponse.class);
    }

    public UsersResponse getFollowing(PagedRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/users/" + sanitizeAlias(request.getTargetAlias()) + "/following/users";
        return clientCommunicator.doGet(urlPath, createHeaders(request.getAuthToken()), UsersResponse.class);
    }

    public CountResponse countFollowing(UserTargetedRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/users/" + sanitizeAlias(request.getTargetAlias()) + "/following/count";
        return clientCommunicator.doGet(urlPath, createHeaders(request.getAuthToken()), CountResponse.class);
    }

    public IsFollowingResponse isFollowing(FollowsRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/users/" + sanitizeAlias(request.getFollowerAlias()) + "/following/users/" + sanitizeAlias(request.getFolloweeAlias());
        return clientCommunicator.doGet(urlPath, createHeaders(request.getAuthToken()), IsFollowingResponse.class);
    }

    public Response follow(FollowsRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/users/" + sanitizeAlias(request.getFollowerAlias()) + "/following/users/" + sanitizeAlias(request.getFolloweeAlias());
        return clientCommunicator.doPost(urlPath, request, createHeaders(request.getAuthToken()), Response.class);
    }

    public Response unfollow(FollowsRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/users/" + sanitizeAlias(request.getFollowerAlias()) + "/following/users/" + sanitizeAlias(request.getFolloweeAlias());
        return clientCommunicator.doDelete(urlPath, request, createHeaders(request.getAuthToken()), Response.class);
    }

    public Response postStatus(StatusRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/users/" + sanitizeAlias(request.getTargetAlias()) + "/statuses";
        return clientCommunicator.doPost(urlPath, request, createHeaders(request.getAuthToken()), Response.class);
    }

    public StatusesResponse getStory(PagedRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/users/" + sanitizeAlias(request.getTargetAlias()) + "/statuses";
        return clientCommunicator.doGet(urlPath, createHeaders(request.getAuthToken()), StatusesResponse.class);
    }

    public StatusesResponse getFeed(PagedRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/users/" + sanitizeAlias(request.getTargetAlias()) + "/feed";
        return clientCommunicator.doGet(urlPath, createHeaders(request.getAuthToken()), StatusesResponse.class);
    }

    private Map<String, String> createHeaders(AuthToken authToken) {
        return Map.of("Authorization", "Bearer " + authToken.getToken());
    }

    private String sanitizeAlias(String userAlias) {
        return userAlias.substring(1);
    }
}
