package cz.promtply.api.config;

import cz.promtply.shared.config.Constants;
import cz.promtply.shared.enums.EmailPrioritiesEnum;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue jobRequestQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", EmailPrioritiesEnum.values().length - 1); // Indexing from 0
        return new Queue(Constants.RABBIT_MQ_JOB_REQUEST_QUEUE_NAME, true, false, false, args);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(Constants.RABBIT_MQ_JOB_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(Constants.RABBIT_MQ_JOB_REQUEST_ROUTING_KEY);
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

}