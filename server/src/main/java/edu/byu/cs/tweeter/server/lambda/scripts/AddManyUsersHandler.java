package edu.byu.cs.tweeter.server.lambda.scripts;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.Request;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.DynamoDaoFactory;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.beans.UserBean;
import edu.byu.cs.tweeter.server.dao.interfaces.DaoFactory;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDao;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDao;

public class AddManyUsersHandler implements RequestHandler<Request, Response> {
    // How many follower users to add
    // We recommend you test this with a smaller number first, to make sure it works for you
    private final static int NUM_USERS = 2000; // (for 10,000 users/follows, I needed like 500 write capacity units! geez!)
    private final static int START_OFFSET = 8000;

    // The alias of the user to be followed by each user created
    // This example code does not add the target user, that user must be added separately.
    private final static User FOLLOW_TARGET = new User(
        "Test",
        "User",
        "@test",
        "https://tweeter-images-18671.s3.us-west-1.amazonaws.com/@test"
    );

    @Override
    public Response handleRequest(Request request, Context context) {
        fillDatabase();
        return new Response();
    }

    public static void fillDatabase() {
        DaoFactory factory = new DynamoDaoFactory();

        // Get instance of DAOs by way of the Abstract Factory Pattern
        UserDao userDao = factory.getUserDao();
        FollowDao followDao = factory.getFollowDao();

        List<User> followers = new ArrayList<>();
        List<UserBean> users = new ArrayList<>();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String userHash = encoder.encode("armyguy");

        // Iterate over the number of users you will create
        for (int i = 1 + START_OFFSET; i <= START_OFFSET + NUM_USERS; i++) {
            String firstName = "Army" + i;
            String lastName = "Guy" + i;
            String alias = "@army" + i;
            // (image of an army guy -- I know I'm so funny)
            String imageUrl = "https://cdn11.bigcommerce.com/s-s0bffw770m/images/stencil/1280x1280/products/15649/19934/tysm7909_1__29272.1618328422.jpg?c=2";

            // Note that in this example, a UserDTO only has a name and an alias.
            // The url for the profile image can be derived from the alias in this example
            UserBean user = new UserBean(
                firstName,
                lastName,
                alias,
                imageUrl
            );
            user.setPasswordHash(userHash);
            users.add(user);

            // Note that in this example, to represent a follows relationship, only the aliases
            // of the two users are needed
            followers.add(user.asUser());
        }

        // Call the DAOs for the database logic
        if (users.size() > 0) {
//            userDao.createUserBatch(users);
        }
        if (followers.size() > 0) {
            followDao.recordFollowBatch(followers, FOLLOW_TARGET);
        }
    }
}