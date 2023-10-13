package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class NavigatingPresenter<ViewType extends NavigatingPresenter.View> extends BasePresenter<ViewType> {
    protected final UserService userService = new UserService();

    public NavigatingPresenter(ViewType view) {
        super(view);
    }

    public interface View extends BasePresenter.View {
        void setCurrentUser(User user);
    }

    protected class NavigateToUserObserver extends ServiceResultObserver<User> {
        public NavigateToUserObserver() {
            super("navigate to user");
        }

        @Override
        public void onResultLoaded(User user) {
            view.setCurrentUser(user);
        }
    }

    public void loadUserProfile(String userAlias) {
        view.displayMessage("Getting user's profile...");
        AuthToken authToken = Cache.getInstance().getCurrUserAuthToken();
        userService.loadUser(authToken, userAlias, new NavigateToUserObserver());
    }
}
