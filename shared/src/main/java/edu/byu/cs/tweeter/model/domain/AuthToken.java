package edu.byu.cs.tweeter.model.domain;

import java.io.Serializable;

/**
 * Represents an auth token in the system.
 */
public class AuthToken implements Serializable {
    /**
     * Value of the auth token.
     */
    private String token;
    /**
     * String representation of date/time at which the auth token was created.
     */
    private long timestamp;

    public AuthToken() {
    }

    public AuthToken(String token) {
        this.token = token;
    }

    public AuthToken(String token, long timestamp) {
        this.token = token;
        this.timestamp = timestamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}