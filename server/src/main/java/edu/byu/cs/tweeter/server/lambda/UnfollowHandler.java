package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.service.FollowService;
import edu.byu.cs.tweeter.server.service.UserService;

public class UnfollowHandler implements RequestHandler<FollowsRequest, Response> {
    @Override
    public Response handleRequest(FollowsRequest request, Context context) {
        FollowService service = new FollowService();
        return service.unfollow(request);
    }
}
