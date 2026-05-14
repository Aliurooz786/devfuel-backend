package com.example.urooz.devfuel.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a single meal log entry persisted after AI extraction.
 *
 * <p>The {@code macrosJson} field stores the full extraction result
 * (items + modifiers) as a serialized JSON string for flexible querying.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "nutrition_logs")
public class NutritionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "raw_input", nullable = false, columnDefinition = "TEXT")
    private String rawInput;

    @Column(name = "total_calories")
    private Integer totalCalories;

    /**
     * Serialized JSON of the extracted items array.
     * Stored as TEXT; upgrade to JSONB with hypersistence-utils when needed.
     */
    @Column(name = "macros_json", columnDefinition = "TEXT")
    private String macrosJson;

    @Column(name = "context")
    private String context;

    @Column(name = "needs_clarification", nullable = false)
    private boolean needsClarification;

    @Column(name = "logged_at", nullable = false, updatable = false)
    private LocalDateTime loggedAt;

    /**
     * Automatically sets the log timestamp before first persist.
     */
    @PrePersist
    protected void onCreate() {
        if (this.loggedAt == null) {
            this.loggedAt = LocalDateTime.now();
        }
    }
}
