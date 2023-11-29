package edu.byu.cs.tweeter.server.dao.implementations.fakedata;

import edu.byu.cs.tweeter.server.dao.interfaces.DaoFactory;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDao;
import edu.byu.cs.tweeter.server.dao.interfaces.SessionDao;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDao;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDao;

public class FakeDataDaoFactory implements DaoFactory {
    FakeDataFollowDao followDao = new FakeDataFollowDao();
    FakeDataSessionDao sessionDao = new FakeDataSessionDao();
    FakeDataStatusDao statusDao = new FakeDataStatusDao();
    FakeDataUserDao userDao = new FakeDataUserDao();

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
