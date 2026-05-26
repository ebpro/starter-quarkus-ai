package fr.univtln.bruno.starter.ai.v5;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * === TOOL LAYER: Java methods callable by the LLM ===
 *
 * Key concept for students:
 * -----------------------------------------------------------
 * Normally, an LLM only generates text.
 * With tools, the LLM can DECIDE to call Java methods at runtime.
 *
 * Flow:
 *   User prompt → LLM reasons → LLM calls tool → Java runs → result sent back → LLM continues
 *
 * This is what makes a system "agentic":
 *   the model takes actions, not just produces words.
 *
 * Design rules for tool methods:
 *   - Parameters must be simple types (String, int, etc.)
 *   - Always annotate params with @P to guide the LLM
 *   - Tool descriptions must be clear and unambiguous
 *   - Return types should be serializable (String, List<String>, etc.)
 */
@ApplicationScoped
public class TravelKnowledgeTools {

    /**
     * Tool #1 — Factual city information
     *
     * Called by the LLM when it needs reliable facts about a city.
     * This prevents hallucination: instead of inventing data,
     * the model delegates to this method.
     *
     * @param city a single city name (e.g. "rome", "paris")
     * @return a short factual summary of the city
     */
    @Tool("Returns key facts about a city: landmarks, culture, and character. Call with a single city name.")
    public String cityInfo(@P("The city name, e.g. 'rome' or 'paris'") String city) {
        return switch (city.toLowerCase().trim()) {
            case "rome"  -> "Rome: home to the Colosseum, Vatican City, Roman Forum. Known for pasta, ancient history, and vibrant street life.";
            case "paris" -> "Paris: Eiffel Tower, Louvre, Notre-Dame. Known for art, fashion, cafés, and French cuisine.";
            case "tokyo" -> "Tokyo: mix of ancient temples (Senso-ji) and futuristic tech. Famous for ramen, sushi, anime culture, and efficient transit.";
            case "new york" -> "New York: Central Park, Statue of Liberty, Broadway. Melting pot of cultures, world-class museums, and iconic skyline.";
            case "barcelona" -> "Barcelona: Sagrada Família, Gothic Quarter, beaches. Known for Gaudí architecture, tapas, and vibrant nightlife.";
            default -> "Unknown city: '%s'. No local data available — using general travel knowledge.".formatted(city);
        };
    }

    /**
     * Tool #2 — Local food recommendations
     *
     * Returns a structured list (List<String>) of local dishes.
     * This shows students that tools can return rich types,
     * not just plain strings — LangChain4j serializes them for the LLM.
     *
     * @param city a single city name
     * @return list of must-try local foods
     */
    @Tool("Returns a list of must-try local foods for a city. Call with a single city name.")
    public List<String> localFoods(@P("The city name, e.g. 'tokyo' or 'barcelona'") String city) {
        return switch (city.toLowerCase().trim()) {
            case "rome"      -> List.of("Cacio e pepe", "Supplì (rice balls)", "Gelato", "Carbonara", "Tiramisu");
            case "paris"     -> List.of("Croissant", "Baguette", "Crêpes", "French onion soup", "Macarons");
            case "tokyo"     -> List.of("Ramen", "Sushi", "Tempura", "Yakitori", "Matcha desserts");
            case "new york"  -> List.of("New York pizza", "Bagel with lox", "Pastrami sandwich", "Cheesecake", "Hot dog");
            case "barcelona" -> List.of("Patatas bravas", "Pan con tomate", "Paella", "Croquetas", "Crema catalana");
            default -> List.of("Local specialties (no specific data for '%s')".formatted(city));
        };
    }
}