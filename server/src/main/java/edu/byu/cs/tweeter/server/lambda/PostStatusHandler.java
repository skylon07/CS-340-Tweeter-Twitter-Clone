package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.StatusRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.implementations.fakedata.FakeDataSessionDao;
import edu.byu.cs.tweeter.server.dao.implementations.fakedata.FakeDataStatusDao;
import edu.byu.cs.tweeter.server.service.StatusService;

public class PostStatusHandler implements RequestHandler<StatusRequest, Response> {
    @Override
    public Response handleRequest(StatusRequest request, Context context) {
        StatusService service = new StatusService(new FakeDataSessionDao(), new FakeDataStatusDao());
        return service.postStatus(request);
    }
}
