package edu.byu.cs.tweeter.server.sqs.messages;

import edu.byu.cs.tweeter.model.domain.Status;

public class PostedStatusMessage {
    private Status status;

    public PostedStatusMessage() {}

    public PostedStatusMessage(Status status) {
        this.status = status;
    }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
