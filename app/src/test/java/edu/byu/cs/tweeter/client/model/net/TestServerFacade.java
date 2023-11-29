package edu.byu.cs.tweeter.client.model.net;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.PagedRequestByLong;
import edu.byu.cs.tweeter.model.net.request.PagedRequestByString;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserTargetedRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.model.net.response.UsersResponse;

public class TestServerFacade {
    ServerFacade facadeSpy;
    ClientCommunicator communicatorSpy;

    @BeforeEach
    public void setup() {
        communicatorSpy = Mockito.spy(new ClientCommunicator(ServerFacade.SERVER_URL));
        facadeSpy = Mockito.spy(new ServerFacade(communicatorSpy));
    }

    @Test
    public void testRegister() throws IOException, TweeterRemoteException {
        RegisterRequest request = new RegisterRequest("TestUser1", "TheTester1", "@testusername1", "testpassword1", "TEST_IMAGE");
        Response response = facadeSpy.register(request);

        Mockito.verify(communicatorSpy, Mockito.times(1)).doPost(Mockito.eq("/register"), Mockito.any(RegisterRequest.class), Mockito.eq(null), Mockito.eq(LoginResponse.class));
        assert response.isSuccess();
    }

    @Test
    public void testRegisterOnMissingParams() throws IOException, TweeterRemoteException {
        Assertions.assertThrows(TweeterRequestException.class, () -> {
            RegisterRequest request = new RegisterRequest(null, "TheTester1", "@testusername1", "testpassword1", "TEST_IMAGE");
            facadeSpy.register(request);
        });
        Mockito.verify(communicatorSpy, Mockito.times(1)).doPost(Mockito.eq("/register"), Mockito.any(RegisterRequest.class), Mockito.eq(null), Mockito.eq(LoginResponse.class));
        Mockito.reset(communicatorSpy);

        Assertions.assertThrows(TweeterRequestException.class, () -> {
            RegisterRequest request = new RegisterRequest("TestUser1", null, "@testusername1", "testpassword1", "TEST_IMAGE");
            facadeSpy.register(request);
        });
        Mockito.verify(communicatorSpy, Mockito.times(1)).doPost(Mockito.eq("/register"), Mockito.any(RegisterRequest.class), Mockito.eq(null), Mockito.eq(LoginResponse.class));
        Mockito.reset(communicatorSpy);

        Assertions.assertThrows(TweeterRequestException.class, () -> {
            RegisterRequest request = new RegisterRequest("TestUser1", "TheTester1", null, "testpassword1", "TEST_IMAGE");
            facadeSpy.register(request);
        });
        Mockito.verify(communicatorSpy, Mockito.times(1)).doPost(Mockito.eq("/register"), Mockito.any(RegisterRequest.class), Mockito.eq(null), Mockito.eq(LoginResponse.class));
        Mockito.reset(communicatorSpy);

        Assertions.assertThrows(TweeterRequestException.class, () -> {
            RegisterRequest request = new RegisterRequest("TestUser1", "TheTester1", "@testusername1", null, "TEST_IMAGE");
            facadeSpy.register(request);
        });
        Mockito.verify(communicatorSpy, Mockito.times(1)).doPost(Mockito.eq("/register"), Mockito.any(RegisterRequest.class), Mockito.eq(null), Mockito.eq(LoginResponse.class));
        Mockito.reset(communicatorSpy);

        Assertions.assertThrows(TweeterRequestException.class, () -> {
            RegisterRequest request = new RegisterRequest(null, null, null, null, null);
            facadeSpy.register(request);
        });
        Mockito.verify(communicatorSpy, Mockito.times(1)).doPost(Mockito.eq("/register"), Mockito.any(RegisterRequest.class), Mockito.eq(null), Mockito.eq(LoginResponse.class));
        Mockito.reset(communicatorSpy);
    }

    @Test
    public void testGetFollowers() throws IOException, TweeterRemoteException {
        PagedRequestByString request = new PagedRequestByString(getDummyToken(), "@testusername1", 5, null);
        UsersResponse response = facadeSpy.getFollowers(request);

        Mockito.verify(communicatorSpy, Mockito.times(1)).doPost(Mockito.eq("/getfollowers"), Mockito.any(PagedRequest.class), Mockito.eq(null), Mockito.eq(UsersResponse.class));
        assert response.isSuccess();
        assert response.getResults().size() == 5;

        User lastUser = response.getResults().get(4);
        request = new PagedRequestByString(getDummyToken(), "@testusername1", 5, lastUser.getAlias());
        UsersResponse response2 = facadeSpy.getFollowers(request);

        Mockito.verify(communicatorSpy, Mockito.times(2)).doPost(Mockito.eq("/getfollowers"), Mockito.any(PagedRequest.class), Mockito.eq(null), Mockito.eq(UsersResponse.class));
        assert response2.isSuccess();
        assert response2.getResults().size() == 5;
        for (int userIdx = 0; userIdx < 5; ++userIdx) {
            // not necessarily checking for different data -- just different objects
            assert response.getResults().get(userIdx) != response2.getResults().get(userIdx);
        }
    }

