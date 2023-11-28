package edu.byu.cs.tweeter.server.dao.interfaces;

import java.util.stream.Stream;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public interface SessionDao {
    void createSession(String username, AuthToken token);
    String getAssociatedUsername(AuthToken token);
    void revokeSession(AuthToken token);
    AuthToken updateTimestamp(AuthToken token);
    void saveUpdatedTimestamp(AuthToken token);
    Stream<AuthToken> getAuthTokens();
}
