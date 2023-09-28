package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {
    public void loadFollowees(AuthToken authToken, User user, int pageSize, User lastFollowee, LoadItemsObserver observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(
            authToken,
            user,
            pageSize,
            lastFollowee,
            new GetFollowingHandler(observer)
        );
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowingTask);
    }

    public void loadFollowers(AuthToken authToken, User user, int pageSize, User lastFollower, LoadItemsObserver observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(
            authToken,
            user,
            pageSize,
            lastFollower,
            new GetFollowersHandler(observer)
        );
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowersTask);
    }

    public void loadFollowCounts(AuthToken authToken, User user, LoadCountObserver followersObserver, LoadCountObserver followingObserver) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(
            authToken,
            user,
            new GetFollowersCountHandler(followersObserver)
        );
        executor.execute(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(
            authToken,
            user,
            new GetFollowingCountHandler(followingObserver)
        );
        executor.execute(followingCountTask);
    }

    public void doesUserFollow(AuthToken authToken, User userFollowing, User userFollowed, TruthObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken, userFollowing, userFollowed, new IsFollowerHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(isFollowerTask);
    }

    public void requestFollow(AuthToken authToken, User user, TruthObserver observer) {
        FollowTask followTask = new FollowTask(authToken, user, new FollowHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followTask);
    }

    public void requestUnfollow(AuthToken authToken, User user, TruthObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(authToken, user, new UnfollowHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(unfollowTask);
    }

    public interface LoadItemsObserver {
        void onItemsLoaded(List<User> items, boolean hasMorePages);
        void displayError(String message);
        void displayException(Exception ex);
    }

    public interface LoadCountObserver {
        void onCountLoaded(int followersCount);
        void displayError(String message);
        void displayException(Exception ex);
    }

    public interface TruthObserver {
        void onLoaded(boolean truth);
        void displayError(String message);
        void displayException(Exception ex);
    }

    /**
     * Message handler (i.e., observer) for GetFollowingTask.
     */
    private class GetFollowingHandler extends Handler {
        private final LoadItemsObserver observer;

        public GetFollowingHandler(LoadItemsObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowingTask.SUCCESS_KEY);
            if (success) {
                List<User> followees = (List<User>) msg.getData().getSerializable(GetFollowingTask.FOLLOWEES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFollowingTask.MORE_PAGES_KEY);
                observer.onItemsLoaded(followees, hasMorePages);
            } else if (msg.getData().containsKey(GetFollowingTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowingTask.MESSAGE_KEY);
                observer.displayError(message);
            } else if (msg.getData().containsKey(GetFollowingTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingTask.EXCEPTION_KEY);
                observer.displayException(ex);
            }
        }
    }

    /**
     * Message handler (i.e., observer) for GetFollowersTask.
     */
    private class GetFollowersHandler extends Handler {
        private final LoadItemsObserver observer;

        public GetFollowersHandler(LoadItemsObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
            if (success) {
                List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.FOLLOWERS_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);
                observer.onItemsLoaded(followers, hasMorePages);
            } else if (msg.getData().containsKey(GetFollowersTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersTask.MESSAGE_KEY);
                observer.displayError(message);
            } else if (msg.getData().containsKey(GetFollowersTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersTask.EXCEPTION_KEY);
                observer.displayException(ex);
            }
        }
    }

    // GetFollowersCountHandler
    private class GetFollowersCountHandler extends Handler {
        private LoadCountObserver observer;

        public GetFollowersCountHandler(LoadCountObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowersCountTask.SUCCESS_KEY);
            if (success) {
                int count = msg.getData().getInt(GetFollowersCountTask.COUNT_KEY);
                observer.onCountLoaded(count);
            } else if (msg.getData().containsKey(GetFollowersCountTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersCountTask.MESSAGE_KEY);
                observer.displayError(message);
            } else if (msg.getData().containsKey(GetFollowersCountTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersCountTask.EXCEPTION_KEY);
                observer.displayException(ex);
            }
        }
    }

    // GetFollowingCountHandler
    private class GetFollowingCountHandler extends Handler {
        private LoadCountObserver observer;

        public GetFollowingCountHandler(LoadCountObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowingCountTask.SUCCESS_KEY);
            if (success) {
                int count = msg.getData().getInt(GetFollowingCountTask.COUNT_KEY);
                observer.onCountLoaded(count);
            } else if (msg.getData().containsKey(GetFollowingCountTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowingCountTask.MESSAGE_KEY);
                observer.displayError(message);
            } else if (msg.getData().containsKey(GetFollowingCountTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingCountTask.EXCEPTION_KEY);
                observer.displayException(ex);
            }
        }
    }

    // IsFollowerHandler
    private class IsFollowerHandler extends Handler {
        TruthObserver observer;

        public IsFollowerHandler(TruthObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(IsFollowerTask.SUCCESS_KEY);
            if (success) {
                boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
                observer.onLoaded(isFollower);
            } else if (msg.getData().containsKey(IsFollowerTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(IsFollowerTask.MESSAGE_KEY);
                observer.displayError(message);
            } else if (msg.getData().containsKey(IsFollowerTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(IsFollowerTask.EXCEPTION_KEY);
                observer.displayException(ex);
            }
        }
    }

    // FollowHandler
    private class FollowHandler extends Handler {
        private TruthObserver observer; // could just be a parameter-less callback...

        public FollowHandler(TruthObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
            if (success) {
                observer.onLoaded(true);
            } else if (msg.getData().containsKey(FollowTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(FollowTask.MESSAGE_KEY);
                observer.displayError(message);
            } else if (msg.getData().containsKey(FollowTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(FollowTask.EXCEPTION_KEY);
                observer.displayException(ex);
            }
        }
    }

    // UnfollowHandler
    private class UnfollowHandler extends Handler {
        private TruthObserver observer; // could just be a parameter-less callback...

        public UnfollowHandler(TruthObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(UnfollowTask.SUCCESS_KEY);
            if (success) {
                observer.onLoaded(true);
            } else if (msg.getData().containsKey(UnfollowTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(UnfollowTask.MESSAGE_KEY);
                observer.displayError(message);
            } else if (msg.getData().containsKey(UnfollowTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(UnfollowTask.EXCEPTION_KEY);
                observer.displayException(ex);
            }
        }
    }
}
