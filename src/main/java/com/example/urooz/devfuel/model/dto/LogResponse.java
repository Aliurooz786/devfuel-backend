package com.example.urooz.devfuel.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO returned after a meal log is saved.
 *
 * @param logId              the UUID of the persisted log
 * @param userId             the user who owns this log
 * @param itemCount          number of food items extracted
 * @param context            detected meal context (e.g., "canteen")
 * @param needsClarification whether the AI flagged the input as vague
 * @param loggedAt           timestamp when the log was persisted
 * @param message            human-readable status message
 */
public record LogResponse(
        UUID logId,
        Long userId,
        int itemCount,
        String context,
        boolean needsClarification,
        LocalDateTime loggedAt,
        String message
) {
}
