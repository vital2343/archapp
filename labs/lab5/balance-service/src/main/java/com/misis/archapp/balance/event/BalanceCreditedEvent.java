package com.misis.archapp.balance.event;

import java.math.BigDecimal;
import java.util.UUID;
import org.axonframework.serialization.Revision;

/**
 * Событие: на баланс были положены деньги.
 */
@Revision("1")
public record BalanceCreditedEvent(UUID userId, BigDecimal amount) {
}
