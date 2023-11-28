package edu.byu.cs.tweeter.server.dao.implementations.dynamodb.beans;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class SessionBean {
    private String token;
    private Long timestamp;
    private String username;

    public SessionBean() {}

    public SessionBean(String token, Long timestamp, String username) {
        this.token = token;
        this.timestamp = timestamp;
        this.username = username;
    }

    @DynamoDbPartitionKey
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public AuthToken toAuthToken() {
        return new AuthToken(token, timestamp);
    }
}
