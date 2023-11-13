package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.response.Response;

public abstract class NetworkTask extends BackgroundTask {
    protected NetworkTask(Handler messageHandler) {
        super(messageHandler);
    }

    @Override
    protected void runTask() {
        try {
            Response response = callApi();
            if (response.isSuccess()) {
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getErrorMessage());
            }
        } catch (Exception exception) {
            sendExceptionMessage(exception);
        }
    }

    protected abstract Response callApi() throws IOException, TweeterRemoteException;
}
