package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.observer.ResultObserver;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public class UserPageHandler
        extends ResultHandler<Pair<List<User>, Boolean>, ResultObserver<Pair<List<User>, Boolean>>> {
    public UserPageHandler(ResultObserver<Pair<List<User>, Boolean>> observer) {
        super(observer);
    }

    @Override
    protected Pair<List<User>, Boolean> getResult(Bundle data) {
        return new Pair<>(
            (List<User>) data.getSerializable(PagedTask.ITEMS_KEY),
            data.getBoolean(PagedTask.MORE_PAGES_KEY)
        );
    }
}