    @Test
    public void testGetFollowersMissingParams() throws IOException, TweeterRemoteException {
        Assertions.assertThrows(TweeterRequestException.class, () -> {
            PagedRequestByString request = new PagedRequestByString(null, "@testusername1", 5, null);
            facadeSpy.getFollowers(request);
        });
        Mockito.verify(communicatorSpy, Mockito.times(1)).doPost(Mockito.eq("/getfollowers"), Mockito.any(PagedRequestByString.class), Mockito.eq(null), Mockito.eq(UsersResponse.class));
        Mockito.reset(communicatorSpy);

        Assertions.assertThrows(TweeterRequestException.class, () -> {
            PagedRequestByString request = new PagedRequestByString(getDummyToken(), null, 5, null);
            facadeSpy.getFollowers(request);
        });
        Mockito.verify(communicatorSpy, Mockito.times(1)).doPost(Mockito.eq("/getfollowers"), Mockito.any(PagedRequestByString.class), Mockito.eq(null), Mockito.eq(UsersResponse.class));
        Mockito.reset(communicatorSpy);

        Assertions.assertThrows(TweeterRequestException.class, () -> {
            PagedRequestByString request = new PagedRequestByString(getDummyToken(), "@testusername1", null, null);
            facadeSpy.getFollowers(request);
        });
        Mockito.verify(communicatorSpy, Mockito.times(1)).doPost(Mockito.eq("/getfollowers"), Mockito.any(PagedRequestByString.class), Mockito.eq(null), Mockito.eq(UsersResponse.class));
        Mockito.reset(communicatorSpy);

        Assertions.assertThrows(TweeterRequestException.class, () -> {
            PagedRequestByString request = new PagedRequestByString(null, null, null, null);
            facadeSpy.getFollowers(request);
        });
        Mockito.verify(communicatorSpy, Mockito.times(1)).doPost(Mockito.eq("/getfollowers"), Mockito.any(PagedRequestByString.class), Mockito.eq(null), Mockito.eq(UsersResponse.class));
        Mockito.reset(communicatorSpy);
    }

    @Test
    public void testGetFollowerCount() throws IOException, TweeterRemoteException {
        UserTargetedRequest request = new UserTargetedRequest(getDummyToken(), "@testusername1");
        Response response = facadeSpy.countFollowers(request);

        Mockito.verify(communicatorSpy, Mockito.times(1)).doPost(Mockito.eq("/countfollowers"), Mockito.any(UserTargetedRequest.class), Mockito.eq(null), Mockito.eq(CountResponse.class));
        assert response.isSuccess();
    }

    @Test
    public void testGetFollowerCountMissingParams() throws IOException, TweeterRemoteException {
        Assertions.assertThrows(TweeterRequestException.class, () -> {
            UserTargetedRequest request = new UserTargetedRequest(null, "@testusername1");
            facadeSpy.countFollowers(request);
        });
        Mockito.verify(communicatorSpy, Mockito.times(1)).doPost(Mockito.eq("/countfollowers"), Mockito.any(UserTargetedRequest.class), Mockito.eq(null), Mockito.eq(CountResponse.class));
        Mockito.reset(communicatorSpy);

        Assertions.assertThrows(TweeterRequestException.class, () -> {
            UserTargetedRequest request = new UserTargetedRequest(getDummyToken(), null);
            facadeSpy.countFollowers(request);
        });
        Mockito.verify(communicatorSpy, Mockito.times(1)).doPost(Mockito.eq("/countfollowers"), Mockito.any(UserTargetedRequest.class), Mockito.eq(null), Mockito.eq(CountResponse.class));
        Mockito.reset(communicatorSpy);

        Assertions.assertThrows(TweeterRequestException.class, () -> {
            UserTargetedRequest request = new UserTargetedRequest(null, null);
            facadeSpy.countFollowers(request);
        });
        Mockito.verify(communicatorSpy, Mockito.times(1)).doPost(Mockito.eq("/countfollowers"), Mockito.any(UserTargetedRequest.class), Mockito.eq(null), Mockito.eq(CountResponse.class));
        Mockito.reset(communicatorSpy);
    }

    @Test
    public void testGetStory() throws IOException, TweeterRemoteException {
        PagedRequestByLong request = new PagedRequestByLong(getDummyToken(), "@testusername1", 5, null);
        PagedResponse<Status> response = facadeSpy.getStory(request);

        Mockito.verify(communicatorSpy, Mockito.times(1)).doPost(Mockito.eq("/getstory"), Mockito.any(PagedRequest.class), Mockito.eq(null), Mockito.eq(StatusesResponse.class));
        assert response.isSuccess();
        assert response.getResults().size() == 5;

        Status lastStatus = response.getResults().get(4);
        request = new PagedRequestByLong(getDummyToken(), "@testusername1", 5, lastStatus);
        PagedResponse<Status> response2 = facadeSpy.getStory(request);

        Mockito.verify(communicatorSpy, Mockito.times(2)).doPost(Mockito.eq("/getstory"), Mockito.any(PagedRequest.class), Mockito.eq(null), Mockito.eq(StatusesResponse.class));
        assert response2.isSuccess();
        assert response2.getResults().size() == 5;
        for (int userIdx = 0; userIdx < 5; ++userIdx) {
            // not necessarily checking for different data -- just different objects
            assert response.getResults().get(userIdx) != response2.getResults().get(userIdx);
        }
    }

    private AuthToken getDummyToken() {
        return new AuthToken("test_token");
    }
}
