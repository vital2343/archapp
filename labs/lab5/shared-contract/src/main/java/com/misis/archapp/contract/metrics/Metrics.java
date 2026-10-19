package com.misis.archapp.contract.metrics;

/**
 * Имена и теги кастомных метрик. В Prometheus точки превращаются в подчеркивания:
 * users.new -> users_new_total, api.user.request.duration -> api_user_request_duration_seconds_*
 */
public abstract class Metrics {

    // Counter метрика - сколько всего было создано пользователей
    public static final String USERS_CREATED_TOTAL = "users.new";

    // Timer метрика - сколько времени обрабатывался запрос к API пользователей, с лейблом method = get/post
    public static final String API_USER_REQ_DURATION = "api.user.request.duration";
    public static final String METHOD_TAG = "method";
    public static final String POST_TAG_VAL = "post";
    public static final String GET_TAG_VAL = "get";

    // Counter метрика в notification-service - сколько событий получено из RabbitMQ, с лейблом event = тип события
    public static final String NOTIFICATIONS_RECEIVED_TOTAL = "notifications.received";
    public static final String EVENT_TAG = "event";

}
