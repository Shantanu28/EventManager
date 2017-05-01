package com.cultur.eventmanager.queues;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class EventDataProcessQueueProducer<T> extends AbstractQueueProducer<T> {

    @Autowired
    @Qualifier("eventDataProcessQueueTemplate")
    private RabbitTemplate template;

    @Autowired
    private MessageConverter converter;
    
    @Override
    public RabbitTemplate getTemplate() {
        return template;
    }

    @Override
    public MessageConverter getConverter() {
        return converter;
    }

}
