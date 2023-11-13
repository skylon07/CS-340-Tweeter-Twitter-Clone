package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class StatusesResponse extends PagedResponse<Status> {
    public StatusesResponse(String message) {
        super(message);
    }
    public StatusesResponse(List<Status> statuses, boolean hasMorePages) {
        super(statuses, hasMorePages);
    }
}
