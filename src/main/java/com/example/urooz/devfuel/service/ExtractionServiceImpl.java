package com.example.urooz.devfuel.service;

import com.example.urooz.devfuel.model.dto.ExtractionResponse;
import com.example.urooz.devfuel.provider.PromptProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link ExtractionService} that uses Spring AI's
 * {@link ChatClient} fluent API to call OpenAI (GPT-4o mini) and extract
 * structured food data via {@code .entity()} structured-output mapping.
 *
 * <p>Flow: Controller → Service → PromptProvider → ChatClient → OpenAI</p>
 */
@Service
public class ExtractionServiceImpl implements ExtractionService {

    private static final Logger log = LoggerFactory.getLogger(ExtractionServiceImpl.class);

    private final ChatClient chatClient;
    private final PromptProvider promptProvider;

    public ExtractionServiceImpl(ChatClient chatClient, PromptProvider promptProvider) {
        this.chatClient = chatClient;
        this.promptProvider = promptProvider;
    }

    /**
     * Extracts structured food data from the raw user input.
     *
     * @param content the unstructured text from the user
     * @return a fully populated {@link ExtractionResponse}
     * @throws IllegalStateException if the AI model returns a null response
     */
    @Override
    public ExtractionResponse extract(String content) {
        log.info("Extraction agent invoked — input length: {} chars", content.length());

        // 1. Render the prompt from the .st template
        String renderedPrompt = promptProvider.getExtractionPrompt(content);
        log.debug("Rendered prompt: {}", renderedPrompt);

        // 2. Call OpenAI via ChatClient with structured output mapping
        ExtractionResponse response = chatClient.prompt()
                .user(renderedPrompt)
                .call()
                .entity(ExtractionResponse.class);

        // 3. Null-response guard
        if (response == null) {
            log.error("AI model returned null for input: {}", content);
            throw new IllegalStateException(
                    "Extraction agent received a null response from the AI model. "
                            + "The input may be malformed or the model quota may be exhausted."
            );
        }

        log.info("Extraction complete — {} items extracted, needsClarification={}",
                response.items() != null ? response.items().size() : 0,
                response.needsClarification());

        return response;
    }
}
