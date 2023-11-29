package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequestByString;
import edu.byu.cs.tweeter.model.net.request.UserTargetedRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowingResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.UsersResponse;
import edu.byu.cs.tweeter.server.dao.interfaces.DaoFactory;
import edu.byu.cs.tweeter.server.service.exceptions.BadRequestException;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService extends BaseService {

    public FollowService(DaoFactory daoFactory) {
        super(daoFactory);
    }

    public UsersResponse getFollowers(PagedRequestByString request) {
        validatePagedRequest(request);

        Pair<List<User>, Boolean> pageData = getDaos().getFollowDao().getFollowers(request.getTargetAlias(), request.getLimit(), request.getLastItem());
        return new UsersResponse(pageData.getFirst(), pageData.getSecond());
    }

    public CountResponse countFollowers(UserTargetedRequest request) {
        validateUserTargetedRequest(request);

        Integer count = getDaos().getFollowDao().getFollowerCount(request.getTargetAlias());
        return new CountResponse(count);
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
    public UsersResponse getFollowing(PagedRequestByString request) {
        validatePagedRequest(request);

        Pair<List<User>, Boolean> pageData = getDaos().getFollowDao().getFollowees(request.getTargetAlias(), request.getLimit(), request.getLastItem());
        return new UsersResponse(pageData.getFirst(), pageData.getSecond());
    }

    public CountResponse countFollowing(UserTargetedRequest request) {
        validateUserTargetedRequest(request);

        Integer count = getDaos().getFollowDao().getFolloweeCount(request.getTargetAlias());
        return new CountResponse(count);
    }

    public IsFollowingResponse isFollowing(FollowsRequest request) {
        validateFollowsRequest(request);

        boolean isFollowing = getDaos().getFollowDao().isFollowing(request.getFollowerAlias(), request.getFolloweeAlias());
        return new IsFollowingResponse(isFollowing);
    }

    public Response follow(FollowsRequest request) {
        validateFollowsRequest(request);
        checkValidFollowsRequest(request);

        User follower = getDaos().getUserDao().getUser(request.getFollowerAlias());
        User followee = getDaos().getUserDao().getUser(request.getFolloweeAlias());
        getDaos().getFollowDao().recordFollow(follower, followee);
        return new Response();
    }

    public Response unfollow(FollowsRequest request) {
        validateFollowsRequest(request);
        checkValidFollowsRequest(request);

        getDaos().getFollowDao().removeFollow(request.getFollowerAlias(), request.getFolloweeAlias());
        return new Response();
    }

    void checkValidFollowsRequest(FollowsRequest request) {
        String actingUserAlias = getDaos().getSessionDao().getAssociatedUsername(request.getAuthToken());
        if (!actingUserAlias.equals(request.getFollowerAlias())) throw new BadRequestException("User can only request following changes as the follower");
    }
}
