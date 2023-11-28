package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.implementations.fakedata.FakeDataFollowDao;
import edu.byu.cs.tweeter.server.dao.implementations.fakedata.FakeDataSessionDao;
import edu.byu.cs.tweeter.server.service.FollowService;

public class FollowHandler implements RequestHandler<FollowsRequest, Response> {
    @Override
    public Response handleRequest(FollowsRequest request, Context context) {
        FollowService service = new FollowService(new FakeDataSessionDao(), new FakeDataFollowDao());
        return service.follow(request);
    }
}
