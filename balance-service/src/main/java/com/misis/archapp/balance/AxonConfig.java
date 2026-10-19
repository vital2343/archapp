package com.misis.archapp.balance;

import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventhandling.tokenstore.jpa.JpaTokenStore;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.serialization.Serializer;
import org.axonframework.springboot.util.jpa.ContainerManagedEntityManagerProvider;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// сущности приложения + TokenEntry из Axon (таблица token_entry)
@EntityScan(basePackages = {"com.misis.archapp.balance", "org.axonframework.eventhandling.tokenstore.jpa"})
public class AxonConfig {

    // снапшот состояния агрегата после каждых 10 событий
    @Bean
    public SnapshotTriggerDefinition snapshotTrigger(Snapshotter snapshotter) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, 10);
    }

    @Bean
    public EntityManagerProvider entityManagerProvider() {
        return new ContainerManagedEntityManagerProvider();
    }

    // позиция проекции в потоке событий хранится в БД (token_entry), а не в памяти:
    // иначе после перезапуска проекция заново применит все события и задублирует read-модели
    @Bean
    public TokenStore tokenStore(EntityManagerProvider entityManagerProvider, Serializer serializer) {
        return JpaTokenStore.builder()
            .entityManagerProvider(entityManagerProvider)
            .serializer(serializer)
            .build();
    }

}
