package edu.byu.cs.tweeter.server.dao.implementations.dynamodb.beans;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.DynamoFollowDao;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class FollowBean {
    private String followerAlias;
    private String followerFirstName;
    private String followerLastName;
    private String followerImageUrl;
    private String followeeAlias;
    private String followeeFirstName;
    private String followeeLastName;
    private String followeeImageUrl;

    public FollowBean() {}

    public FollowBean(User follower, User followee) {
        followerAlias = follower.getAlias();
        followerFirstName = follower.getFirstName();
        followerLastName = follower.getLastName();
        followerImageUrl = follower.getImageUrl();
        followeeAlias = followee.getAlias();
        followeeFirstName = followee.getFirstName();
        followeeLastName = followee.getLastName();
        followeeImageUrl = followee.getImageUrl();
    }

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = DynamoFollowDao.followIndexName)
    public String getFollowerAlias() { return followerAlias; }
    public void setFollowerAlias(String followerAlias) { this.followerAlias = followerAlias; }

    public String getFollowerFirstName() { return followerFirstName; }
    public void setFollowerFirstName(String followerFirstName) { this.followerFirstName = followerFirstName; }

    public String getFollowerLastName() { return followerLastName; }
    public void setFollowerLastName(String followerLastName) { this.followerLastName = followerLastName; }

    public String getFollowerImageUrl() { return followerImageUrl; }
    public void setFollowerImageUrl(String followerImageUrl) { this.followerImageUrl = followerImageUrl; }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = DynamoFollowDao.followIndexName)
    public String getFolloweeAlias() { return followeeAlias; }
    public void setFolloweeAlias(String followeeAlias) { this.followeeAlias = followeeAlias; }

    public String getFolloweeFirstName() { return followeeFirstName; }
    public void setFolloweeFirstName(String followeeFirstName) { this.followeeFirstName = followeeFirstName; }

    public String getFolloweeLastName() { return followeeLastName; }
    public void setFolloweeLastName(String followeeLastName) { this.followeeLastName = followeeLastName; }

    public String getFolloweeImageUrl() { return followeeImageUrl; }
    public void setFolloweeImageUrl(String followeeImageUrl) { this.followeeImageUrl = followeeImageUrl; }

    public User asFollower() {
        return new User(followerFirstName, followerLastName, followerAlias, followerImageUrl);
    }

    public User asFollowee() {
        return new User(followeeFirstName, followeeLastName, followeeAlias, followeeImageUrl);
    }
}
