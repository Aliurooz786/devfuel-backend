package com.example.urooz.devfuel.model.dto;

import java.util.List;

/**
 * Represents a single food item extracted by the AI agent.
 *
 * @param name      the name of the food item (e.g., "roti", "dal")
 * @param qtyRaw   the raw quantity string as spoken (e.g., "do", "2", "ek plate")
 * @param modifiers additional modifiers (e.g., "with butter", "extra cheese")
 */
public record FoodItem(
        String name,
        String qtyRaw,
        List<String> modifiers
) {
}