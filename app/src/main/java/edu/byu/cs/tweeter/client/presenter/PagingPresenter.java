package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public abstract class PagingPresenter<ItemType, ViewType extends PagingPresenter.View<ItemType>> extends NavigatingPresenter<ViewType> {
    public static final int PAGE_SIZE = 10;

    private ItemType lastItem;
    private boolean hasMorePages;
    private boolean isLoading;

    public PagingPresenter(ViewType view) {
        super(view);
    }

    public boolean isLoading() {
            return isLoading;
        }

    public boolean hasMorePages() {
            return hasMorePages;
}

    public ItemType getLastItem() {
        return lastItem;
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooterVisible(true);
            AuthToken authToken = Cache.getInstance().getCurrUserAuthToken();
            loadItemsFromService(user, authToken);
        }
    }

    protected abstract void loadItemsFromService(User user, AuthToken authToken);

    public interface View<ItemType> extends NavigatingPresenter.View {
        void addItems(List<ItemType> items);
        void setLoadingFooterVisible(boolean visible);
    }

    protected class PagingServiceObserver extends ServiceResultObserver<Pair<List<ItemType>, Boolean>> {
        public PagingServiceObserver(String actionTag) {
            super(actionTag);
        }

        @Override
        public void onResultLoaded(Pair<List<ItemType>, Boolean> result) {
            List<ItemType> items = result.getFirst();
            Boolean hasMorePages = result.getSecond();

            isLoading = false;
            view.setLoadingFooterVisible(false);
            PagingPresenter.this.hasMorePages = hasMorePages;
            lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
            view.addItems(items);
        }

        @Override
        public void handleFailure(String message) {
            super.handleFailure(message);
            isLoading = false;
            view.setLoadingFooterVisible(false);
        }

        @Override
        public void handleException(Exception exception) {
            super.handleException(exception);
            isLoading = false;
            view.setLoadingFooterVisible(false);
        }
    }
}
