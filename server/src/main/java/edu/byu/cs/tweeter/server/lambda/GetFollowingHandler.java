package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequestByString;
import edu.byu.cs.tweeter.model.net.response.UsersResponse;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.DynamoFollowDao;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.DynamoSessionDao;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.DynamoUserDao;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowingHandler implements RequestHandler<PagedRequestByString, UsersResponse> {

    @Override
    public UsersResponse handleRequest(PagedRequestByString request, Context context) {
        FollowService service = new FollowService(new DynamoSessionDao(), new DynamoFollowDao(), new DynamoUserDao());
        return service.getFollowing(request);
    }
}
