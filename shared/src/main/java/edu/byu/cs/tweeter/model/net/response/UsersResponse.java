package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;

/**
 * A paged response for a {@link PagedRequest}.
 */
public class UsersResponse extends PagedResponse<User> {
    public UsersResponse(String message) {
        super(message);
    }
    public UsersResponse(List<User> users, boolean hasMorePages) {
        super(users, hasMorePages);
    }
}
