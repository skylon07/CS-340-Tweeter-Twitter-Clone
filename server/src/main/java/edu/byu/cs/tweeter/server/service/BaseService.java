package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.AuthorizedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.Request;
import edu.byu.cs.tweeter.model.net.request.StatusRequest;
import edu.byu.cs.tweeter.model.net.request.UserTargetedRequest;
import edu.byu.cs.tweeter.server.dao.interfaces.SessionDao;
import edu.byu.cs.tweeter.server.service.exceptions.BadRequestException;
import edu.byu.cs.tweeter.server.service.exceptions.RequestMissingPropertyException;
import edu.byu.cs.tweeter.server.service.exceptions.UnauthorizedRequestException;

public class BaseService {
    private static final long AUTH_TOKEN_AGE_LIMIT = 1000 * 60 * 10; // ten minutes for testing purposes

    protected final SessionDao sessionDao;

    BaseService(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    protected void validateRequest(Request request) {
        if (request == null) throw new BadRequestException("Request is null!");
    }

    protected void validateLoginRequest(LoginRequest request) {
        validateRequest(request);

        if (request.getUsername() == null) throw new RequestMissingPropertyException("username");
        if (request.getPassword() == null) throw new RequestMissingPropertyException("password");
    }

    protected void validateRegisterRequest(RegisterRequest request) {
        validateLoginRequest(request);

        if (request.getFirstName() == null) throw new RequestMissingPropertyException("firstName");
        if (request.getLastName() == null) throw new RequestMissingPropertyException("lastName");
        if (request.getImage() == null) throw new RequestMissingPropertyException("image");
    }

    protected void validateAuthorizedRequest(AuthorizedRequest request) {
        validateRequest(request);
        validateAuth(request.getAuthToken());

        if (request.getAuthToken() == null) throw new UnauthorizedRequestException();
    }

    protected void validateUserTargetedRequest(UserTargetedRequest request) {
        validateAuthorizedRequest(request);

        if (request.getTargetAlias() == null) throw new RequestMissingPropertyException("targetAlias");
    }

    protected <ItemT> void validatePagedRequest(PagedRequest<ItemT> request) {
        validateUserTargetedRequest(request);

        if (request.getLimit() <= 0) throw new BadRequestException("Request property \"limit\" must be a positive integer or zero");
        // lastItem can be null
    }

    protected void validateFollowsRequest(FollowsRequest request) {
        validateUserTargetedRequest(request);

        if (request.getFollowerAlias() == null) throw new RequestMissingPropertyException("followerAlias");
        if (request.getFolloweeAlias() == null) throw new RequestMissingPropertyException("followeeAlias");
    }

    protected void validateStatusRequest(StatusRequest request) {
        validateUserTargetedRequest(request);

        if (request.getPost() == null) throw new RequestMissingPropertyException("post");
        if (request.getTimestamp() == null) throw new RequestMissingPropertyException("timestamp");
    }

    private void validateAuth(AuthToken authToken) {
        authToken = sessionDao.updateTimestamp(authToken);
        String associatedUser = sessionDao.getAssociatedUsername(authToken);
        long currentTime = System.currentTimeMillis();
        if (associatedUser == null || isExpiredAuthToken(authToken, currentTime)) {
            cleanExpiredSessions(); // TODO: should be done as a background job or something...
            throw new UnauthorizedRequestException();
        }

        resetExpiration(authToken);
    }

    private void cleanExpiredSessions() {
        long currentTime = System.currentTimeMillis();
        sessionDao.getAuthTokens().forEach((authToken -> {
            if (isExpiredAuthToken(authToken, currentTime)) {
                sessionDao.revokeSession(authToken);
            }
        }));
    }

    private boolean isExpiredAuthToken(AuthToken authToken, long currentTime) {
        long timeSinceCreation = currentTime - authToken.getTimestamp();
        return timeSinceCreation > AUTH_TOKEN_AGE_LIMIT;
    }

    private void resetExpiration(AuthToken authToken) {
        authToken.setTimestamp(System.currentTimeMillis());
        sessionDao.saveUpdatedTimestamp(authToken);
    }
}
