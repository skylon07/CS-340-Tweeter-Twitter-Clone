package edu.byu.cs.tweeter.server.dao.implementations.fakedata;

import edu.byu.cs.tweeter.util.FakeData;

abstract class FakeDataDao {
    /**
     * Returns the {@link FakeData} object used to generate dummy users and auth tokens.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
