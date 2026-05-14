package com.example.urooz.devfuel.model.dto;

/**
 * Inbound request DTO for the meal-log save endpoint.
 *
 * @param userId  the ID of the user logging the meal
 * @param content the raw text describing food intake (may be Hinglish)
 */
public record LogRequest(
        Long userId,
        String content
) {
}
