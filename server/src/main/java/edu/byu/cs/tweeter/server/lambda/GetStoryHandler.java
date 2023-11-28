package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.DynamoSessionDao;
import edu.byu.cs.tweeter.server.dao.implementations.fakedata.FakeDataStatusDao;
import edu.byu.cs.tweeter.server.service.StatusService;

public class GetStoryHandler implements RequestHandler<PagedRequest<Status>, StatusesResponse> {
    @Override
    public StatusesResponse handleRequest(PagedRequest<Status> request, Context context) {
        StatusService service = new StatusService(new DynamoSessionDao(), new FakeDataStatusDao());
        return service.getStory(request);
    }
}
