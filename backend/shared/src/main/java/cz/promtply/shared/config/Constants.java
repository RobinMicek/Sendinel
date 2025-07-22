package cz.promtply.shared.config;

import java.io.File;
import java.util.Objects;

public class Constants {

    public static final String INTERNAL_API_ROUTE_PREFIX = "/internal"; // e.g. /internal
    public static final String EXTERNAL_API_ROUTE_PREFIX = "/external"; // e.g. /external
    public static final String TRACKING_API_ROUTE_PREFIX = "/tracking"; // e.g. /tracking


    public static final String RABBIT_MQ_JOB_EXCHANGE_NAME = "jobs.exchange";
    public static final String RABBIT_MQ_JOB_REQUEST_QUEUE_NAME = "jobRequests.queue";
    public static final String RABBIT_MQ_JOB_REQUEST_ROUTING_KEY = "jobRequests.key";
    public static final String RABBIT_MQ_JOB_RESPONSE_QUEUE_NAME = "jobResponses.queue";
    public static final String RABBIT_MQ_JOB_RESPONSE_ROUTING_KEY = "jobResponses.key";

    public static final String OBFUSCATION_MASK = "********";
    public static final String LOG_BLOCK_DELIMITER = "#############################################################################";

    public static final String EXPORT_FILE_EXTENSION = ".export";
    public static final String EXPORT_FILE_PART_EXTENSION = ".template";

    public static final File PDF_EXPORT_DEFAULT_FONT = new File(Objects.requireNonNull(Constants.class.getResource("/static/fonts/DejaVuSans.ttf")).toString());

    private Constants() {}
}