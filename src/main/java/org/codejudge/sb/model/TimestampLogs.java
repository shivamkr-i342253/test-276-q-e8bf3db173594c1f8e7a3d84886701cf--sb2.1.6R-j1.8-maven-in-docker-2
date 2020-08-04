package org.codejudge.sb.model;

import java.util.List;

public class TimestampLogs {
    private String timestamp;
    private List<ExceptionCount> logs;

    @Override
    public String toString() {
        return "{" +
                "timestamp: " + timestamp + "," +
                "logs: " + logs +
                "}";

    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<ExceptionCount> getLogs() {
        return logs;
    }

    public void setLogs(List<ExceptionCount> logs) {
        this.logs = logs;
    }
}
