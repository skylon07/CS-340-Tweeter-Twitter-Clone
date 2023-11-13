package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.StatusRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService extends BaseService {
    public Response postStatus(StatusRequest request) {
        validateStatusRequest(request);

        // TODO: actually record status post in DB
        return new Response();
    }

    public StatusesResponse getFeed(PagedRequest<Status> request) {
        validatePagedRequest(request);

        // TODO: Generates dummy data. Replace with a real implementation.
        Pair<List<Status>, Boolean> pageData = FakeData.getInstance().getPageOfStatus(request.getLastItem(), request.getLimit());
        return new StatusesResponse(pageData.getFirst(), pageData.getSecond());
    }

    public StatusesResponse getStory(PagedRequest<Status> request) {
        validatePagedRequest(request);

        // TODO: Generates dummy data. Replace with a real implementation.
        Pair<List<Status>, Boolean> pageData = FakeData.getInstance().getPageOfStatus(request.getLastItem(), request.getLimit());
        return new StatusesResponse(pageData.getFirst(), pageData.getSecond());
    }
}
