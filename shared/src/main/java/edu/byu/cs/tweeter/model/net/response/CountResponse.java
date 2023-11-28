package edu.byu.cs.tweeter.model.net.response;

public class CountResponse extends Response {
    private final Integer count;

    public CountResponse(String message) {
        super(message);
        count = null;
    }

    public CountResponse(Integer count) {
        this.count = count;
    }

    public Integer getCount() { return count; }
}
