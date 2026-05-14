package com.example.urooz.devfuel.model.dto;

import java.util.List;

/**
 * Structured JSON response returned to the React frontend
 * after the AI extraction agent completes parsing.
 *
 * @param items              list of extracted food items
 * @param context            meal context if detected (e.g., "canteen", "home")
 * @param needsClarification true if the input was too vague for extraction
 */
public record ExtractionResponse(
        List<FoodItem> items,
        String context,
        boolean needsClarification
) {
}