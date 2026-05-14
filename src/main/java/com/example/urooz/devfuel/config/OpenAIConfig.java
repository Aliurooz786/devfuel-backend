package com.example.urooz.devfuel.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring AI Bean configurations for OpenAI integration.
 *
 * <p>The actual model (gpt-4o-mini) and API key are configured via
 * {@code application.properties / application.yml}:
 * <pre>
 *   spring.ai.openai.api-key=${OPENAI_API_KEY}
 *   spring.ai.openai.chat.options.model=gpt-4o-mini
 * </pre>
 */
@Configuration
public class OpenAIConfig {

    /**
     * Creates a pre-configured {@link ChatClient} bean that all services
     * can inject. The underlying model is determined by Spring AI
     * auto-configuration properties.
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("You are a precise food-item extraction agent. "
                        + "Always respond with valid JSON matching the requested schema.")
                .build();
    }
}
