package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequestByLong;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.DynamoFollowDao;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.DynamoSessionDao;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.DynamoStatusDao;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.DynamoUserDao;
import edu.byu.cs.tweeter.server.service.StatusService;

public class GetFeedHandler implements RequestHandler<PagedRequestByLong, StatusesResponse> {
    @Override
    public StatusesResponse handleRequest(PagedRequestByLong request, Context context) {
        StatusService service = new StatusService(new DynamoSessionDao(), new DynamoStatusDao(), new DynamoFollowDao(), new DynamoUserDao());
        return service.getFeed(request);
    }
}
