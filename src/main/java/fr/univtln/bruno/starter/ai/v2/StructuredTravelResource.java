package fr.univtln.bruno.starter.ai.v2;

import fr.univtln.bruno.starter.ai.TravelPlan;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestQuery;

/**
 * REST resource (V2) introducing STRUCTURED OUTPUT with LangChain4j + Quarkus.
 *
 * <p>This version improves V1 by introducing a key concept:
 * the AI response is no longer raw text, but a mapped Java object ({@link TravelPlan}).
 *
 * <h3>Key improvement over V1</h3>
 * <ul>
 *   <li>V1 → returns raw JSON string (unreliable, untyped)</li>
 *   <li>V2 → returns a typed Java object (TravelPlan)</li>
 * </ul>
 *
 * <h3>What Quarkus + LangChain4j does here</h3>
 * <ul>
 *   <li>Calls the LLM</li>
 *   <li>Receives JSON from the model</li>
 *   <li>Automatically deserializes JSON → TravelPlan</li>
 * </ul>
 *
 * <h3>Result</h3>
 * The REST API becomes fully type-safe and returns structured JSON directly.
 */
@Path("/api/v1/travel")
@Produces(MediaType.APPLICATION_JSON)
public class StructuredTravelResource {

    /**
     * AI service injected by Quarkus CDI.
     * The implementation is generated automatically at runtime.
     */
    @Inject
    StructuredTravelAi aiService;

    /**
     * Generates a structured travel plan using AI.
     *
     * <p>This method demonstrates the benefit of structured output:
     * the controller does not deal with JSON anymore, only Java objects.
     *
     * @param topic travel destination or theme
     * @param days number of days (default = 3)
     * @return a fully structured {@link TravelPlan}
     *
     * @throws BadRequestException if topic is empty
     */
    @GET
    public TravelPlan plan(
            @RestQuery String topic,
            @RestQuery @DefaultValue("3") int days
    ) {
        if (topic == null || topic.isBlank()) {
            throw new BadRequestException("topic cannot be empty");
        }

        return aiService.plan(topic, days);
    }
}