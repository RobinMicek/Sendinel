package cz.sendinel.worker.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Data
public class AppConfig {;
    @Value("${app.scaling.max-threads}")
    private int maxThreads;
}
