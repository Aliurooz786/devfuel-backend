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
}
