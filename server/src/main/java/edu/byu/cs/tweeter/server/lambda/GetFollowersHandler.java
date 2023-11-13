package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.UsersResponse;
import edu.byu.cs.tweeter.server.service.FollowService;
import edu.byu.cs.tweeter.server.service.UserService;

public class GetFollowersHandler implements RequestHandler<PagedRequest<User>, UsersResponse> {
    @Override
    public UsersResponse handleRequest(PagedRequest<User> request, Context context) {
        FollowService service = new FollowService();
        return service.getFollowers(request);
    }
}
