package com.cultur.eventmanager.queues;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

public abstract class AbstractQueueProducer<T> implements QueueProducer<T>{

    public AbstractQueueProducer() {
        super();
    }

    @Override
    public void pushMessage(T message) {
        pushMessage(message, null);
    }

    @Override
    public void pushMessage(T message, MessageProperties properties) {
        pushMessage(null, message, properties);
    }

    protected void pushMessage(String exchangeName, T message, MessageProperties properties) {
        if (message == null) {
            throw new IllegalArgumentException("Cannot send null message");
        }

        Message messageObject = getConverter().toMessage(message, properties);
        getTemplate().send(exchangeName, null, messageObject);
    }
    
}