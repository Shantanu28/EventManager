package com.cultur.eventmanager.queues;

public interface DelayedMessageProducer<T> {

    public void pushMessageWithDelay(T message, long milliseconds);
}
