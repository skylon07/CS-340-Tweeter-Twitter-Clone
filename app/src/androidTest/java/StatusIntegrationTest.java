import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.login.LoginPresenter;
import edu.byu.cs.tweeter.client.presenter.main.MainPresenter;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequestByLong;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;

public class StatusIntegrationTest {
    LoginPresenter loginPresenterSpy;
    LoginPresenter.View loginPresenterViewMock;
    MainPresenter mainPresenterSpy;
    MainPresenter.View mainPresenterViewMock;
    StatusService statusServiceSpy;

    @BeforeEach
    public void setup() {
        mainPresenterViewMock = Mockito.mock(MainPresenter.View.class, "MainPresenter.View");
        loginPresenterViewMock = Mockito.mock(LoginPresenter.View.class, "LoginPresenter.View");

        statusServiceSpy = Mockito.spy(new StatusService());
        loginPresenterSpy = Mockito.spy(new LoginPresenter(loginPresenterViewMock));
        mainPresenterSpy = Mockito.spy(new MainPresenter(mainPresenterViewMock, statusServiceSpy));
    }

    @Test
    public void postStatusTest() {
        // this test assumes login and post status lambdas are already warm
        String USER_ALIAS = "@test2";
        String USER_PASSWORD = "asdf";
        int PAGE_SIZE = 10;

        loginPresenterSpy.onLoginClick(USER_ALIAS, USER_PASSWORD);
        sleepNoThrow(1000);

        int uniqueStatusNumber = (new Random()).nextInt();
        String statusMessage = "This is an automated test post! Its (practically) unique number is " + uniqueStatusNumber;
        mainPresenterSpy.onStatusPosted(statusMessage);
        sleepNoThrow(1000);
        Mockito.verify(mainPresenterViewMock, Mockito.times(1)).displayMessage("Successfully Posted!");

        sleepNoThrow(1000);

        Cache cache = Cache.getInstance();
        ServerFacade serverFacade = new ServerFacade();
        StatusesResponse statusesResponse;
        try {
            statusesResponse = serverFacade.getStory(new PagedRequestByLong(
                cache.getCurrUserAuthToken(),
                USER_ALIAS,
                PAGE_SIZE,
                null
            ));
        } catch (Exception err) {
            throw new AssertionError("No errors should be thrown for valid story request");
        }
        Status lastStatusPosted = statusesResponse.getResults().get(0);
        assert (lastStatusPosted.getPost().equals(statusMessage)) : "Last status in story should be posted status";
    }

    private void sleepNoThrow(long timeMillis) {
        try {
            Thread.sleep(timeMillis);
        } catch (Exception err) {
            throw new AssertionError("Sleeping should not throw");
        }
    }
}
