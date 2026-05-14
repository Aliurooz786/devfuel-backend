package com.example.urooz.devfuel.service;

import com.example.urooz.devfuel.model.dto.EstimationResult;
import com.example.urooz.devfuel.model.dto.FoodItem;
import com.example.urooz.devfuel.provider.PromptProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link EstimationService} that uses Spring AI's
 * {@link ChatClient} to call OpenAI (GPT-4o mini) for calorie/macro estimation.
 *
 * <p>Flow: LogService → EstimationService → PromptProvider → ChatClient → OpenAI</p>
 */
@Service
public class EstimationServiceImpl implements EstimationService {

    private static final Logger log = LoggerFactory.getLogger(EstimationServiceImpl.class);

    private final ChatClient chatClient;
    private final PromptProvider promptProvider;

    public EstimationServiceImpl(ChatClient chatClient, PromptProvider promptProvider) {
        this.chatClient = chatClient;
        this.promptProvider = promptProvider;
    }

    @Override
    public EstimationResult estimate(List<FoodItem> items, String context) {
        log.info("Estimation agent invoked — {} items, context='{}'", items.size(), context);

        // 1. Build a human-readable item summary for the prompt
        String itemsSummary = items.stream()
                .map(this::formatItem)
                .collect(Collectors.joining("\n"));

        // 2. Render the estimation prompt
        String renderedPrompt = promptProvider.getEstimationPrompt(itemsSummary, context);
        log.debug("Rendered estimation prompt:\n{}", renderedPrompt);

        // 3. Call OpenAI with structured output mapping
        EstimationResult result = chatClient.prompt()
                .user(renderedPrompt)
                .call()
                .entity(EstimationResult.class);

        // 4. Null-response guard
        if (result == null) {
            log.error("Estimation agent returned null for items: {}", itemsSummary);
            throw new IllegalStateException(
                    "Estimation agent received a null response from the AI model.");
        }

        log.info("Estimation complete — calories={}, protein={}g, carbs={}g, fat={}g",
                result.calories(), result.protein(), result.carbs(), result.fat());

        return result;
    }

    /**
     * Formats a single FoodItem into a readable line for the AI prompt.
     * e.g., "2 roti (with butter, tandoori)"
     */
    private String formatItem(FoodItem item) {
        StringBuilder sb = new StringBuilder();
        sb.append(item.qtyRaw()).append(" ").append(item.name());
        if (item.modifiers() != null && !item.modifiers().isEmpty()) {
            sb.append(" (").append(String.join(", ", item.modifiers())).append(")");
        }
        return sb.toString();
    }
}
