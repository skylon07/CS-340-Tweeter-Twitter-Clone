package edu.byu.cs.tweeter.client.presenter.main.feed;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.PagingPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagingPresenter<Status, FeedPresenter.View<Status>> {

    private final StatusService statusService = new StatusService();
    private final UserService userService = new UserService();

    public FeedPresenter(View<Status> view) {
        super(view);
    }

    public void onFeedItemClick(String userAlias) {
        loadUserProfile(userAlias);
    }

    public void onMentionClick(String userAlias) {
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
        statusService.loadFeed(authToken, user, PAGE_SIZE, getLastItem(), new PagingServiceObserver("get feed"));
    }
}
