package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.observer.ResultObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.util.Pair;

public class StatusPageHandler
        extends ResultHandler<Pair<List<Status>, Boolean>, ResultObserver<Pair<List<Status>, Boolean>>> {
    public StatusPageHandler(ResultObserver<Pair<List<Status>, Boolean>> observer) {
        super(observer);
    }

    @Override
    protected Pair<List<Status>, Boolean> getResult(Bundle data) {
        return new Pair<>(
            (List<Status>) data.getSerializable(PagedTask.ITEMS_KEY),
            data.getBoolean(PagedTask.MORE_PAGES_KEY)
        );
    }

}
