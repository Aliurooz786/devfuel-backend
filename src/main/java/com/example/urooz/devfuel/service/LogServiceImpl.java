package com.example.urooz.devfuel.service;

import com.example.urooz.devfuel.model.dto.ExtractionResponse;
import com.example.urooz.devfuel.model.dto.LogResponse;
import com.example.urooz.devfuel.model.entity.NutritionLog;
import com.example.urooz.devfuel.repository.LogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link LogService}.
 *
 * <p>Flow: Controller → LogService → ExtractionService (AI) → LogRepository (DB)</p>
 */
@Service
public class LogServiceImpl implements LogService {

    private static final Logger log = LoggerFactory.getLogger(LogServiceImpl.class);

    private final ExtractionService extractionService;
    private final LogRepository logRepository;
    private final ObjectMapper objectMapper;

    public LogServiceImpl(ExtractionService extractionService,
                          LogRepository logRepository,
                          ObjectMapper objectMapper) {
        this.extractionService = extractionService;
        this.logRepository = logRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Orchestrates the extraction → persistence pipeline.
     *
     * <ol>
     *   <li>Calls the Extraction Agent to parse the raw input.</li>
     *   <li>If {@code needsClarification} is true, still persists a partial log
     *       (so the user can update it later) but flags the response.</li>
     *   <li>Serializes extracted items to JSON and saves the {@link NutritionLog}.</li>
     * </ol>
     */
    @Override
    @Transactional
    public LogResponse saveMealLog(Long userId, String content) {
        log.info("saveMealLog invoked — userId={}, contentLength={}", userId, content.length());

        // 1. Call the Extraction Agent
        ExtractionResponse extraction = extractionService.extract(content);

        // 2. Serialize the items list to JSON string for macrosJson column
        String macrosJson = serializeItems(extraction);

        // 3. Build the NutritionLog entity
        NutritionLog entity = NutritionLog.builder()
                .userId(userId)
                .rawInput(content)
                .totalCalories(null) // Phase 3 will compute this via Nutrition Lookup Agent
                .macrosJson(macrosJson)
                .context(extraction.context())
                .needsClarification(extraction.needsClarification())
                .build();

        // 4. Persist
        NutritionLog saved = logRepository.save(entity);

        log.info("NutritionLog persisted — logId={}, needsClarification={}",
                saved.getId(), saved.isNeedsClarification());

        // 5. Build response
        int itemCount = extraction.items() != null ? extraction.items().size() : 0;
        String message = extraction.needsClarification()
                ? "Log saved but needs clarification — please provide more details."
                : "Meal logged successfully with " + itemCount + " item(s).";

        return new LogResponse(
                saved.getId(),
                saved.getUserId(),
                itemCount,
                saved.getContext(),
                saved.isNeedsClarification(),
                saved.getLoggedAt(),
                message
        );
    }

    /**
     * Serializes the extraction response to a JSON string for storage.
     */
    private String serializeItems(ExtractionResponse extraction) {
        try {
            return objectMapper.writeValueAsString(extraction.items());
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize extraction items to JSON", e);
            throw new RuntimeException("Failed to serialize extraction data", e);
        }
    }
}
