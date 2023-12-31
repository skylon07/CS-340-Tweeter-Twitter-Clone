package edu.byu.cs.tweeter.server.lambda.endpoints;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowingResponse;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.DynamoDaoFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class IsFollowingHandler implements RequestHandler<FollowsRequest, IsFollowingResponse> {
    @Override
    public IsFollowingResponse handleRequest(FollowsRequest request, Context context) {
        FollowService service = new FollowService(new DynamoDaoFactory());
        return service.isFollowing(request);
    }
}
