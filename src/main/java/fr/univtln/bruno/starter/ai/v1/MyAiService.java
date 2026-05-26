package fr.univtln.bruno.starter.ai.v1;

import jakarta.enterprise.context.ApplicationScoped;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

/**
 * === BASIC QUARKUS + LANGCHAIN4J CONCEPTS (V1) ===
 *
 * <p>This example demonstrates the simplest way to integrate an LLM into a Quarkus application
 * using the Quarkus LangChain4j extension.
 *
 * <h3>1. AI SERVICE AS A CDI BEAN</h3>
 * <p>The interface is turned into a managed Quarkus bean using:
 * <ul>
 *   <li>{@link RegisterAiService} → declares this interface as an AI service</li>
 *   <li>{@link ApplicationScoped} → one shared instance for the application</li>
 * </ul>
 *
 * <p>Quarkus automatically generates the implementation at runtime.
 * There is no need to write a concrete class or manually call an LLM API.
 *
 * <h3>2. DECLARATIVE AI (NO INFRASTRUCTURE CODE)</h3>
 * <p>Instead of writing:
 * <ul>
 *   <li>HTTP calls to OpenAI / other LLM providers</li>
 *   <li>JSON parsing logic</li>
 *   <li>Prompt orchestration code</li>
 * </ul>
 *
 * <p>We only declare:
 * <ul>
 *   <li>What the model should do (@SystemMessage)</li>
 *   <li>What the user asks (@UserMessage)</li>
 *   <li>The method signature (input/output contract)</li>
 * </ul>
 *
 * <h3>3. SYSTEM MESSAGE (MODEL BEHAVIOR CONTROL)</h3>
 * <p>{@link SystemMessage} defines global behavior for the LLM:
 * it acts like a "permanent instruction" applied to every request.
 *
 * <p>In this V1 version, we ask the model to produce JSON-like output,
 * but we do NOT enforce strict structure validation.
 *
 * <h3>4. USER MESSAGE (DYNAMIC INPUT)</h3>
 * <p>{@link UserMessage} defines the runtime prompt template.
 * It injects method parameters (topic, days) into the prompt.
 *
 * <p>This is how Quarkus + LangChain4j binds Java method arguments to LLM prompts.
 *
 * <h3>5. OUTPUT MODEL (RAW STRING)</h3>
 * <p>The return type is {@code String}, meaning:
 * <ul>
 *   <li>The response is raw LLM output</li>
 *   <li>No parsing into Java objects</li>
 *   <li>No guarantee that the result is valid JSON</li>
 * </ul>
 *
 * <h3>6. WHY THIS VERSION EXISTS</h3>
 * <p>This V1 approach is used to:
 * <ul>
 *   <li>Understand how LLMs behave without constraints</li>
 *   <li>Observe variability in generated outputs</li>
 *   <li>Motivate structured output in later versions (DTO mapping)</li>
 * </ul>
 *
 * <p><b>Next step (V2):</b> introduce a Java record (TravelPlan) and let the framework
 * automatically map JSON into a structured object.
 */
@RegisterAiService
@ApplicationScoped
@SystemMessage("""
You are a helpful travel assistant.

You generate travel itineraries in JSON format.
Follow the JSON structure.
""")
public interface MyAiService {

    /**
     * Generates a travel plan in JSON format as raw text.
     *
     * <p>Important concept (V1):
     * - The output is NOT mapped to a Java object
     * - It is returned as a String
     * - JSON correctness is not guaranteed by the framework
     *
     * <p>This helps students observe why structured output is needed later.
     *
     * @param topic travel destination or theme (e.g. "Paris", "Japan")
     * @param days number of days for the itinerary
     * @return travel plan as JSON string (unvalidated)
     */
    @UserMessage("""
Create a travel plan in JSON format.

Destination: {topic}
Days: {days}

Return a JSON with:
- destination
- days
- itinerary (day, activities, food)
""")
    String plan(String topic, int days);
}