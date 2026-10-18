package com.misis.archapp.balance.event;

import java.util.UUID;

/**
 * Событие: баланс был создан.
 */
public record BalanceCreatedEvent(UUID userId) {
}
