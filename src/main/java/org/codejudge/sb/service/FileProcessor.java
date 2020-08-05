package org.codejudge.sb.service;

import org.codejudge.sb.controller.SharedConstants;
import org.codejudge.sb.model.ExceptionCount;
import org.codejudge.sb.model.TimestampLogs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class FileProcessor {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(FileProcessor.class);



    private void initializeDataStore() {
        LOG.info("Creating tables...");

        jdbcTemplate.execute("DROP TABLE logs IF EXISTS");

        jdbcTemplate.execute("CREATE TABLE logs (" +
                "request_id VARCHAR(255), timestamp VARCHAR(255), exception_name VARCHAR(255))");

    }

    public List<TimestampLogs> processLogFilesList(List<String> fileList) {

        initializeDataStore();

        List<CompletableFuture<Integer>> completableFutureList = new ArrayList<>();

        for (int i = 0; i < fileList.size(); ++i) {

            FileTask fileTask = new FileTask(jdbcTemplate);

            completableFutureList.add(fileTask.fetchFile(fileList.get(i)));
        }

        for (int i = 0; i < completableFutureList.size(); ++i) {
            CompletableFuture.allOf(completableFutureList.get(i)).join();
        }

        return aggregateLogsByTimestamp();

    }

    private String getMinimumTimestamp() {
        try {
            return jdbcTemplate.queryForObject("SELECT MIN(timestamp) FROM logs", String.class);
        }
        catch (Throwable e) {
            LOG.error(e.getLocalizedMessage());
        }
        return null;
    }

    private String getMaximumTimestamp() {
        try {
            return jdbcTemplate.queryForObject("SELECT MAx(timestamp) FROM logs", String.class);
        }
        catch (Throwable e) {
            LOG.error(e.getLocalizedMessage());
        }
        return null;
    }

    private List<TimestampLogs> aggregateLogsByTimestamp() {
        List<TimestampLogs> timestampLogsList = new ArrayList<>();

        String minTimestamp = getMinimumTimestamp();

        String maxTimestamp = getMaximumTimestamp();

        long minTimestampValue = Long.parseLong(minTimestamp);

        long maxTimestampValue = Long.parseLong(maxTimestamp);

        minTimestampValue -= SharedConstants.LOG_INTERVAL;

        while (minTimestampValue <= maxTimestampValue) {

            String minTimestampRange = String.valueOf(minTimestampValue + SharedConstants.RECORDS_RANGE);

            List<ExceptionCount> exceptionCountList = jdbcTemplate.query("SELECT COUNT(exception_name), exception_name FROM logs " +
                    "WHERE timestamp BETWEEN "+ String.valueOf(minTimestampValue)+ " AND "+ minTimestampRange+
                    " GROUP BY exception_name ORDER BY exception_name", new ExceptionMapper());

            if (exceptionCountList.size() > 0) {
                TimestampLogs timestampLogs = new TimestampLogs();
                String subTimestamp = String.valueOf(minTimestampValue);
                String timestamp = timeRangeFromTimestamp(subTimestamp, minTimestampRange);
                timestampLogs.setTimestamp(timestamp);
                timestampLogs.setLogs(exceptionCountList);
                timestampLogsList.add(timestampLogs);
            }

            minTimestampValue = Long.parseLong(minTimestampRange)+1;
        }


//        LOG.info(minTimestamp);

        return timestampLogsList;
    }

    public String timeRangeFromTimestamp(String timestamp, String timestampRange) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        simpleDateFormat.applyPattern("HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));


        Date date = new Date(Long.parseLong(timestamp));
        String startTime = simpleDateFormat.format(date);

        Date dateRange = new Date(Long.parseLong(timestampRange));
        String rangeTime = simpleDateFormat.format(dateRange);

        return startTime.concat("-").concat(rangeTime);
    }
}
