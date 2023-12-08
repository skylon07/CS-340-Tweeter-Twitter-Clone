package edu.byu.cs.tweeter.server.sqs;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.sqs.messages.PostedStatusMessage;
import edu.byu.cs.tweeter.server.sqs.messages.UpdateFeedMessage;

public class SqsClient {
    private static final String POSTED_STATUS_QUEUE_URL = "https://sqs.us-west-1.amazonaws.com/305606272069/tweeter-postedstatus";
    private static final String UPDATE_FEED_QUEUE_URL = "https://sqs.us-west-1.amazonaws.com/305606272069/tweeter-updatefeed";

    public void sendPostedStatusMessage(Status status) {
        PostedStatusMessage message = new PostedStatusMessage(status);
        String messageBody = JsonSerializer.serialize(message);
        sendMessage(POSTED_STATUS_QUEUE_URL, messageBody);
    }

    public void sendUpdateFeedMessage(List<String> followerAliases, Status status) {
        UpdateFeedMessage message = new UpdateFeedMessage(status, followerAliases);
        String messageBody = JsonSerializer.serialize(message);
        sendMessage(UPDATE_FEED_QUEUE_URL, messageBody);
    }

    private SendMessageResult sendMessage(String queueUrl, String messageBody) {
        SendMessageRequest send_msg_request = new SendMessageRequest()
            .withQueueUrl(queueUrl)
            .withMessageBody(messageBody);

        AmazonSQS sqs = AmazonSQSClient.builder().withRegion(Regions.US_WEST_1).build();
        return sqs.sendMessage(send_msg_request);
    }
}
