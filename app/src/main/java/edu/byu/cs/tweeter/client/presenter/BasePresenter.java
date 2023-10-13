package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.observer.ResultObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SuccessObserver;

public abstract class BasePresenter {
    protected View view;

    public BasePresenter(View view) {
        this.view = view;
    }

    public interface View {
        void displayMessage(String message);
    }

    protected abstract class ServiceResultObserver<ResultType> implements ResultObserver<ResultType> {
        private final String actionTag;

        public ServiceResultObserver(String actionTag) {
            this.actionTag = actionTag;
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to " + actionTag + ": " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to " + actionTag + " because of exception: " + exception.getMessage());
        }
    }

    protected abstract class ServiceSuccessObserver implements SuccessObserver {
        private final String actionTag;

        public ServiceSuccessObserver(String actionTag) {
            this.actionTag = actionTag;
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to " + actionTag + ": " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to " + actionTag + " because of exception: " + exception.getMessage());
        }
    }
}
