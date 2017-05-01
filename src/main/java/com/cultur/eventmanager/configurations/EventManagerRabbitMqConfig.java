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

    private static final String EVENT_DATA_MESSAGE_QUEUE = "EVENT_DATA_PROCESS";

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private Environment environment;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
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
    public Queue simpleQueue() {
        return new Queue(EVENT_DATA_MESSAGE_QUEUE);
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name = "eventDataProcessQueueTemplate")
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(EVENT_DATA_MESSAGE_QUEUE);
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
        listenerContainer.setQueues(simpleQueue());
//        listenerContainer.setMessageConverter(jsonMessageConverter());
        listenerContainer.setMessageListener(listenerAdapter());
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        listenerContainer.setChannelTransacted(true);
        listenerContainer.setDefaultRequeueRejected(false);
        listenerContainer.setTransactionManager(transactionManager);

        return listenerContainer;
    }
}
