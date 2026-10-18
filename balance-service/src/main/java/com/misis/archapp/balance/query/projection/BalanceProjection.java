package com.misis.archapp.balance.query.projection;

import com.misis.archapp.balance.event.BalanceCreatedEvent;
import com.misis.archapp.balance.event.BalanceCreditedEvent;
import com.misis.archapp.balance.event.BalanceDebitedEvent;
import com.misis.archapp.balance.query.BalanceHistoryEntry;
import com.misis.archapp.balance.query.BalanceHistoryRepository;
import com.misis.archapp.balance.query.BalanceView;
import com.misis.archapp.balance.query.BalanceViewRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.eventhandling.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Проекция: строит read-модели (BalanceView и BalanceHistoryEntry) только из событий.
 * При пустой БД (или после reset) все события из Event Store проигрываются заново (replay).
 */
@Component
public class BalanceProjection {

    private final BalanceViewRepository balanceViewRepository;
    private final BalanceHistoryRepository balanceHistoryRepository;

    @Autowired
    public BalanceProjection(
        BalanceViewRepository balanceViewRepository,
        BalanceHistoryRepository balanceHistoryRepository
    ) {
        this.balanceViewRepository = balanceViewRepository;
        this.balanceHistoryRepository = balanceHistoryRepository;
    }

    @EventHandler
    public void on(BalanceCreatedEvent e, @Timestamp Instant timestamp) {
        balanceViewRepository.save(new BalanceView().setUserId(e.userId()).setBalance(BigDecimal.ZERO));
        saveHistory(e.userId(), "CREATE", BigDecimal.ZERO, timestamp);
    }

    @EventHandler
    public void on(BalanceCreditedEvent e, @Timestamp Instant timestamp) {
        // меняет состояние read-модели баланса пользователя - добавляет деньги
        BalanceView view = balanceViewRepository.findById(e.userId()).orElseThrow(EntityNotFoundException::new);
        balanceViewRepository.save(view.setBalance(view.getBalance().add(e.amount())));
        saveHistory(e.userId(), "CREDIT", e.amount(), timestamp);
    }

    @EventHandler
    public void on(BalanceDebitedEvent e, @Timestamp Instant timestamp) {
        // меняет состояние read-модели баланса пользователя - забирает деньги
        BalanceView view = balanceViewRepository.findById(e.userId()).orElseThrow(EntityNotFoundException::new);
        balanceViewRepository.save(view.setBalance(view.getBalance().subtract(e.amount())));
        saveHistory(e.userId(), "DEBIT", e.amount(), timestamp);
    }

    // перед replay очищает read-модели, чтобы построить их из событий с нуля
    @ResetHandler
    public void reset() {
        balanceHistoryRepository.deleteAllInBatch();
        balanceViewRepository.deleteAllInBatch();
    }

    // время берется из события, поэтому после replay история совпадает с исходной
    private void saveHistory(UUID userId, String type, BigDecimal amount, Instant timestamp) {
        balanceHistoryRepository.save(
            new BalanceHistoryEntry()
                .setUserId(userId)
                .setType(type)
                .setAmount(amount)
                .setTimestamp(timestamp)
        );
    }

}
