package com.example.urooz.devfuel.controller;

import com.example.urooz.devfuel.model.dto.LogRequest;
import com.example.urooz.devfuel.model.dto.LogResponse;
import com.example.urooz.devfuel.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for meal-log persistence (Phase 2).
 */
@RestController
@RequestMapping("/api/v1/logs")
@CrossOrigin(origins = "*")
public class LogController {

    private static final Logger log = LoggerFactory.getLogger(LogController.class);

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    /**
     * Saves a meal log by extracting food items from raw input and persisting them.
     *
     * <p>Endpoint: {@code POST /api/v1/logs/save}</p>
     *
     * @param request contains userId and raw content
     * @return summary of the saved log
     */
    @PostMapping("/save")
    public ResponseEntity<LogResponse> saveMealLog(@RequestBody LogRequest request) {
        log.info("POST /api/v1/logs/save — userId={}, contentLength={}",
                request.userId(),
                request.content() != null ? request.content().length() : 0);

        LogResponse response = logService.saveMealLog(request.userId(), request.content());
        return ResponseEntity.ok(response);
    }
}
