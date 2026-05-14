package com.example.urooz.devfuel.service;

import com.example.urooz.devfuel.model.dto.LogResponse;

/**
 * Contract for the meal-log persistence service.
 * Orchestrates AI extraction and database persistence.
 */
public interface LogService {

    /**
     * Processes raw food input through the extraction agent and,
     * if the extraction is unambiguous, persists a {@link com.example.urooz.devfuel.model.entity.NutritionLog}.
     *
     * @param userId  the ID of the user logging the meal
     * @param content the raw text describing food intake
     * @return a summary of the saved log (or a clarification prompt)
     */
    LogResponse saveMealLog(Long userId, String content);
}
