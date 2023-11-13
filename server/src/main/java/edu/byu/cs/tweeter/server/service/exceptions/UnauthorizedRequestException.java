package edu.byu.cs.tweeter.server.service.exceptions;

public class UnauthorizedRequestException extends BadRequestException {
    public UnauthorizedRequestException() {
        super("Unauthorized -- auth token is missing or invalid for requested action");
    }
}
