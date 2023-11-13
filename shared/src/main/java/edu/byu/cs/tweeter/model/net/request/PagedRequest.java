package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followees for a specified follower.
 */
public class PagedRequest extends UserTargetedRequest {
    private int limit;
    private String lastPageMark;

    public PagedRequest() { super(); }

    public PagedRequest(AuthToken authToken, String targetAlias, int limit, String lastPageMark) {
        super(authToken, targetAlias);
        this.limit = limit;
        this.lastPageMark = lastPageMark;
    }

    /**
     * Returns the number representing the maximum number of followees to be returned by this request.
     *
     * @return the limit.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Sets the limit.
     *
     * @param limit the limit.
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getLastPageMark() {
        return lastPageMark;
    }

    public void setLastPageMark(String lastPageMark) {
        this.lastPageMark = lastPageMark;
    }
}
