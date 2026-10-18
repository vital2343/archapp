package com.misis.archapp.balance.command;

import java.util.UUID;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * Команда: создать баланс пользователя.
 */
public record CreateBalanceCommand(@TargetAggregateIdentifier UUID userId) {
}
