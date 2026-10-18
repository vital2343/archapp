package com.misis.archapp.notification.listener;

import com.misis.archapp.contract.configuration.RabbitConfiguration;
import com.misis.archapp.contract.dto.UserCreatedEvent;
import com.misis.archapp.contract.metrics.Metrics;
import com.misis.archapp.notification.service.NotificationService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Consumer: слушает очередь user.events и передает события в NotificationService.
 */
@Service
public class EventConsumerListener {

    private final NotificationService notificationService;
    private final MeterRegistry meterRegistry;

    @Autowired
    public EventConsumerListener(
        NotificationService notificationService,
        MeterRegistry meterRegistry
    ) {
        this.notificationService = notificationService;
        this.meterRegistry = meterRegistry;
    }

    @RabbitListener(queues = RabbitConfiguration.USER_QUEUE)
    public void handleUserEvent(UserCreatedEvent event) {
        // считаем полученные события, в Prometheus: notifications_received_total{event="user_created"}
        meterRegistry.counter(Metrics.NOTIFICATIONS_RECEIVED_TOTAL, Metrics.EVENT_TAG, "user_created").increment();
        notificationService.sendNotification(event);
    }

}
