package cz.promptly.worker.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Data
public class AppConfig {
    @Value("${app.scaling.scaling-monitor-interval}")
    private long scalingMonitorInterval;

    @Value("${app.scaling.max-threads}")
    private int maxThreads;

    @Value("${app.scaling.worker-idle-wait}")
    private long workerIdleWait;

    @Value("${app.scaling.worker-idle-timeout}")
    private long workerIdleTimeout;

    @Value("${app.job.max-retries}")
    private int jobMaxRetries;
}
