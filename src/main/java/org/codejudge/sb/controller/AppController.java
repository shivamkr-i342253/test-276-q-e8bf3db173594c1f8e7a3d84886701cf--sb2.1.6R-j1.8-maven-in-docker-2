package org.codejudge.sb.controller;

import io.swagger.annotations.ApiOperation;
import org.codejudge.sb.model.TimestampLogs;
import org.codejudge.sb.service.BadRequestException;
import org.codejudge.sb.service.FileProcessor;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

@RestController
@RequestMapping
public class AppController {

    @Autowired
    FileProcessor fileProcessor;

    private static final Logger LOG = LoggerFactory.getLogger(AppController.class);

    @ApiOperation("This is the hello world api")
    @GetMapping("/")
    public String hello() {
        return "Hello World!!";
    }

    @PostMapping("/api/process-logs/")
    public Map<String, ? extends Object> logProcessor(@RequestBody String request) {

        JSONObject jsonObject = new JSONObject(request);
        JSONArray logFilesArray = (JSONArray) jsonObject.get(SharedConstants.LOG_FILES);
        int parallelCount = jsonObject.getInt(SharedConstants.PARALLEL_PROCESSING);

        JSONObject jsonObject1 = null;

//        if ((logFilesList != null && logFilesList.length() == 0) || parallelCount == 0) {
        if (parallelCount == 0) {
            throw new BadRequestException(SharedConstants.BAD_REQUEST_STATUS, SharedConstants.BAD_REQUEST_REASON);

        }
        else {

            List<String> logFilesList = new ArrayList<>();

            for (Object obj: logFilesArray) {
                logFilesList.add(obj.toString());
            }

            LOG.info(String.valueOf(logFilesList)+" "+parallelCount);
//            LOG.info(String.valueOf(fileProcessor.processLogFilesList(logFilesList)));

            Map<String, List<TimestampLogs>> responseMap = new HashMap<>();

            responseMap.put(SharedConstants.RESPONSE, fileProcessor.processLogFilesList(logFilesList));
//
//            jsonObject1 = new JSONObject(new JSON(responseMap));

            return responseMap;


//            LOG.info(fileProcessor.timeRangeFromTimestamp("1573594032526", "1573594932526"));

        }

    }

}
