package fr.univtln.bruno.starter.ai.v4;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import fr.univtln.bruno.starter.ai.TravelPlan;
import io.quarkiverse.langchain4j.RegisterAiService;

/**
 * === V4 AI SERVICE ===
 *
 * <h2>Important evolution from V3</h2>
 * We no longer rely on chat memory.
 *
 * <h2>New role of the LLM</h2>
 * The model is now a:
 * → pure "TravelPlan generator"
 *
 * <h2>Key concept</h2>
 * - No conversation state inside the model
 * - Each call is independent
 * - Context is provided explicitly by the application
 */
@RegisterAiService
@SystemMessage("""
    You are a travel planner. /no_think

    RULES:
    - Always return a COMPLETE TravelPlan as valid JSON
    - Never omit any field
    - Generate realistic, varied activities and local food
    """)
public interface TravelPlannerAi {

    /**
     * Generates a structured travel plan.
     *
     * <h2>Important concept</h2>
     * The prompt is fully controlled by the application,
     * including previous state (if any).
     *
     * @param message user request (possibly enriched with previous state)
     * @param days number of travel days
     * @return structured TravelPlan (mapped automatically)
     */
    @UserMessage("""
        Create a {days}-day travel plan for: {message}

        Return ONLY this JSON structure, no explanation:
        {
          "destination": "city name",
          "days": {days},
          "itinerary": [
            {
              "day": 1,
              "activities": ["activity 1", "activity 2", "activity 3"],
              "food": "realistic local meal description"
            }
          ]
        }

        The itinerary array must have exactly {days} entries.
        """)
    TravelPlan plan(String message, int days);
}