package fr.univtln.bruno.starter.ai.v3;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * V3 : Chat with memory per session
 *
 * <h2>Goal</h2>
 * Same default memory system, but with better conversational stability.
 *
 * <h2>Why this exists</h2>
 * Default memory is raw text history → sometimes ambiguous.
 *
 * This version slightly improves:
 * <ul>
 *   <li>context continuity</li>
 *   <li>follow-up question handling</li>
 *   <li>consistency of travel intent</li>
 * </ul>
 *
 * <h2>Important clarification</h2>
 * It is still the same default memory system.
 */
@RegisterAiService
@ApplicationScoped
@SystemMessage("""
You are a helpful travel assistant.
Answer concisely and keep travel advice practical.
""")
public interface ConversationalTravelAi {

    /**
     * Chat endpoint using default LangChain4j memory.
     *
     * @param sessionId conversation identifier
     * @param message user message
     * @return contextual assistant response
     */
    String chat(@MemoryId String sessionId, @UserMessage String message);
}