package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowingResponse;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.DynamoFollowDao;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.DynamoSessionDao;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.DynamoUserDao;
import edu.byu.cs.tweeter.server.service.FollowService;

public class IsFollowingHandler implements RequestHandler<FollowsRequest, IsFollowingResponse> {
    @Override
    public IsFollowingResponse handleRequest(FollowsRequest request, Context context) {
        FollowService service = new FollowService(new DynamoSessionDao(), new DynamoFollowDao(), new DynamoUserDao());
        return service.isFollowing(request);
    }
}
