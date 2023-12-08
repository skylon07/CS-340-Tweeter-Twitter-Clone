package edu.byu.cs.tweeter.server.sqs.messages;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class UpdateFeedMessage {
    private List<String> feedOwnerAliases;
    private Status status;

    public UpdateFeedMessage() {}

    public UpdateFeedMessage(Status status, List<String> feedOwnerAliases) {
        this.status = status;
        this.feedOwnerAliases = feedOwnerAliases;
    }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public List<String> getFeedOwnerAliases() { return feedOwnerAliases; }
    public void setFeedOwnerAliases(List<String> feedOwnerAliases) { this.feedOwnerAliases = feedOwnerAliases; }
}
