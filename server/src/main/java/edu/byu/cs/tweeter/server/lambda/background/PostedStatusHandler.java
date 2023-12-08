package edu.byu.cs.tweeter.server.lambda.background;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.DynamoDaoFactory;
import edu.byu.cs.tweeter.server.sqs.JsonSerializer;
import edu.byu.cs.tweeter.server.sqs.messages.PostedStatusMessage;
import edu.byu.cs.tweeter.server.service.TaskService;

public class PostedStatusHandler implements RequestHandler<SQSEvent, Void> {
    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        TaskService service = new TaskService(new DynamoDaoFactory());
        for (SQSEvent.SQSMessage messageData : event.getRecords()) {
            PostedStatusMessage message = JsonSerializer.deserialize(messageData.getBody(), PostedStatusMessage.class);
            service.scheduleUpdateFeedsJobs(message.getStatus());
        }
        return null;
    }
}
