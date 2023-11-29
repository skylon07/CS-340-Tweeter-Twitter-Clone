package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.UserTargetedRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.DynamoFollowDao;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.DynamoSessionDao;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.DynamoUserDao;
import edu.byu.cs.tweeter.server.service.FollowService;

public class CountFollowingHandler implements RequestHandler<UserTargetedRequest, CountResponse> {
    @Override
    public CountResponse handleRequest(UserTargetedRequest request, Context context) {
        FollowService service = new FollowService(new DynamoSessionDao(), new DynamoFollowDao(), new DynamoUserDao());
        return service.countFollowing(request);
    }
}
