package edu.byu.cs.tweeter.server.dao.implementations.dynamodb;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.implementations.dynamodb.beans.UserBean;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDao;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class DynamoUserDao extends DynamoDao implements UserDao {
    public static final String userTableName = "Tweeter-users";
    private static final DynamoDbTable<UserBean> userTable = enhancedClient.table(userTableName, TableSchema.fromBean(UserBean.class));

    private static final String S3_URL = "https://tweeter-images-18671.s3.us-west-1.amazonaws.com/";
    private static final AmazonS3 s3 = AmazonS3ClientBuilder
        .standard()
        .withRegion(Regions.US_WEST_1)
        .build();


    @Override
    public User createUser(String firstName, String lastName, String username, String image) {
        saveProfileImageToS3(username, image);
        String imageUrl = S3_URL + username;

        UserBean newBean = new UserBean(firstName, lastName, username, imageUrl);
        userTable.putItem(newBean);

        return newBean.asUser();
    }

    @Override
    public User getUser(String username) {
        Key key = Key.builder()
            .partitionValue(username)
            .build();
        UserBean userBean = userTable.getItem(key);
        if (userBean == null) return null;
        return userBean.asUser();
    }

    @Override
    public User getUserForLogin(String username) {
        return getUser(username);
    }

    @Override
    public boolean isValidPassword(String username, String password) {
        Key key = Key.builder()
            .partitionValue(username)
            .build();
        UserBean userBean = userTable.getItem(key);
        String userHash = userBean.getPasswordHash();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, userHash);
    }

    @Override
    public void savePassword(String username, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String userHash = encoder.encode(password);

        Key key = Key.builder()
            .partitionValue(username)
            .build();
        UserBean userBean = userTable.getItem(key);

        userBean.setPasswordHash(userHash);
        userTable.updateItem(userBean);
    }

    private void saveProfileImageToS3(String username, String image) {
        byte[] imageBytes = Base64.getDecoder().decode(image);

        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentLength(imageBytes.length);
        metaData.setContentType("image/jpeg");

        PutObjectRequest request = new PutObjectRequest(
            "tweeter-images-18671",
            username,
            new ByteArrayInputStream(imageBytes),
            metaData
        ).withCannedAcl(CannedAccessControlList.PublicRead);
        s3.putObject(request);
    }
}
