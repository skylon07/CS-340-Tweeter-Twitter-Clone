package edu.byu.cs.tweeter.server.dao.interfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.beans.UserBean;

public interface UserDao {
    User createUser(String firstName, String lastName, String username, String image);
    void createUserBatch(List<UserBean> users);
    User getUser(String username);
    User getUserForLogin(String username);
    boolean isValidPassword(String username, String password);
    void savePassword(String username, String password);
}
