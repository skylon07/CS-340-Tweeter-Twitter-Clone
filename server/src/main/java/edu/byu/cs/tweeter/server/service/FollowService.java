package edu.byu.cs.tweeter.server.service;

import java.util.List;
import java.util.Random;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.UserTargetedRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowingResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.UsersResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService extends BaseService {
    public UsersResponse getFollowers(PagedRequest<User> request) {
        validatePagedRequest(request);

        // TODO: Generates dummy data. Replace with a real implementation.
        String lastAlias = null;
        if (request.getLastItem() != null) {
            lastAlias = request.getLastItem().getAlias();
        }
        Pair<List<User>, Boolean> pair = getFollowingDAO().getFollowees(request.getTargetAlias(), request.getLimit(), lastAlias);
        return new UsersResponse(pair.getFirst(), pair.getSecond());
    }

    public CountResponse countFollowers(UserTargetedRequest request) {
        validateUserTargetedRequest(request);

        // TODO: fake data; replace with real count
        return new CountResponse(72);
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public UsersResponse getFollowing(PagedRequest<User> request) {
        validatePagedRequest(request);

        // TODO: Generates dummy data. Replace with a real implementation.
        String lastAlias = null;
        if (request.getLastItem() != null) {
            lastAlias = request.getLastItem().getAlias();
        }
        Pair<List<User>, Boolean> pair = getFollowingDAO().getFollowees(request.getTargetAlias(), request.getLimit(), lastAlias);
        return new UsersResponse(pair.getFirst(), pair.getSecond());
    }

    public CountResponse countFollowing(UserTargetedRequest request) {
        validateUserTargetedRequest(request);

        // TODO: fake data; replace with real count
        return new CountResponse(27);
    }

    public IsFollowingResponse isFollowing(FollowsRequest request) {
        validateFollowsRequest(request);

        // TODO: fake data; replace with real info
        return new IsFollowingResponse(new Random().nextInt() > 0);
    }

    public Response follow(FollowsRequest request) {
        validateFollowsRequest(request);

        // TODO: should actually change data in the DB here
        return new Response();
    }

    public Response unfollow(FollowsRequest request) {
        validateFollowsRequest(request);

        // TODO: should actually change data in the DB here
        return new Response();
    }

    /**
     * Returns an instance of {@link FollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowDAO getFollowingDAO() {
        return new FollowDAO();
    }
}
