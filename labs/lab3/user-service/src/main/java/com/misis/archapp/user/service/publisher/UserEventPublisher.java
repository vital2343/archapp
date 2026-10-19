package com.misis.archapp.user.service.publisher;

import com.misis.archapp.contract.configuration.RabbitConfiguration;
import com.misis.archapp.contract.dto.UserCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Producer: публикует события о пользователях в RabbitMQ.
 */
@Service
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public UserEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishUserEvent(UserCreatedEvent event) {
        rabbitTemplate.convertAndSend(
            RabbitConfiguration.USER_EXCHANGE,
            RabbitConfiguration.USER_CREATED_ROUTING_KEY,
            event
        );
    }

}
