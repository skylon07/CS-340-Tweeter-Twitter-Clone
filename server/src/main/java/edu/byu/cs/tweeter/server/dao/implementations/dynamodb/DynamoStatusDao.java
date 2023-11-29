package edu.byu.cs.tweeter.server.dao.implementations.dynamodb;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.beans.StatusBean;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDao;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

public class DynamoStatusDao extends DynamoDao implements StatusDao {
    public static final String statusTableName = "Tweeter-statuses";
    public static final String feedTableName = "Tweeter-feeds";

    private static final String userAliasKeyAttr = "userAlias";
    private static final String timestampKeyAttr = "timestamp";

    private static final DynamoDbTable<StatusBean> statusTable = enhancedClient.table(statusTableName, TableSchema.fromBean(StatusBean.class));
    private static final DynamoDbTable<StatusBean> feedTable = enhancedClient.table(feedTableName, TableSchema.fromBean(StatusBean.class));

    @Override
    public Pair<List<Status>, Boolean> getFeed(String targetAlias, Integer limit, Long lastStatusTimestamp) {
        Key key = Key.builder()
            .partitionValue(targetAlias)
            .build();
        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
            .queryConditional(QueryConditional.keyEqualTo(key))
            .scanIndexForward(false)
            .limit(limit);
        if (lastStatusTimestamp != null) {
            setPageRequestStartKey(
                requestBuilder,
                userAliasKeyAttr, targetAlias,
                timestampKeyAttr, lastStatusTimestamp,
                true
            );
        }
        QueryEnhancedRequest request = requestBuilder.build();
        PageIterable<StatusBean> pages = feedTable.query(request);
        return convertPages(pages, StatusBean::asStatus);
    }

    @Override
    public Pair<List<Status>, Boolean> getStory(String targetAlias, Integer limit, Long lastStatusTimestamp) {
        Key key = Key.builder()
            .partitionValue(targetAlias)
            .build();
        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
            .queryConditional(QueryConditional.keyEqualTo(key))
            .scanIndexForward(false)
            .limit(limit);
        if (lastStatusTimestamp != null) {
            setPageRequestStartKey(
                requestBuilder,
                userAliasKeyAttr, targetAlias,
                timestampKeyAttr, lastStatusTimestamp,
                true
            );
        }
        QueryEnhancedRequest request = requestBuilder.build();
        PageIterable<StatusBean> pages = statusTable.query(request);
        return convertPages(pages, StatusBean::asStatus);
    }

    @Override
    public void postStatusToStoryAndFeeds(User poster, List<String> followerAliases, String post, Long timestamp) {
        StatusBean newBean = new StatusBean(poster.getAlias(), timestamp, poster, post, Status.parseURLs(post), Status.parseMentions(post));
        statusTable.putItem(newBean);

        for (String followerAlias : followerAliases) {
            newBean.setUserAlias(followerAlias);
            feedTable.putItem(newBean);
        }
    }
}
