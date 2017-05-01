package com.cultur.eventmanager.queues;

import org.springframework.amqp.core.CustomExchange;

public class DelayedExchange extends CustomExchange{

    public static final String EXCHANGE_NAME = "custom-delayed-exchange";

    public DelayedExchange() {
        super(EXCHANGE_NAME, "x-delayed-message");
        addArgument("x-delayed-type", "direct");
    }
    
}
