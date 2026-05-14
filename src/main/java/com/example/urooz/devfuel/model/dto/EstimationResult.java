package com.example.urooz.devfuel.model.dto;

/**
 * Structured result from the AI Estimation Agent containing
 * total calorie count and macronutrient breakdown.
 *
 * @param calories total estimated calories (kcal)
 * @param protein  estimated protein (grams)
 * @param carbs    estimated carbohydrates (grams)
 * @param fat      estimated fat (grams)
 */
public record EstimationResult(
        Double calories,
        Double protein,
        Double carbs,
        Double fat
) {

    /**
     * Fallback constant used when estimation fails or is skipped.
     */
    public static final EstimationResult ZERO = new EstimationResult(0.0, 0.0, 0.0, 0.0);
}
