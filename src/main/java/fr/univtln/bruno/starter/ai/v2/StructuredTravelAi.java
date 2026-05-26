package fr.univtln.bruno.starter.ai.v2;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import fr.univtln.bruno.starter.ai.TravelPlan;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * LangChain4j AI service (V2 - STRUCTURED OUTPUT version).
 *
 * <h3>Core concept introduced in V2</h3>
 * The model output is no longer treated as plain text.
 * It is expected to match a structure that can be mapped to a Java record ({@link TravelPlan}).
 *
 * <h3>What changes compared to V1</h3>
 * <ul>
 *   <li>V1 → String output (manual parsing / unsafe)</li>
 *   <li>V2 → Java object output (automatic mapping)</li>
 * </ul>
 *
 * <h3>How it works in Quarkus + LangChain4j</h3>
 * <ol>
 *   <li>Java method defines return type (TravelPlan)</li>
 *   <li>LLM generates JSON</li>
 *   <li>Framework parses JSON → TravelPlan automatically</li>
 * </ol>
 *
 * <h3>Important idea for students</h3>
 * You are no longer "parsing AI output".
 * You are defining a contract and letting the framework enforce structure.
 */
@RegisterAiService
@ApplicationScoped
@SystemMessage("""
You are a deterministic JSON generator for travel plans.

RULES:
- Output ONLY valid JSON
- No markdown, no explanations, no extra text
- Do NOT invent fields
- Keep all text short (max 6 words per activity)
- Use only these activity types:
  sightseeing, museum, walking tour, restaurant, nature, shopping, relaxation
- If unsure, choose popular tourist destinations

OUTPUT FORMAT:
{
  "destination": string,
  "days": number,
  "itinerary": [
    {
      "day": number,
      "activities": [string],
      "food": string
    }
  ]
}

If output is invalid JSON, it is incorrect.
""")
public interface StructuredTravelAi {

    /**
     * Generates a structured travel plan using AI.
     *
     * <h3>Key concept: Declarative AI</h3>
     * The method signature defines:
     * <ul>
     *   <li>Input (topic, days)</li>
     *   <li>Output (TravelPlan)</li>
     * </ul>
     *
     * <h3>Framework responsibility</h3>
     * LangChain4j + Quarkus handles:
     * <ul>
     *   <li>Prompt execution</li>
     *   <li>JSON generation</li>
     *   <li>Deserialization into TravelPlan</li>
     * </ul>
     *
     * <h3>Result</h3>
     * A strongly typed Java object instead of raw text.
     */
    @UserMessage("""
INPUT:
destination_request = {topic}
number_of_days = {days}

TASK:
Generate a travel plan using the required JSON format.
""")
    TravelPlan plan(String topic, int days);
}