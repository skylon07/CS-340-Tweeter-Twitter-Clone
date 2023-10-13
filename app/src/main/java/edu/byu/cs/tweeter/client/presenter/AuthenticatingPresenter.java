package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public abstract class AuthenticatingPresenter<ViewType extends AuthenticatingPresenter.View> extends NavigatingPresenter<ViewType> {
    public AuthenticatingPresenter(ViewType view) {
        super(view);
    }

    public interface View extends NavigatingPresenter.View {
        void cancelMessage();
    }

    protected class UserServiceAuthenticationObserver extends ServiceResultObserver<Pair<User, AuthToken>> {
        public UserServiceAuthenticationObserver(String actionTag) {
            super(actionTag);
        }

        @Override
        public void onResultLoaded(Pair<User, AuthToken> result) {
            User user = result.getFirst();
            AuthToken authToken = result.getSecond();

            Cache.getInstance().setCurrUser(user);
            Cache.getInstance().setCurrUserAuthToken(authToken);
            view.setCurrentUser(user);
            view.cancelMessage();
            view.displayMessage("Hello " + user.getName());
        }
    }
}
