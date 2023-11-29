package edu.byu.cs.tweeter.server.dao.implementations.dynamodb.beans;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class StatusBean {
    private String userAlias;
    private Long timestamp;
    private String posterAlias;
    private String posterFirstName;
    private String posterLastName;
    private String posterImageUrl;
    private String post;
    private List<String> urls;
    private List<String> mentions;

    public StatusBean() {}

    public StatusBean(String userAlias, Long timestamp, User poster, String post, List<String> urls, List<String> mentions) {
        this.userAlias = userAlias;
        this.timestamp = timestamp;
        this.posterAlias = poster.getAlias();
        this.posterFirstName = poster.getFirstName();
        this.posterLastName = poster.getLastName();
        this.posterImageUrl = poster.getImageUrl();
        this.post = post;
        this.urls = urls;
        this.mentions = mentions;
    }

    @DynamoDbPartitionKey
    public String getUserAlias() { return userAlias; }
    public void setUserAlias(String userAlias) { this.userAlias = userAlias; }

    @DynamoDbSortKey
    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }

    public String getPosterAlias() { return posterAlias; }
    public void setPosterAlias(String posterAlias) { this.posterAlias = posterAlias; }

    public String getPosterFirstName() { return posterFirstName; }
    public void setPosterFirstName(String posterFirstName) { this.posterFirstName = posterFirstName; }

    public String getPosterLastName() { return posterLastName; }
    public void setPosterLastName(String posterLastName) { this.posterLastName = posterLastName; }

    public String getPosterImageUrl() { return posterImageUrl; }
    public void setPosterImageUrl(String posterImageUrl) { this.posterImageUrl = posterImageUrl; }

    public String getPost() { return post; }
    public void setPost(String post) { this.post = post; }

    public List<String> getUrls() { return urls; }
    public void setUrls(List<String> urls) { this.urls = urls; }

    public List<String> getMentions() { return mentions; }
    public void setMentions(List<String> mentions) { this.mentions = mentions; }

    public Status asStatus() {
        User poster = new User(posterFirstName, posterLastName, posterAlias, posterImageUrl);
        return new Status(post, poster, timestamp, urls, mentions);
    }
}
