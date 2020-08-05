package org.codejudge.sb.model;

public class ExceptionCount {
    private String exception;
    private int count;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
