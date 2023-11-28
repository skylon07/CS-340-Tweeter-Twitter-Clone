package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.AuthorizedRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.implementations.fakedata.FakeDataSessionDao;
import edu.byu.cs.tweeter.server.dao.implementations.fakedata.FakeDataUserDao;
import edu.byu.cs.tweeter.server.service.UserService;

public class LogoutHandler implements RequestHandler<AuthorizedRequest, Response> {
    @Override
    public Response handleRequest(AuthorizedRequest request, Context context) {
        UserService service = new UserService(new FakeDataSessionDao(), new FakeDataUserDao());
        return service.logout(request);
    }
}
