package com.example.urooz.devfuel.controller;

import com.example.urooz.devfuel.model.dto.ExtractionResponse;
import com.example.urooz.devfuel.model.dto.RawInputRequest;
import com.example.urooz.devfuel.service.ExtractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API endpoints consumed by the React frontend
 * for the Phase 1 Extraction Agent.
 */
@RestController
@RequestMapping("/api/v1/extraction")
@CrossOrigin(origins = "*") // Allow React dev server
public class ExtractionController {

    private static final Logger log = LoggerFactory.getLogger(ExtractionController.class);

    private final ExtractionService extractionService;

    public ExtractionController(ExtractionService extractionService) {
        this.extractionService = extractionService;
    }

    /**
     * Parses raw text input and returns structured food extraction data.
     *
     * <p>Endpoint: {@code POST /api/v1/extraction/parse}</p>
     *
     * @param request the raw input from the React frontend
     * @return structured {@link ExtractionResponse} with extracted items
     */
    @PostMapping("/parse")
    public ResponseEntity<ExtractionResponse> parse(@RequestBody RawInputRequest request) {
        log.info("POST /api/v1/extraction/parse — content length: {}",
                request.content() != null ? request.content().length() : 0);

        ExtractionResponse response = extractionService.extract(request.content());
        return ResponseEntity.ok(response);
    }
}
