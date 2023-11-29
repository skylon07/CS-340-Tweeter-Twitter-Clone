package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.PagedRequestByString;
import edu.byu.cs.tweeter.model.net.response.UsersResponse;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.DynamoDaoFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowersHandler implements RequestHandler<PagedRequestByString, UsersResponse> {
    @Override
    public UsersResponse handleRequest(PagedRequestByString request, Context context) {
        FollowService service = new FollowService(new DynamoDaoFactory());
        return service.getFollowers(request);
    }
}
