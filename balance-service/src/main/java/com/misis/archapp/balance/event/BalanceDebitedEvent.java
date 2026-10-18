package com.misis.archapp.balance.event;

import java.math.BigDecimal;
import java.util.UUID;
import org.axonframework.serialization.Revision;

/**
 * Событие: с баланса были сняты деньги.
 */
@Revision("1")
public record BalanceDebitedEvent(
    UUID userId,
    BigDecimal amount
) {
}
