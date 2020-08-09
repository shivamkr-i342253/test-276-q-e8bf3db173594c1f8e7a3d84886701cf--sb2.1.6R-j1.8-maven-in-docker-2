package org.codejudge.sb.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.codejudge.sb.model.TimestampLogs;
import org.codejudge.sb.service.BadRequestException;
import org.codejudge.sb.service.FileProcessor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class AppController {

    @Autowired
    private ApplicationContext applicationContext;

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
        Integer parallelCount = jsonObject.getInt(SharedConstants.PARALLEL_PROCESSING);

//        if ((logFilesList != null && logFilesList.length() == 0) || parallelCount == 0) {
        if (parallelCount == 0) {
            throw new BadRequestException(SharedConstants.BAD_REQUEST_STATUS, SharedConstants.BAD_REQUEST_REASON);

        }
        else {

//            updateBean(parallelCount.intValue());

            List<String> logFilesList = new ArrayList<>();

            for (Object obj: logFilesArray) {
                logFilesList.add(obj.toString());
            }

//            LOG.info(String.valueOf(logFilesList)+" "+parallelCount);

            Map<String, List<TimestampLogs>> responseMap = new HashMap<>();

            responseMap.put(SharedConstants.RESPONSE, fileProcessor.processLogFilesList(logFilesList, parallelCount.intValue()));

            return responseMap;

        }

    }

    public void updateBean(int count) {

        ThreadPoolTaskExecutor beanObject = (ThreadPoolTaskExecutor) applicationContext.getBean(SharedConstants.CUSTOM_BEAN_ID);

        LOG.info(String.valueOf(count));

        beanObject.setMaxPoolSize(count);

        beanObject.setCorePoolSize(count);

        beanObject.initialize();

        LOG.info(String.valueOf(beanObject.getMaxPoolSize()));

        LOG.info(String.valueOf(beanObject.getCorePoolSize()));

        LOG.info(String.valueOf(beanObject));

    }

}
