package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequestByLong;
import edu.byu.cs.tweeter.model.net.request.StatusRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.server.dao.interfaces.DaoFactory;
import edu.byu.cs.tweeter.server.service.exceptions.BadRequestException;
import edu.byu.cs.tweeter.server.sqs.SqsClient;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService extends BaseService {
    // TODO: could be abstracted into a factory like the DAOs
    private static final SqsClient sqsClient = new SqsClient();

    public StatusService(DaoFactory daoFactory) {
        super(daoFactory);
    }

    public Response postStatus(StatusRequest request) {
        validateStatusRequest(request);
        checkValidStatusRequest(request);

        User poster = getDaos().getUserDao().getUser(request.getTargetAlias());
        // TODO: feels a little weird the DAO isn't passed the status...
        getDaos().getStatusDao().postStatusToStory(poster, request.getPost(), request.getTimestamp());

        Status status = new Status(
            request.getPost(),
            poster,
            request.getTimestamp(),
            Status.parseURLs(request.getPost()),
            Status.parseMentions(request.getPost())
        );
        sqsClient.sendPostedStatusMessage(status);

        return new Response();
    }

    public void postStatusToFeeds(List<String> followerAliases, Status status) {
        getDaos().getStatusDao().postStatusToFeeds(followerAliases, status.getUser(), status.getPost(), status.getTimestamp());
    }

    public StatusesResponse getFeed(PagedRequestByLong request) {
        validatePagedRequest(request);

        Pair<List<Status>, Boolean> pageData = getDaos().getStatusDao().getFeed(request.getTargetAlias(), request.getLimit(), request.getLastItem());
        return new StatusesResponse(pageData.getFirst(), pageData.getSecond());
    }

    public StatusesResponse getStory(PagedRequestByLong request) {
        validatePagedRequest(request);

        Pair<List<Status>, Boolean> pageData = getDaos().getStatusDao().getStory(request.getTargetAlias(), request.getLimit(), request.getLastItem());
        return new StatusesResponse(pageData.getFirst(), pageData.getSecond());
    }

    private void checkValidStatusRequest(StatusRequest request) {
        String actingUserAlias = getDaos().getSessionDao().getAssociatedUsername(request.getAuthToken());
        if (!actingUserAlias.equals(request.getTargetAlias())) throw new BadRequestException("User can only post statuses for themselves");
    }
}
