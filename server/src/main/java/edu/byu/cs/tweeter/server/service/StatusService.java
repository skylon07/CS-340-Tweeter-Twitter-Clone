package edu.byu.cs.tweeter.server.service;

import java.util.List;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequestByLong;
import edu.byu.cs.tweeter.model.net.request.StatusRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDao;
import edu.byu.cs.tweeter.server.dao.interfaces.SessionDao;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDao;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDao;
import edu.byu.cs.tweeter.server.service.exceptions.BadRequestException;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService extends BaseService {
    private final StatusDao statusDao;
    private final FollowDao followDao;
    private final UserDao userDao;

    public StatusService(SessionDao sessionDao, StatusDao statusDao, FollowDao followDao, UserDao userDao) {
        super(sessionDao);
        this.statusDao = statusDao;
        this.followDao = followDao;
        this.userDao = userDao;
    }

    public Response postStatus(StatusRequest request) {
        validateStatusRequest(request);
        checkValidStatusRequest(request);

        // TODO: needs to be a background task that updates statuses for all followers
        List<User> followers = followDao.getFollowers(request.getTargetAlias(), Integer.MAX_VALUE, null).getFirst();
        List<String> followerAliases = followers.stream().map(User::getAlias).collect(Collectors.toList());

        User poster = userDao.getUser(request.getTargetAlias());
        statusDao.postStatusToStoryAndFeeds(poster, followerAliases, request.getPost(), request.getTimestamp());
        return new Response();
    }

    public StatusesResponse getFeed(PagedRequestByLong request) {
        validatePagedRequest(request);

        Pair<List<Status>, Boolean> pageData = statusDao.getFeed(request.getTargetAlias(), request.getLimit(), request.getLastItem());
        return new StatusesResponse(pageData.getFirst(), pageData.getSecond());
    }

    public StatusesResponse getStory(PagedRequestByLong request) {
        validatePagedRequest(request);

        Pair<List<Status>, Boolean> pageData = statusDao.getStory(request.getTargetAlias(), request.getLimit(), request.getLastItem());
        return new StatusesResponse(pageData.getFirst(), pageData.getSecond());
    }

    private void checkValidStatusRequest(StatusRequest request) {
        String actingUserAlias = sessionDao.getAssociatedUsername(request.getAuthToken());
        if (!actingUserAlias.equals(request.getTargetAlias())) throw new BadRequestException("User can only post statuses for themselves");
    }
}
