package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class UserTargetedRequest extends AuthorizedRequest {
    private String targetAlias;

    public UserTargetedRequest() { super(); }

    public UserTargetedRequest(AuthToken authToken, String targetAlias) {
        super(authToken);
        this.targetAlias = targetAlias;
    }

    public String getTargetAlias() {
        return targetAlias;
    }

    public void setTargetAlias(String targetAlias) {
        this.targetAlias = targetAlias;
    }
}
