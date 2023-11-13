package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class StatusRequest extends UserTargetedRequest {
    private String post;
    private Long timestamp;

    public StatusRequest(AuthToken authToken, String targetAlias, String post, Long timestamp) {
        super(authToken, targetAlias);
        this.post = post;
        this.timestamp = timestamp;
    }

    public String getPost() { return post; }

    public void setPost(String post) { this.post = post; }

    public Long getTimestamp() { return timestamp; }

    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
}
