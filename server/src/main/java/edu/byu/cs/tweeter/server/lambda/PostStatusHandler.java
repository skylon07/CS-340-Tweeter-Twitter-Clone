package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.StatusRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.service.StatusService;
import edu.byu.cs.tweeter.server.service.UserService;

public class PostStatusHandler implements RequestHandler<StatusRequest, Response> {
    @Override
    public Response handleRequest(StatusRequest request, Context context) {
        StatusService service = new StatusService();
        return service.postStatus(request);
    }
}
