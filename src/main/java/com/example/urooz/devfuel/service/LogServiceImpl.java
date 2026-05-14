package com.example.urooz.devfuel.service;

import com.example.urooz.devfuel.model.dto.EstimationResult;
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
 * <p>Flow: Controller → LogService → ExtractionService (AI) → EstimationService (AI) → LogRepository (DB)</p>
 */
@Service
public class LogServiceImpl implements LogService {

    private static final Logger log = LoggerFactory.getLogger(LogServiceImpl.class);

    private final ExtractionService extractionService;
    private final EstimationService estimationService;
    private final LogRepository logRepository;
    private final ObjectMapper objectMapper;

    public LogServiceImpl(ExtractionService extractionService,
                          EstimationService estimationService,
                          LogRepository logRepository,
                          ObjectMapper objectMapper) {
        this.extractionService = extractionService;
        this.estimationService = estimationService;
        this.logRepository = logRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Orchestrates the full extraction → estimation → persistence pipeline.
     *
     * <ol>
     *   <li>Calls the Extraction Agent to parse the raw input.</li>
     *   <li>If extraction is unambiguous, calls the Estimation Agent for calorie/macro estimates.</li>
     *   <li>If estimation fails, logs a warning and saves with zero values (graceful degradation).</li>
     *   <li>Serializes extracted items to JSON and saves the {@link NutritionLog}.</li>
     * </ol>
     */
    @Override
    @Transactional
    public LogResponse saveMealLog(Long userId, String content) {
        log.info("saveMealLog invoked — userId={}, contentLength={}", userId, content.length());

        // 1. Call the Extraction Agent
        ExtractionResponse extraction = extractionService.extract(content);
        int itemCount = extraction.items() != null ? extraction.items().size() : 0;

        // 2. Serialize the items list to JSON for the macrosJson column
        String macrosJson = serializeItems(extraction);

        // 3. Estimate calories/macros (only if extraction is clear and has items)
        EstimationResult estimation = EstimationResult.ZERO;

        if (!extraction.needsClarification() && itemCount > 0) {
            estimation = safeEstimate(extraction);
        } else {
            log.info("Skipping estimation — needsClarification={}, itemCount={}",
                    extraction.needsClarification(), itemCount);
        }

        // 4. Build the NutritionLog entity
        NutritionLog entity = NutritionLog.builder()
                .userId(userId)
                .rawInput(content)
                .totalCalories(estimation.calories() != null ? estimation.calories().intValue() : 0)
                .protein(estimation.protein())
                .carbs(estimation.carbs())
                .fat(estimation.fat())
                .macrosJson(macrosJson)
                .context(extraction.context())
                .needsClarification(extraction.needsClarification())
                .build();

        // 5. Persist
        NutritionLog saved = logRepository.save(entity);

        log.info("NutritionLog persisted — logId={}, calories={}, needsClarification={}",
                saved.getId(), saved.getTotalCalories(), saved.isNeedsClarification());

        // 6. Build response
        String message = buildMessage(extraction, saved);

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
     * Calls the Estimation Agent with graceful error handling.
     * If estimation fails, returns zeroed-out values instead of crashing.
     */
    private EstimationResult safeEstimate(ExtractionResponse extraction) {
        try {
            return estimationService.estimate(extraction.items(), extraction.context());
        } catch (Exception e) {
            log.warn("Estimation failed — saving log with zero calories. Reason: {}", e.getMessage());
            return EstimationResult.ZERO;
        }
    }

    /**
     * Builds a human-readable response message based on the outcome.
     */
    private String buildMessage(ExtractionResponse extraction, NutritionLog saved) {
        if (extraction.needsClarification()) {
            return "Log saved but needs clarification — please provide more details.";
        }
        int itemCount = extraction.items() != null ? extraction.items().size() : 0;
        return String.format("Meal logged successfully with %d item(s) — estimated %d kcal.",
                itemCount, saved.getTotalCalories());
    }

    /**
     * Serializes the extraction response items to a JSON string for storage.
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
