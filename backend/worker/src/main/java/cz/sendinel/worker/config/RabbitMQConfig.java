package cz.sendinel.worker.config;

import cz.sendinel.shared.config.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    private final AppConfig appConfig;

    @Bean
    public Queue jobResponseQueue() {
        return new Queue(Constants.RABBIT_MQ_JOB_RESPONSE_QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(Constants.RABBIT_MQ_JOB_EXCHANGE_NAME);
    }

    @Bean
    public Binding jobResponseBinding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(Constants.RABBIT_MQ_JOB_RESPONSE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);

        // Use AppConfig to configure scaling
        factory.setMessageConverter(jsonMessageConverter());
        factory.setConcurrentConsumers(Math.max(1, appConfig.getMaxThreads() / 2)); // initial
        factory.setMaxConcurrentConsumers(appConfig.getMaxThreads());
        factory.setPrefetchCount(1); // process one message at a time per worker

        return factory;
    }

}