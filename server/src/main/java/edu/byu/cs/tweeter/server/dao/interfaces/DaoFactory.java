package edu.byu.cs.tweeter.server.dao.interfaces;

public interface DaoFactory {
    FollowDao getFollowDao();
    SessionDao getSessionDao();
    StatusDao getStatusDao();
    UserDao getUserDao();
}
