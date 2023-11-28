package edu.byu.cs.tweeter.server.dao.interfaces;

import edu.byu.cs.tweeter.model.domain.User;

public interface UserDao {
    User createUser(String firstName, String lastName, String username, String image);
    User getUser(String username);
    User getUserForLogin(String username);
    boolean isValidPassword(String username, String password);
    void savePassword(String username, String password);
}
