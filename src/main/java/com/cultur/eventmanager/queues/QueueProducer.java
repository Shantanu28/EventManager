package com.cultur.eventmanager.queues;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;

public interface QueueProducer<T> {

    public abstract void pushMessage(T message);

    public abstract RabbitTemplate getTemplate();

    public abstract MessageConverter getConverter();

    void pushMessage(T message, MessageProperties properties);

}