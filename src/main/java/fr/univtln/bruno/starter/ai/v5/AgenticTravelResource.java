package fr.univtln.bruno.starter.ai.v5;

import fr.univtln.bruno.starter.ai.TravelPlan;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestQuery;

/**
 * === V5 REST API: Agentic Travel Planner ===
 *
 * Design principle:
 * The HTTP layer stays intentionally thin.
 * All AI complexity (tool orchestration, structured output) is
 * handled by TravelPlannerAi — this resource just exposes it.
 *
 * @RunOnVirtualThread: LLM calls are blocking I/O.
 * Virtual threads (Project Loom) let Quarkus handle many
 * concurrent requests without blocking platform threads.
 */
@Path("/api/v5/travel")
@Produces(MediaType.APPLICATION_JSON)
public class AgenticTravelResource {

    // Constructor injection (preferred over @Inject for testability)
    private final AgenticTravelAi ai;

    public AgenticTravelResource(AgenticTravelAi ai) {
        this.ai = ai;
    }

    /**
     * GET /api/v5/travel?destination=Tokyo&days=3
     *
     * @param destination city name (required, non-blank)
     * @param days        number of days (optional, default 3, minimum 1)
     * @return AI-generated structured travel plan as JSON
     */
    @GET
    @RunOnVirtualThread
    public TravelPlan plan(
            @RestQuery @NotBlank String destination,
            @RestQuery @DefaultValue("3") @Min(1) int days
    ) {
        return ai.plan(destination, days);
    }
}