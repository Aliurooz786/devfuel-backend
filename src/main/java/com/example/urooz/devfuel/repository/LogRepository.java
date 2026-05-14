package com.example.urooz.devfuel.repository;

import com.example.urooz.devfuel.model.entity.NutritionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link NutritionLog} entities.
 */
@Repository
public interface LogRepository extends JpaRepository<NutritionLog, UUID> {

    /**
     * Finds all nutrition logs for a given user, ordered by most recent first.
     */
    List<NutritionLog> findByUserIdOrderByLoggedAtDesc(Long userId);
}
