package fr.univtln.bruno.starter.ai.v4;

import fr.univtln.bruno.starter.ai.TravelPlan;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

/**
 * === V4: HYBRID ARCHITECTURE (SOTA CONCEPT) ===
 *
 * <h2>Key idea</h2>
 * We now explicitly control memory in the application layer.
 *
 * <h2>Architecture shift from V3</h2>
 * V3: LLM + hidden chat memory
 * V4: Application = source of truth
 *
 * <h2>Benefits</h2>
 * - deterministic behavior
 * - full control of state
 * - easier debugging
 * - no hidden prompt injection
 */
@Path("/api/v4/travel")
public class TravelPlannerResource {

    @Inject TravelPlannerAi ai;
    @Inject TravelMemory memory;

    /**
     * Generates or updates a travel plan with explicit state.
     *
     * <h2>Flow</h2>
     * 1. Load previous state (if exists)
     * 2. Build enriched prompt
     * 3. Call LLM statelessly
     * 4. Save updated state
     *
     * @param sessionId conversation identifier
     * @param message user request
     * @param days trip duration
     * @return updated TravelPlan
     */
    @POST
    public TravelPlan chat(
            @QueryParam("sessionId") String sessionId,
            @QueryParam("message") String message,
            @QueryParam("days") int days) {

        TravelPlan previous = memory.get(sessionId);

        // Build enriched context manually (key V4 concept)
        String prompt = (previous == null)
                ? message
                : """
                  Previous destination: %s
                  Previous itinerary (days summary): %s
                  New request: %s
                  """.formatted(
                previous.destination(),
                previous.itinerary().stream()
                        .map(d -> "Day " + d.day())
                        .toList(),
                message);

        TravelPlan result = ai.plan(prompt, days);

        memory.save(sessionId, result);

        return result;
    }
}