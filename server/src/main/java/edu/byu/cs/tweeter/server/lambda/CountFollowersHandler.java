package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.UserTargetedRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.server.dao.implementations.fakedata.FakeDataFollowDao;
import edu.byu.cs.tweeter.server.dao.implementations.fakedata.FakeDataSessionDao;
import edu.byu.cs.tweeter.server.service.FollowService;

public class CountFollowersHandler implements RequestHandler<UserTargetedRequest, CountResponse> {
    @Override
    public CountResponse handleRequest(UserTargetedRequest request, Context context) {
        FollowService service = new FollowService(new FakeDataSessionDao(), new FakeDataFollowDao());
        return service.countFollowers(request);
    }
}
