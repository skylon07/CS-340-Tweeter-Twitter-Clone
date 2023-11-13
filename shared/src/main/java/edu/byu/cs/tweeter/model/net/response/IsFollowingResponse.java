package edu.byu.cs.tweeter.model.net.response;

public class IsFollowingResponse extends Response {
    private final Boolean isFollowing;

    public IsFollowingResponse(String message) {
        super(message);
        this.isFollowing = null;
    }

    public IsFollowingResponse(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

    public Boolean getIsFollowing() { return isFollowing; }
}
