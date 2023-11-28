package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.UsersResponse;
import edu.byu.cs.tweeter.server.dao.implementations.fakedata.FakeDataFollowDao;
import edu.byu.cs.tweeter.server.dao.implementations.fakedata.FakeDataSessionDao;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowersHandler implements RequestHandler<PagedRequest<User>, UsersResponse> {
    @Override
    public UsersResponse handleRequest(PagedRequest<User> request, Context context) {
        FollowService service = new FollowService(new FakeDataSessionDao(), new FakeDataFollowDao());
        return service.getFollowers(request);
    }
}
