package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.UserTargetedRequest;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.DynamoSessionDao;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.DynamoUserDao;
import edu.byu.cs.tweeter.server.dao.implementations.fakedata.FakeDataUserDao;
import edu.byu.cs.tweeter.server.service.UserService;

public class GetUserHandler implements RequestHandler<UserTargetedRequest, UserResponse> {
    @Override
    public UserResponse handleRequest(UserTargetedRequest request, Context context) {
        UserService service = new UserService(new DynamoSessionDao(), new DynamoUserDao());
        return service.getUser(request);
    }
}
