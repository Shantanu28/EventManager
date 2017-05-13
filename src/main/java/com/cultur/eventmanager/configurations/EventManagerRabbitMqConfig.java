package com.cultur.eventmanager.configurations;

import com.cultur.eventmanager.services.EventProcessor;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Created by shantanu on 1/5/17.
 */
@Configuration
@ComponentScan(basePackages = { "com.cultur.eventmanager", "com.cultur.eventmanager.repositories", "com.cultur.eventmanager.services", "com.cultur.eventmanager.controllers" })
@PropertySource("classpath:application.properties")
public class EventManagerRabbitMqConfig {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private Environment environment;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(environment.getProperty("rabbitmq.host"));
        connectionFactory.setUsername(environment.getProperty("rabbitmq.username"));
        connectionFactory.setPassword(environment.getProperty("rabbitmq.password"));
        connectionFactory.setPort(5672);

        return connectionFactory;
    }

    /**
     * Required for executing adminstration functions against an AMQP Broker
     */
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public Queue eventDataProcessingQueue() {
        return new Queue(environment.getProperty("event.data.messages.queue"));
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name = "eventDataProcessQueueTemplate")
    public RabbitTemplate eventDataProcessQueueTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(environment.getProperty("event.data.messages.queue"));
        template.setMessageConverter(jsonMessageConverter());
        template.setChannelTransacted(true);

        return template;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter() {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter();
        messageListenerAdapter.setDelegate(eventProcessor());
        messageListenerAdapter.setMessageConverter(jsonMessageConverter());
        messageListenerAdapter.setDefaultListenerMethod("processEvent");

        return messageListenerAdapter;
    }

    @Bean
    public EventProcessor eventProcessor() {
        return new EventProcessor();
    }

    @Bean
    public SimpleMessageListenerContainer listenerContainer() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.setQueues(eventDataProcessingQueue());
        listenerContainer.setMessageListener(listenerAdapter());
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        listenerContainer.setChannelTransacted(true);
        listenerContainer.setDefaultRequeueRejected(false);
//        listenerContainer.setTransactionManager(transactionManager);

        return listenerContainer;
    }
}
