package edu.byu.cs.tweeter.client.presenter.main.followers;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.PagingPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagingPresenter<User, FollowersPresenter.View<User>> {
    private final FollowService followService = new FollowService();
    private final UserService userService = new UserService();

    public FollowersPresenter(View<User> view) {
        super(view);
    }

    public void onFollowerItemClick(String userAlias) {
        loadUserProfile(userAlias);
    }

    public void onScrolled(User user) {
        loadMoreItems(user);
    }

    public void loadUserProfile(String userAlias) {
        view.displayMessage("Getting user's profile...");
        AuthToken authToken = Cache.getInstance().getCurrUserAuthToken();
        userService.loadUser(authToken, userAlias, new NavigateToUserObserver());
    }

    @Override
    protected void loadItemsFromService(User user, AuthToken authToken) {
        followService.loadFollowers(authToken, user, PAGE_SIZE, getLastItem(), new PagingServiceObserver("get follower"));
    }
}
