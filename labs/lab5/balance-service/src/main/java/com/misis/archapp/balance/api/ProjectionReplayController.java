package com.misis.archapp.balance.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.StreamingEventProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Демонстрация replay: очищает read-модели и заново проигрывает все события из Event Store через проекцию.
 */
@Tag(name = "Projection replay")
@RestController
@RequestMapping("/admin/projections")
public class ProjectionReplayController {

    // по умолчанию processing group = пакет класса проекции
    private static final String PROCESSING_GROUP = "com.misis.archapp.balance.query.projection";

    private final EventProcessingConfiguration eventProcessingConfiguration;

    @Autowired
    public ProjectionReplayController(EventProcessingConfiguration eventProcessingConfiguration) {
        this.eventProcessingConfiguration = eventProcessingConfiguration;
    }

    @PostMapping("/replay")
    public String replay() {
        StreamingEventProcessor processor = eventProcessingConfiguration
            .eventProcessor(PROCESSING_GROUP, StreamingEventProcessor.class)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "processor not found"));

        processor.shutDown();
        processor.resetTokens();
        processor.start();
        return "replay started";
    }

}
