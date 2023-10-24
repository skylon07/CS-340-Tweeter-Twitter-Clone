package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.observer.SuccessObserver;
import edu.byu.cs.tweeter.client.presenter.main.MainPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenterTest {
    StatusService mockService;
    MainPresenter.View mockView;
    Cache mockCache;
    MockedStatic<Cache> mockCacheStatic;
    MainPresenter spyPresenter;
    SuccessObserver spyObserver;

    /**
     * Sets up mocks and spy before each test.
     */
    @BeforeEach
    public void setup() {
        mockService = Mockito.mock(StatusService.class, "StatusService");

        mockView = Mockito.mock(MainPresenter.View.class, "MainPresenter.View");

        mockCache = Mockito.mock(Cache.class, "Cache");
        mockCacheStatic = Mockito.mockStatic(Cache.class);
        mockCacheStatic.when(Cache::getInstance).thenReturn(mockCache);
        Mockito.doReturn(new User("Firstname", "Lastname", "IMAGE_URL")).when(mockCache).getCurrUser();
        Mockito.doReturn(new AuthToken("test token")).when(mockCache).getCurrUserAuthToken();

        spyPresenter = Mockito.spy(new MainPresenter(mockView, mockService));
        spyObserver = Mockito.spy(spyPresenter.new StatusServiceObserver());
        Mockito.doReturn(spyObserver).when(spyPresenter).createStatusObserver();
    }

    @AfterEach
    public void teardown() {
        mockCacheStatic.close();
    }

    @Test
    public void testPresenterCanRequestPosts() {
        spyPresenter.onStatusPosted("Hey guys! What's up?");

        Mockito.verify(mockView, Mockito.times(1)).displayMessage(Mockito.eq("Posting Status..."));
        Mockito.verify(mockService, Mockito.times(1)).createStatus(
            Mockito.any(AuthToken.class),
            Mockito.eq(new User("Firstname", "Lastname", "IMAGE_URL")),
            Mockito.eq("Hey guys! What's up?"),
            Mockito.any(MainPresenter.StatusServiceObserver.class)
        );
    }

    @Test
    public void testPostRequestSucceedsProperly() {
        spyObserver.handleSuccess();

        Mockito.verify(mockView, Mockito.times(1)).cancelMessage();
        Mockito.verify(mockView, Mockito.times(1)).displayMessage(Mockito.eq("Successfully Posted!"));
    }

    @Test
    public void testPostRequestFailsProperlyForFailures() {
        spyObserver.handleFailure("FAILURE MESSAGE");

        Mockito.verify(mockView, Mockito.times(1)).cancelMessage();
        Mockito.verify(mockView, Mockito.times(1)).displayMessage(Mockito.eq("Failed to post status: FAILURE MESSAGE"));
    }

    @Test
    public void testPostRequestFailsProperlyForExceptions() {
        spyObserver.handleException(new Exception("FAILURE MESSAGE"));

        Mockito.verify(mockView, Mockito.times(1)).cancelMessage();
        Mockito.verify(mockView, Mockito.times(1)).displayMessage(Mockito.eq("Failed to post status because of exception: FAILURE MESSAGE"));
    }
}
