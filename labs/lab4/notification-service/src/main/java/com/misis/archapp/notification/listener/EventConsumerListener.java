package com.misis.archapp.notification.listener;

import com.misis.archapp.contract.configuration.RabbitConfiguration;
import com.misis.archapp.contract.dto.UserCreatedEvent;
import com.misis.archapp.notification.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Consumer: слушает очередь user.events и передает события в NotificationService.
 */
@Service
public class EventConsumerListener {

    private final NotificationService notificationService;

    @Autowired
    public EventConsumerListener(
        NotificationService notificationService
    ) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = RabbitConfiguration.USER_QUEUE)
    public void handleUserEvent(UserCreatedEvent event) {
        notificationService.sendNotification(event);
    }

}
