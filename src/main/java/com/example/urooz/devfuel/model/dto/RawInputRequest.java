package com.example.urooz.devfuel.model.dto;

/**
 * Inbound request DTO from the React frontend.
 * Contains the raw text content describing food intake.
 *
 * @param content the unstructured text input (may be in Hinglish)
 */
public record RawInputRequest(
        String content
) {
}
