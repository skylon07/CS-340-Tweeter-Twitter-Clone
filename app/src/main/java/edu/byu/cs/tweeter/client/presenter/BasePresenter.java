package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.observer.ResultObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SuccessObserver;

public abstract class BasePresenter<ViewType extends BasePresenter.View> {
    protected final ViewType view;

    public BasePresenter(ViewType view) {
        this.view = view;
    }

    public interface View {
        void displayMessage(String message);
    }

    private abstract class BaseServiceObserver implements ServiceObserver {
        protected final String actionTag;

        public BaseServiceObserver(String actionTag) { this.actionTag = actionTag; }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to " + actionTag + ": " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to " + actionTag + " because of exception: " + exception.getMessage());
        }
    }

    protected abstract class ServiceResultObserver<ResultType> extends BaseServiceObserver implements ResultObserver<ResultType> {
        public ServiceResultObserver(String actionTag) {
            super(actionTag);
        }
    }

    protected abstract class ServiceSuccessObserver extends BaseServiceObserver implements SuccessObserver {
        public ServiceSuccessObserver(String actionTag) {
            super(actionTag);
        }
    }
}
