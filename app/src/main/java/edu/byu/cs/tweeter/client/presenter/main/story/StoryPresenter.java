package edu.byu.cs.tweeter.client.presenter.main.story;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.PagingPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagingPresenter<Status, StoryPresenter.View<Status>> {
    private final StatusService statusService = new StatusService();

    public StoryPresenter(View<Status> view) {
        super(view);
    }

    public void onStoryItemClick(String userAlias) {
        loadUserProfile(userAlias);
    }

    public void onMentionClick(String userAlias) {
        loadUserProfile(userAlias);
    }

    public void onScrolled(User user) {
        loadMoreItems(user);
    }

    @Override
    protected void loadItemsFromService(User user, AuthToken authToken) {
        statusService.loadStory(authToken, user, PAGE_SIZE, getLastItem(), new PagingServiceObserver("get story"));
    }
}
