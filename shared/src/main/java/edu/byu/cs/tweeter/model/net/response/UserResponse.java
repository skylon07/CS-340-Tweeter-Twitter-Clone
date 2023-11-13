package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.User;

public class UserResponse extends Response {
    private final User user;

    public UserResponse(String messsage) {
        super(messsage);
        user = null;
    }

    public UserResponse(User user) { this.user = user; }

    public User getUser() { return user; }
}
