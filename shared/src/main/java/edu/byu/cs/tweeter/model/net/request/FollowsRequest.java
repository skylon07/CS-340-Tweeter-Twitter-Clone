package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FollowsRequest extends UserTargetedRequest {
    private String followeeAlias;

    public FollowsRequest() { super(); }

    public FollowsRequest(AuthToken authToken, String followerAlias, String followeeAlias) {
        super(authToken, followerAlias);
        this.followeeAlias = followeeAlias;
    }

    public String getFollowerAlias() {
            return getTargetAlias();
        }

    public void setFollowerAlias(String followerAlias) {
            setTargetAlias(followerAlias);
    }

    public String getFolloweeAlias() {
                return followeeAlias;
            }

    public void setFolloweeAlias(String followeeAlias) {
                this.followeeAlias = followeeAlias;
    }
}
