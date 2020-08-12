package org.codejudge.sb.controller;

public class SharedConstants {

    public static final String LOG_FILES = "logFiles";

    public static final String PARALLEL_PROCESSING = "parallelFileProcessingCount";

    public static final String BAD_REQUEST_STATUS = "failure";

    public static final String BAD_REQUEST_REASON = "Parallel File Processing count must be greater than zero!";

    public static final String RESPONSE = "response";

    public static final long RECORDS_RANGE = 900000;

    public static final String CUSTOM_BEAN_ID = "customThreadPool";

    public static final int LOG_INTERVAL = 720000;

    public static final long THREAD_AWAIT = 10000L;
}
