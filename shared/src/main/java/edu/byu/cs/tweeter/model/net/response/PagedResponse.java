package edu.byu.cs.tweeter.model.net.response;

import java.util.List;
import java.util.Objects;

/**
 * A response that can indicate whether there is more data available from the server.
 */
public class PagedResponse<ResultT> extends Response {
    private final List<ResultT> results;
    private final Boolean hasMorePages;

    public PagedResponse(List<ResultT> results, boolean hasMorePages) {
        this.results = results;
        this.hasMorePages = hasMorePages;
    }

    public PagedResponse(String message) {
        super(message);
        this.results = null;
        this.hasMorePages = null;
    }

    public List<ResultT> getResults() { return results; }

    /**
     * An indicator of whether more data is available from the server. A value of true indicates
     * that the result was limited by a maximum value in the request and an additional request
     * would return additional data.
     *
     * @return true if more data is available; otherwise, false.
     */
    public boolean getHasMorePages() {
        return hasMorePages;
    }

    @Override
    public boolean equals(Object param) {
        if (this == param) {
            return true;
        }

        if (param == null || getClass() != param.getClass()) {
            return false;
        }

        UsersResponse that = (UsersResponse) param;

        return (Objects.equals(getResults(), that.getResults()) &&
                Objects.equals(this.getErrorMessage(), that.getErrorMessage()) &&
                this.isSuccess() == that.isSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getResults());
    }
}
