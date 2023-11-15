package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followees for a specified follower.
 */
public class PagedRequest<ItemT> extends UserTargetedRequest {
    private Integer limit;
    private ItemT lastItem;

    public PagedRequest() { super(); }

    public PagedRequest(AuthToken authToken, String targetAlias, Integer limit, ItemT lastItem) {
        super(authToken, targetAlias);
        this.limit = limit;
        this.lastItem = lastItem;
    }

    /**
     * Returns the number representing the maximum number of followees to be returned by this request.
     *
     * @return the limit.
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * Sets the limit.
     *
     * @param limit the limit.
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public ItemT getLastItem() {
        return lastItem;
    }

    public void setLastItem(ItemT lastItem) {
        this.lastItem = lastItem;
    }
}
