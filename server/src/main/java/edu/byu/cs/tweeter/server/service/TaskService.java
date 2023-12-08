package edu.byu.cs.tweeter.server.service;

import java.util.List;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.interfaces.DaoFactory;
import edu.byu.cs.tweeter.server.sqs.SqsClient;
import edu.byu.cs.tweeter.util.Pair;

public class TaskService extends BaseService {
    private static final int FOLLOWER_PAGE_SIZE = 250;

    // TODO: could be abstracted into a factory like the DAOs
    private static final SqsClient sqsClient = new SqsClient();

    public TaskService(DaoFactory daoFactory) {
        super(daoFactory);
    }

    public void scheduleUpdateFeedsJobs(Status newStatus) {
        String posterAlias = newStatus.getUser().getAlias();

        String lastFollowerAlias = null;
        boolean hasMorePages = true;
        while (hasMorePages) {
            Pair<List<User>, Boolean> followerData = getDaos().getFollowDao().getFollowers(posterAlias, FOLLOWER_PAGE_SIZE, lastFollowerAlias);

            List<User> followers = followerData.getFirst();
            List<String> followerAliases = followers.stream().map(User::getAlias).collect(Collectors.toList());
            sqsClient.sendUpdateFeedMessage(followerAliases, newStatus);

            lastFollowerAlias = followerAliases.get(followerAliases.size() - 1);
            hasMorePages = followerData.getSecond();
        }
    }
}
