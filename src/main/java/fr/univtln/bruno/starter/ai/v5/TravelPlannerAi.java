package fr.univtln.bruno.starter.ai.v5;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import fr.univtln.bruno.starter.ai.TravelPlan;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.ToolBox;

/**
 * === V5: AGENTIC AI SERVICE ===
 *
 * What makes this "agentic"?
 * -----------------------------------------------------------
 * The LLM is not just filling a template.
 * It REASONS about which tools to call, in what order,
 * and integrates the results before producing its final output.
 *
 * Execution flow for plan("Tokyo", 3):
 *   1. LLM receives the user prompt
 *   2. LLM decides to call cityInfo("tokyo")    → Java executes → result returned
 *   3. LLM decides to call localFoods("tokyo")  → Java executes → result returned
 *   4. LLM uses both results to build a TravelPlan
 *   5. LangChain4j deserializes the JSON output into TravelPlan
 *
 * Note: @RegisterAiService is @RequestScoped by default.
 * No memory needed here since each plan() call is independent.
 */
@RegisterAiService
@SystemMessage("""
    You are an expert travel planner. Your job is to create detailed,
    realistic travel itineraries using the tools available to you.

    TOOL USAGE:
    - Always call cityInfo with the destination name first.
    - Always call localFoods with the destination name to get food suggestions.
    - Pass only a plain city name string to tools (e.g. "tokyo", not "Tokyo, Japan").

    OUTPUT REQUIREMENTS:
    - destination: must match the requested city
    - days: must match the requested number of days exactly
    - itinerary: must contain exactly the requested number of day entries
    - Each DayPlan must have a non-empty activities list and a non-null food field
    """)
public interface TravelPlannerAi {

    /**
     * Plans a multi-day itinerary for a given destination.
     *
     * @param destination the city to visit (e.g. "Tokyo", "Paris")
     * @param days        the number of days (drives itinerary length)
     * @return a structured TravelPlan with daily activities and food
     *
     * Note on @V: explicitly binds method parameters to template variables.
     * This is required when the parameter name might not survive compilation.
     */
    @ToolBox(TravelKnowledgeTools.class)
    @UserMessage("""
        Plan a {days}-day trip to {destination}.

        Steps:
        1. Call cityInfo("{destination}") to get city facts.
        2. Call localFoods("{destination}") to get food recommendations.
        3. Build a day-by-day itinerary using that information.

        Return a complete TravelPlan with exactly {days} entries in the itinerary.
        """)
    TravelPlan plan(@V("destination") String destination, @V("days") int days);
}