package edu.byu.cs.tweeter.server.lambda.background;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.DynamoDaoFactory;
import edu.byu.cs.tweeter.server.service.StatusService;
import edu.byu.cs.tweeter.server.sqs.JsonSerializer;
import edu.byu.cs.tweeter.server.sqs.messages.UpdateFeedMessage;

public class UpdateFeedHandler implements RequestHandler<SQSEvent, Void> {
    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        StatusService service = new StatusService(new DynamoDaoFactory());
        for (SQSEvent.SQSMessage messageData : event.getRecords()) {
            UpdateFeedMessage message = JsonSerializer.deserialize(messageData.getBody(), UpdateFeedMessage.class);
            service.postStatusToFeeds(message.getFeedOwnerAliases(), message.getStatus());
        }
        return null;
    }
}
