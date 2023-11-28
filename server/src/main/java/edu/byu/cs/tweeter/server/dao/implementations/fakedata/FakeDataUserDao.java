package edu.byu.cs.tweeter.server.dao.implementations.fakedata;

import java.util.List;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDao;

public class FakeDataUserDao extends FakeDataDao implements UserDao {
    @Override
    public User createUser(String firstName, String lastName, String username, String image) {
        return getUser(username);
    }

    @Override
    public User getUser(String username) {
        List<User> users = getFakeData().getFakeUsers();
        int userIdx =
            users
                .stream()
                .map(User::getAlias)
                .collect(Collectors.toList())
                .indexOf(username);
        return users.get(userIdx);
    }

    @Override
    public User getUserForLogin(String username) {
        return getFakeData().getFirstUser();
    }

    @Override
    public boolean isValidPassword(String username, String password) {
        return true;
    }

    @Override
    public void savePassword(String username, String userHash) {
        return; // intentionally blank
    }
}
