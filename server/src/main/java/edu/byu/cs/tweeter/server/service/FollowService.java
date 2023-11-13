package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.UsersResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.service.exceptions.BadRequestException;
import edu.byu.cs.tweeter.server.service.exceptions.RequestMissingPropertyException;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public UsersResponse getFollowees(PagedRequest request) {
        if (request.getRequestOwnerAlias() == null) {
            throw new RequestMissingPropertyException("requestOwnerAlias");
        } else if (request.getLimit() <= 0) {
            throw new BadRequestException("Request property \"limit\" must be a positive integer or zero");
        }

        Pair<List<User>, Boolean> pair = getFollowingDAO().getFollowees(request.getRequestOwnerAlias(), request.getLimit(), request.getLastPageMark());
        return new UsersResponse(pair.getFirst(), pair.getSecond());
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
