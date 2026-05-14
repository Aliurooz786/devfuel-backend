package com.example.urooz.devfuel.service;

import com.example.urooz.devfuel.model.dto.ExtractionResponse;

/**
 * Contract for the food-item extraction agent.
 * Implementations use an AI model to parse unstructured text
 * into structured {@link ExtractionResponse} data.
 */
public interface ExtractionService {

    /**
     * Extracts structured food item data from raw user input.
     *
     * @param content the unstructured text describing food intake (may be Hinglish)
     * @return structured extraction response with items, context, and clarification flag
     */
    ExtractionResponse extract(String content);
}
