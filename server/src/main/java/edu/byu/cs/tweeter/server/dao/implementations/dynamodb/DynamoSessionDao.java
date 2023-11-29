package edu.byu.cs.tweeter.server.dao.implementations.dynamodb;

import java.util.stream.Stream;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.beans.SessionBean;
import edu.byu.cs.tweeter.server.dao.interfaces.SessionDao;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

public class DynamoSessionDao extends DynamoDao implements SessionDao {
    public static final String sessionTableName = "Tweeter-sessions";
    private static final DynamoDbTable<SessionBean> sessionTable = enhancedClient.table(sessionTableName, TableSchema.fromBean(SessionBean.class));

    @Override
    public void createSession(String username, AuthToken token) {
        SessionBean newBean = new SessionBean(token.getToken(), token.getTimestamp(), username);
        sessionTable.putItem(newBean);
    }

    @Override
    public String getAssociatedUsername(AuthToken token) {
        Key key = Key.builder()
            .partitionValue(token.getToken())
            .build();
        SessionBean sessionBean = sessionTable.getItem(key);
        if (sessionBean == null) return null;
        return sessionBean.getUsername();
    }

    @Override
    public void revokeSession(AuthToken token) {
        Key key = Key.builder()
            .partitionValue(token.getToken())
            .build();
        sessionTable.deleteItem(key);
    }

    @Override
    public AuthToken updateTimestamp(AuthToken token) {
        Key key = Key.builder()
            .partitionValue(token.getToken())
            .build();
        SessionBean knownSession = sessionTable.getItem(key);
        if (knownSession != null) {
            token.setTimestamp(knownSession.getTimestamp());
        }
        return token;
    }

    @Override
    public void saveUpdatedTimestamp(AuthToken token) {
        Key key = Key.builder()
            .partitionValue(token.getToken())
            .build();
        SessionBean knownSession = sessionTable.getItem(key);
        knownSession.setTimestamp(token.getTimestamp());
        sessionTable.updateItem(knownSession);
    }

    @Override
    public Stream<AuthToken> getAuthTokens() {
        return sessionTable
            .scan()
            .items()
            .stream()
            .map(SessionBean::asAuthToken);
    }
}
