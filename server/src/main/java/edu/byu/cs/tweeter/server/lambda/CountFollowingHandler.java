package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.UserTargetedRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.server.service.FollowService;
import edu.byu.cs.tweeter.server.service.UserService;

public class CountFollowingHandler implements RequestHandler<UserTargetedRequest, CountResponse> {
    @Override
    public CountResponse handleRequest(UserTargetedRequest request, Context context) {
        FollowService service = new FollowService();
        return service.countFollowing(request);
    }
}
