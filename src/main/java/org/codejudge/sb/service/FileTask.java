package org.codejudge.sb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Component
@Scope("prototype")
public class FileTask implements Runnable {

    private JdbcTemplate jdbcTemplate;

    private String fileUrl;

    private static final Logger LOG = LoggerFactory.getLogger(FileTask.class);

    public FileTask(JdbcTemplate jdbcTemplate, String fileUrl) {
        this.jdbcTemplate = jdbcTemplate;
        this.fileUrl = fileUrl;
    }

    private Integer saveData(List<Object[]> data) {
        try {
            jdbcTemplate.batchUpdate("INSERT INTO logs(request_id, timestamp, exception_name) VALUES (?,?,?)", data);
            return Integer.valueOf(1);
        }
        catch (Throwable e) {
            LOG.error(e.getLocalizedMessage());
        }

        return null;

    }

//    @Async
    @Override
    public void run() {
        try {

            URL url = new URL(this.fileUrl);
            Scanner scanner = new Scanner(url.openStream());
            List<String> fileContents = new ArrayList<>();

            while (scanner.hasNextLine()) {
                fileContents.add(scanner.nextLine());

            }
            List<Object[]> splitUpData = fileContents.stream().map(record -> record.split(" ")).collect(Collectors.toList());

            saveData(splitUpData);

        }
        catch (MalformedURLException e) {
            LOG.error(e.getLocalizedMessage());
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage());
        }

    }
}
