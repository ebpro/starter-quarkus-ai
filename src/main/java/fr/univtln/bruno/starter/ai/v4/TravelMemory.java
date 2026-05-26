package fr.univtln.bruno.starter.ai.v4;

import fr.univtln.bruno.starter.ai.TravelPlan;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * === V4: EXPLICIT APPLICATION MEMORY ===
 *
 * <h2>Goal</h2>
 * This class replaces implicit LLM memory with explicit server-side state.
 *
 * <h2>Key idea (important for students)</h2>
 * Unlike V3 (LangChain4j chat memory), here WE manage memory ourselves.
 *
 * <h2>Architecture concept</h2>
 * - sessionId → key for conversation state
 * - TravelPlan → stored structured result (not raw text)
 *
 * <h2>Why ConcurrentHashMap?</h2>
 * - Quarkus is multi-threaded
 * - REST requests are concurrent
 * - Map must be thread-safe
 *
 * <h2>Result</h2>
 * We move from "LLM remembers context" → "application stores state".
 */
@ApplicationScoped               // one instance for the entire application lifecycle
public class TravelMemory {

    /**
     * Thread-safe in-memory storage of travel plans per session.
     *
     * <p>This is NOT persistent storage.
     * Restarting the app clears all memory.
     */
    private final Map<String, TravelPlan> store =
            new ConcurrentHashMap<>();

    /**
     * Retrieves the last known travel plan for a session.
     *
     * @param sessionId unique conversation identifier
     * @return previous TravelPlan or null if first request
     */
    public TravelPlan get(String sessionId) {
        return store.get(sessionId);
    }

    /**
     * Stores the latest travel plan for a session.
     *
     * <p>Each new request overwrites the previous state.
     *
     * @param sessionId conversation identifier
     * @param plan generated travel plan
     */
    public void save(String sessionId, TravelPlan plan) {
        store.put(sessionId, plan);
    }
}