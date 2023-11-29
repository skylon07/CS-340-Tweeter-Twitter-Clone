package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class PagedRequestByLong extends PagedRequest<Long> {
    public PagedRequestByLong() { super(); }

    public PagedRequestByLong(AuthToken authToken, String targetAlias, Integer limit, Long lastItem) {
        super(authToken, targetAlias, limit, lastItem);
    }
}
