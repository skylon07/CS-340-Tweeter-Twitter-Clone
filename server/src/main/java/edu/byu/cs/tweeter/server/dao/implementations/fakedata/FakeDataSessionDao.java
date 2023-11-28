package edu.byu.cs.tweeter.server.dao.implementations.fakedata;

import java.util.stream.Stream;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.interfaces.SessionDao;

public class FakeDataSessionDao extends FakeDataDao implements SessionDao {
    @Override
    public void createSession(String username, AuthToken token) {
        return; // intentionally left blank (nothing to "save" on creation)
    }

    @Override
    public String getAssociatedUsername(AuthToken token) {
        return getFakeData().getFirstUser().getAlias();
    }

    @Override
    public boolean revokeSession(AuthToken token) {
        return true; // intentionally left blank (nothing to "revoke")
    }

    @Override
    public AuthToken updateTimestamp(AuthToken token) {
        token.setTimestamp(System.currentTimeMillis());
        return token;
    }

    @Override
    public void saveUpdatedTimestamp(AuthToken token) {
        return; // intentionally left blank (nothing to "save")
    }

    @Override
    public Stream<AuthToken> getAuthTokens() {
        return Stream.of(getFakeData().getAuthToken());
    }
}
