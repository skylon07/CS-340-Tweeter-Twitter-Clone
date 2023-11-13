package edu.byu.cs.tweeter.model.net.response;

import java.io.Serializable;

/**
 * A base class for server responses.
 */
public class Response implements Serializable {

    private final boolean success;
    private final String errorMessage;

    Response() {
        this.success = true;
        this.errorMessage = null;
    }

    Response(String errorMessage) {
        this.success = false;
        this.errorMessage = errorMessage;
    }

    /**
     * Indicates whether the response represents a successful result.
     *
     * @return the success indicator.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * The error message for unsuccessful results.
     *
     * @return an error message or null if the response indicates a successful result.
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}
