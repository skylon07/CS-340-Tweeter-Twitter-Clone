package edu.byu.cs.tweeter.server.dao.implementations.dynamodb;

import edu.byu.cs.tweeter.server.dao.interfaces.DaoFactory;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDao;
import edu.byu.cs.tweeter.server.dao.interfaces.SessionDao;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDao;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDao;

public class DynamoDaoFactory implements DaoFactory {
    DynamoFollowDao followDao = null;
    DynamoSessionDao sessionDao = null;
    DynamoStatusDao statusDao = null;
    DynamoUserDao userDao = null;

    @Override
    public FollowDao getFollowDao() {
        if (followDao == null) {
            followDao = new DynamoFollowDao();
        }
        return followDao;
    }

    @Override
    public SessionDao getSessionDao() {
        if (sessionDao == null) {
            sessionDao = new DynamoSessionDao();
        }
        return sessionDao;
    }

    @Override
    public StatusDao getStatusDao() {
        if (statusDao == null) {
            statusDao = new DynamoStatusDao();
        }
        return statusDao;
    }

    @Override
    public UserDao getUserDao() {
        if (userDao == null) {
            userDao = new DynamoUserDao();
        }
        return userDao;
    }
}
