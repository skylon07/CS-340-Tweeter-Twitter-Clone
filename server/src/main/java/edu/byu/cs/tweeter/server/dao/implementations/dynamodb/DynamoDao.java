package edu.byu.cs.tweeter.server.dao.implementations.dynamodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public abstract class DynamoDao {
    protected static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
        .region(Region.US_WEST_1)
        .build();

    protected static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
        .dynamoDbClient(dynamoDbClient)
        .build();

    protected void setPageRequestStartKey(QueryEnhancedRequest.Builder requestBuilder, String keyAttr, String key, String pageItemAttr, Object lastPageItem, boolean lastItemIsNumber) {
        Map<String, AttributeValue> startKey = new HashMap<>();
        startKey.put(keyAttr, AttributeValue.builder().s(key).build());
        if (lastItemIsNumber) {
            if (lastPageItem == null) throw new RuntimeException("AAAAAAAAAHHHHH");
            startKey.put(pageItemAttr, AttributeValue.builder().n(lastPageItem.toString()).build());
        } else {
            String lastPageItemString = (String) lastPageItem;
            startKey.put(pageItemAttr, AttributeValue.builder().s(lastPageItemString).build());
        }
        requestBuilder.exclusiveStartKey(startKey);
    }

    protected <ResultT, PageT> Pair<List<ResultT>, Boolean> convertPages (PageIterable<PageT> pages, PageMapper<PageT, ResultT> mapper) {
        List<ResultT> results = new ArrayList<>();
        AtomicBoolean hasMorePages = new AtomicBoolean(false);
        pages.stream()
            .limit(1)
            .forEach((page) -> {
                hasMorePages.set(page.lastEvaluatedKey() != null);
                page.items().forEach((item) -> results.add(mapper.mapPageItem(item)));
            });
        return new Pair<>(results, hasMorePages.get());
    }

    protected <PageT> Integer countPageItems(PageIterable<PageT> pages) {
        AtomicReference<Integer> count = new AtomicReference<>(0);
        pages.stream()
            .forEach((page) -> {
               page.items().forEach((item) -> count.set(count.get() + 1));
            });
        return count.get();
    }

    protected interface PageMapper<PageT, ResultT> {
        ResultT mapPageItem(PageT item);
    }
}
