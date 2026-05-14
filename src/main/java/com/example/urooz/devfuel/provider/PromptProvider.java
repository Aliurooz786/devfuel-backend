package com.example.urooz.devfuel.provider;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Manages loading and rendering of StringTemplate (.st) prompt files
 * from the classpath using Spring AI's {@link PromptTemplate}.
 */
@Component
public class PromptProvider {

    @Value("classpath:prompts/extraction-agent.st")
    private Resource extractionAgentResource;

    @Value("classpath:prompts/estimation-agent.st")
    private Resource estimationAgentResource;

    /**
     * Loads the extraction-agent prompt template and renders it
     * with the provided user input.
     *
     * @param input the raw text from the user
     * @return the fully rendered prompt string ready for the ChatClient
     */
    public String getExtractionPrompt(String input) {
        PromptTemplate template = new PromptTemplate(extractionAgentResource);
        return template.render(Map.of("input", input));
    }

    /**
     * Loads the estimation-agent prompt template and renders it
     * with the food items summary and meal context.
     *
     * @param items   formatted multi-line string of food items
     * @param context the meal context (e.g., "canteen", "home", "")
     * @return the fully rendered prompt string ready for the ChatClient
     */
    public String getEstimationPrompt(String items, String context) {
        PromptTemplate template = new PromptTemplate(estimationAgentResource);
        return template.render(Map.of(
                "items", items,
                "context", context != null ? context : ""
        ));
    }
}
