package edu.byu.cs.tweeter.server.dao.implementations.dynamodb.beans;

import edu.byu.cs.tweeter.model.domain.User;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class UserBean {
    private String firstName;
    private String lastName;
    private String userAlias;
    private String imageUrl;
    private String passwordHash;

    public UserBean() {}

    public UserBean(String firstName, String lastName, String userAlias, String imageUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userAlias = userAlias;
        this.imageUrl = imageUrl;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    @DynamoDbPartitionKey
    public String getUserAlias() { return userAlias; }
    public void setUserAlias(String username) { this.userAlias = username; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public User toUser() {
        return new User(firstName, lastName, userAlias, imageUrl);
    }
}
