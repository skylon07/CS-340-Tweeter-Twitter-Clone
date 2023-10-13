package edu.byu.cs.tweeter.client.presenter.main.following;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenter.PagingPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;

public class FollowingPresenter extends PagingPresenter<User, FollowingPresenter.View<User>> {
    private final FollowService followService = new FollowService();
    private final UserService userService = new UserService();


    public FollowingPresenter(View<User> view) {
        super(view);
    }

    public void onFollowingViewClick(String userAlias) {
        loadUserProfile(userAlias);
    }

    public void onScrolled(User user) {
        loadMoreItems(user);
    }

    @Override
    protected void loadItemsFromService(User user, AuthToken authToken) {
        followService.loadFollowees(authToken, user, PAGE_SIZE, getLastItem(), new PagingServiceObserver("get following"));
    }
}
