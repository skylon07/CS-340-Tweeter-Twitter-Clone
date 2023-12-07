package edu.byu.cs.tweeter.server.lambda.endpoints;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.PagedRequestByLong;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.DynamoDaoFactory;
import edu.byu.cs.tweeter.server.service.StatusService;

public class GetStoryHandler implements RequestHandler<PagedRequestByLong, StatusesResponse> {
    @Override
    public StatusesResponse handleRequest(PagedRequestByLong request, Context context) {
        StatusService service = new StatusService(new DynamoDaoFactory());
        return service.getStory(request);
    }
}
