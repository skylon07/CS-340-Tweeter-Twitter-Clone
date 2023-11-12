package edu.byu.cs.tweeter.server.service.exceptions;

import edu.byu.cs.tweeter.server.service.exceptions.BadRequestException;

public class RequestMissingPropertyException extends BadRequestException {
    public RequestMissingPropertyException(String property) {
        super("Missing required property \"" + property + "\"");
    }
}
