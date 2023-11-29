package edu.byu.cs.tweeter.server.dao.implementations.dynamodb;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.beans.FollowBean;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDao;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

public class DynamoFollowDao extends DynamoDao implements FollowDao {
    public static final String followTableName = "Tweeter-follows";
    public static final String followIndexName = "Tweeter-follows-index";

    private static final String followerAliasKeyAttr = "followerAlias";
    private static final String followeeAliasKeyAttr = "followeeAlias";

    private static final DynamoDbTable<FollowBean> followsTable = enhancedClient.table(followTableName, TableSchema.fromBean(FollowBean.class));
    private static final DynamoDbIndex<FollowBean> followsIndex = followsTable.index(followIndexName);

    @Override
    public Pair<List<User>, Boolean> getFollowees(String followerAlias, int limit, String lastFolloweeAlias) {
        Key key = Key.builder()
            .partitionValue(followerAlias)
            .build();
        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
            .queryConditional(QueryConditional.keyEqualTo(key))
            .limit(limit);
        if (lastFolloweeAlias != null) {
            setPageRequestStartKey(
                requestBuilder,
                followerAliasKeyAttr, followerAlias,
                followeeAliasKeyAttr, lastFolloweeAlias,
                false
            );
        }
        QueryEnhancedRequest request = requestBuilder.build();
        PageIterable<FollowBean> pages = PageIterable.create(followsTable.query(request));
        return convertPages(pages, FollowBean::asFollowee);
    }

    @Override
    public Integer getFolloweeCount(String followerAlias) {
        Key key = Key.builder()
            .partitionValue(followerAlias)
            .build();
        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
            .queryConditional(QueryConditional.keyEqualTo(key))
            .build();
        PageIterable<FollowBean> pages = PageIterable.create(followsTable.query(request));
        return countPageItems(pages);
    }

    @Override
    public Pair<List<User>, Boolean> getFollowers(String followeeAlias, int limit, String lastFollowerAlias) {
        Key key = Key.builder()
            .partitionValue(followeeAlias)
            .build();
        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
            .queryConditional(QueryConditional.keyEqualTo(key))
            .limit(limit);
        if (lastFollowerAlias != null) {
            setPageRequestStartKey(
                requestBuilder,
                followeeAliasKeyAttr, followeeAlias,
                followerAliasKeyAttr, lastFollowerAlias,
                false
            );
        }
        QueryEnhancedRequest request = requestBuilder.build();
        PageIterable<FollowBean> pages = PageIterable.create(followsIndex.query(request));
        return convertPages(pages, FollowBean::asFollower);
    }

    @Override
    public Integer getFollowerCount(String followeeAlias) {
        Key key = Key.builder()
            .partitionValue(followeeAlias)
            .build();
        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
            .queryConditional(QueryConditional.keyEqualTo(key))
            .build();
        PageIterable<FollowBean> pages = PageIterable.create(followsIndex.query(request));
        return countPageItems(pages);
    }

    @Override
    public boolean isFollowing(String followerAlias, String followeeAlias) {
        Key key = Key.builder()
            .partitionValue(followerAlias)
            .sortValue(followeeAlias)
            .build();
        return followsTable.getItem(key) != null;
    }

    @Override
    public void recordFollow(User follower, User followee) {
        FollowBean newBean = new FollowBean(follower, followee);
        followsTable.putItem(newBean);
    }

    @Override
    public void removeFollow(String followerAlias, String followeeAlias) {
        Key key = Key.builder()
            .partitionValue(followerAlias)
            .sortValue(followeeAlias)
            .build();
        followsTable.deleteItem(key);
    }
}
