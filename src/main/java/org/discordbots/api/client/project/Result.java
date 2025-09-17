package org.discordbots.api.client.project;

import java.util.List;

public class Result<T> {

    private List<T> results;
    private int limit, offset, count, total;



    public List<T> getResults() {
        return results;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public int getCount() {
        return count;
    }

    public int getTotal() {
        return total;
    }

}
