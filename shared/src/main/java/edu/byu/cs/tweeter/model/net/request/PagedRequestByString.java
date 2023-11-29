package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class PagedRequestByString extends PagedRequest<String> {
    public PagedRequestByString() { super(); }

    public PagedRequestByString(AuthToken authToken, String targetAlias, Integer limit, String lastItem) {
        super(authToken, targetAlias, limit, lastItem);
    }
}
