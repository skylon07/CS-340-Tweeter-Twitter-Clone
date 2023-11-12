package edu.byu.cs.tweeter.server.service.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super("[Bad Request] " + message);
    }
}
