package cz.sendinel.worker;

import cz.sendinel.shared.config.Constants;
import cz.sendinel.worker.config.AppConfig;
import cz.sendinel.worker.rabbitmq.EmailStatusProducer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class WorkerApplication {

	Logger logger = LoggerFactory.getLogger(WorkerApplication.class);

	private final AppConfig appConfig;
	private final EmailStatusProducer emailStatusProducer;

    @Bean
    public CommandLineRunner runScalingWorkerPool() {
        return args -> {
            logger.info("Max threads: {}", appConfig.getMaxThreads());
        };
    }

    public static void main(String[] args) {
		SpringApplication.run(WorkerApplication.class, args);
	}

}
