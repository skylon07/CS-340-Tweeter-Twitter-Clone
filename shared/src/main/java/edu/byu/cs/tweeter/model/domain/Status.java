package edu.byu.cs.tweeter.model.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Represents a status (or tweet) posted by a user.
 */
public class Status implements Serializable {
    public static List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    private static int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    public static List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    /**
     * Text for the status.
     */
    private String post;
    /**
     * User who sent the status.
     */
    private User user;
    /**
     * String representation of the date/time at which the status was sent.
     */
    private Long timestamp;
    /**
     * URLs contained in the post text.
     */
    private List<String> urls;
    /**
     * User mentions contained in the post text.
     */
    private List<String> mentions;

    public Status() {
    }

    public Status(String post, User user, Long timestamp, List<String> urls, List<String> mentions) {
        this.post = post;
        this.user = user;
        this.timestamp = timestamp;
        this.urls = urls;
        this.mentions = mentions;
    }

    public String asPageMark() { return post + "::" + user.getAlias() + "::" + timestamp; }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getPost() {
        return post;
    }

    public List<String> getUrls() {
        return urls;
    }

    public List<String> getMentions() {
        return mentions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return Objects.equals(post, status.post) &&
                Objects.equals(user, status.user) &&
                Objects.equals(timestamp, status.timestamp) &&
                Objects.equals(mentions, status.mentions) &&
                Objects.equals(urls, status.urls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(post, user, timestamp, mentions, urls);
    }

    @Override
    public String toString() {
        return "Status{" +
                "post='" + post + '\'' +
                ", user=" + user +
                ", timestamp=" + timestamp +
                ", mentions=" + mentions +
                ", urls=" + urls +
                '}';
    }

}
