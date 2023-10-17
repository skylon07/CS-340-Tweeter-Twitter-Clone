package edu.byu.cs.tweeter.client.presenter.main.followers;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.presenter.PagingPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter
        extends PagingPresenter<User, FollowersPresenter.View<User>> {
    private final FollowService followService = new FollowService();

    public FollowersPresenter(View<User> view) {
        super(view);
    }

    public void onFollowerItemClick(String userAlias) {
        loadUserProfile(userAlias);
    }

    public void onScrolled(User user) {
        loadMoreItems(user);
    }

    @Override
    protected void loadItemsFromService(User user, AuthToken authToken) {
        followService.loadFollowers(authToken, user, PAGE_SIZE, getLastItem(), new PagingServiceObserver("get follower"));
    }
}
