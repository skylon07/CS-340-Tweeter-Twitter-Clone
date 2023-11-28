package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.StatusRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.server.dao.interfaces.SessionDao;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDao;
import edu.byu.cs.tweeter.server.service.exceptions.BadRequestException;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService extends BaseService {
    private final StatusDao statusDao;

    public StatusService(SessionDao sessionDao, StatusDao statusDao) {
        super(sessionDao);
        this.statusDao = statusDao;
    }

    public Response postStatus(StatusRequest request) {
        validateStatusRequest(request);
        checkValidStatusRequest(request);

        statusDao.postStatusToStoryAndFeeds(request.getTargetAlias(), request.getPost(), request.getTimestamp());
        return new Response();
    }

    public StatusesResponse getFeed(PagedRequest<Status> request) {
        validatePagedRequest(request);

        Pair<List<Status>, Boolean> pageData = statusDao.getFeed(request.getTargetAlias(), request.getLimit(), request.getLastItem());
        return new StatusesResponse(pageData.getFirst(), pageData.getSecond());
    }

    public StatusesResponse getStory(PagedRequest<Status> request) {
        validatePagedRequest(request);

        Pair<List<Status>, Boolean> pageData = statusDao.getStory(request.getTargetAlias(), request.getLimit(), request.getLastItem());
        return new StatusesResponse(pageData.getFirst(), pageData.getSecond());
    }

    private void checkValidStatusRequest(StatusRequest request) {
        String actingUserAlias = sessionDao.getAssociatedUsername(request.getAuthToken());
        if (!actingUserAlias.equals(request.getTargetAlias())) throw new BadRequestException("User can only post statuses for themselves");
    }
}
