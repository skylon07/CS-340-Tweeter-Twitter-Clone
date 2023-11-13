package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.server.service.StatusService;
import edu.byu.cs.tweeter.server.service.UserService;

public class GetStoryHandler implements RequestHandler<PagedRequest<Status>, StatusesResponse> {
    @Override
    public StatusesResponse handleRequest(PagedRequest<Status> request, Context context) {
        StatusService service = new StatusService();
        return service.getStory(request);
    }
}
