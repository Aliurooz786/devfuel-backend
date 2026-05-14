package com.example.urooz.devfuel.service;

import com.example.urooz.devfuel.model.dto.EstimationResult;
import com.example.urooz.devfuel.model.dto.FoodItem;

import java.util.List;

/**
 * Contract for the AI-powered calorie and macro estimation agent.
 * Takes extracted food items and meal context, returns nutritional estimates.
 */
public interface EstimationService {

    /**
     * Estimates total calories and macronutrient breakdown for a list of food items.
     *
     * <p>If the context indicates eating out (canteen, restaurant, etc.),
     * a 15% oil buffer is applied to account for extra cooking oil/ghee.</p>
     *
     * @param items   the list of extracted food items
     * @param context the meal context (e.g., "canteen", "home")
     * @return estimated calories and macros
     */
    EstimationResult estimate(List<FoodItem> items, String context);
}
