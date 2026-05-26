package fr.univtln.bruno.starter.ai.v3;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;


/**
 * === V3: CHAT API (MEMORY-ENABLED ASSISTANT) ===
 *
 * <h2>Design principle</h2>
 * HTTP remains stateless, but AI interaction becomes stateful via server memory.
 * We use POST because interacting with the chat updates the server's memory state.
 *
 * <h2>Important clarification</h2>
 * The API returns plain text because this is a chatbot endpoint,
 * not a structured data API (like V2 TravelPlan).
 */
@Path("/api/v3/chat")
public class ConversationalTravelResource {

    private static final Logger LOG = Logger.getLogger(ConversationalTravelResource.class);

    /**
     * AI service with automatic memory management.
     */
    @Inject
    ConversationalTravelAi ai;

    /**
     * Sends a message to a conversational AI.
     *
     * <h3>Behavior</h3>
     * - Same sessionId → continuity of conversation
     * - Different sessionId → fresh chat
     *
     * @param sessionId conversation identifier (passed via URL path)
     * @param message user input (passed securely in the HTTP body)
     * @return AI response with contextual memory
     */
    @POST
    @Path("/{sessionId}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String chat(
            @PathParam("sessionId") String sessionId,
            String message) {

        if (sessionId == null || sessionId.isBlank()) {
            throw new IllegalArgumentException("sessionId is required");
        }

        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("message body is required");
        }

        LOG.info("Received message for session '%s': %s".formatted(sessionId, message));

        return ai.chat(sessionId, message);
    }
}