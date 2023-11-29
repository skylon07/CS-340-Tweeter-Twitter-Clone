package edu.byu.cs.tweeter.server.dao.implementations.dynamodb;

import edu.byu.cs.tweeter.server.dao.interfaces.DaoFactory;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDao;
import edu.byu.cs.tweeter.server.dao.interfaces.SessionDao;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDao;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDao;

public class DynamoDaoFactory implements DaoFactory {
    DynamoFollowDao followDao = new DynamoFollowDao();
    DynamoSessionDao sessionDao = new DynamoSessionDao();
    DynamoStatusDao statusDao = new DynamoStatusDao();
    DynamoUserDao userDao = new DynamoUserDao();

    @Override
    public FollowDao getFollowDao() {
        return followDao;
    }

    @Override
    public SessionDao getSessionDao() {
        return sessionDao;
    }

    @Override
    public StatusDao getStatusDao() {
        return statusDao;
    }

    @Override
    public UserDao getUserDao() {
        return userDao;
    }
}
