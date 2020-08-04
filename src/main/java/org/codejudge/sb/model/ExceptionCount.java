package org.codejudge.sb.model;

public class ExceptionCount {
    private String exception;
    private String count;

    @Override
    public String toString() {
        return "{"+
                "exception: " + exception + "," +
                "count: " + count +
                "}";
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
