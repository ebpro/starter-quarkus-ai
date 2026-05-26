# Workshop: Building AI Agents with Quarkus & LangChain4j

This starter repository is designed to guide you from executing basic raw prompts to orchestrating a fully agentic system using **Java 25**, **Quarkus**, and the **Quarkus LangChain4j** extension. By the end of this session, you will understand how to seamlessly bridge Large Language Models (LLMs) running locally on your machine (or remotely) with backend code and restfull APIs.

## Prerequisites & Environment Setup

Before starting the code progression, ensure your local development environment is configured.

### System Requirements
* **JDK 25+** configured in your `JAVA_HOME` (use sdkman).
* **Apache Maven** (use the provided `./mvnw` wrapper).
* An IDE (IntelliJ IDEA, Eclipse, or VS Code) with Quarkus plugins recommended.

### Local LLM Engine: Ollama
To ensure data privacy and fast iteration without API costs, we will use **Ollama** to run models locally on your machine.

**1. Install Ollama**
* **Mac/Windows:** Download the installer directly from [ollama.com](https://ollama.com).
* **Linux:** Run the standard install script:
  
```bash
curl -fsSL curl -fsSL https://ollama.com/install.sh | sh | sh
```

**2. Pull the Workshop Model**
Open your terminal and pull a lightweight model suitable for development. We will use a smaller model that is highly capable of function calling and structured output.

```bash
ollama run qwen3:4b
# Or alternatively, if hardware is constrained: ollama run llama3.2
```

test it (adapt model if needed): 

```bash
curl -s http://localhost:11434/api/chat -d '{
  "model": "qwen3:4b",
  "messages": [
    {
      "role": "system",
      "content": "You are a helpful AI assistant."
    },
    {
      "role": "user",
      "content": "Explain Domain-Driven Design in one short sentence."
    }
  ],
  "stream": false
}'|jq
```
*Leave the Ollama server running in the background during the workshop.*

## Running and Testing the Application

### Development Mode

Quarkus shines in Dev Mode. 
Start the application with live reload enabled:

```bash
./mvnw quarkus:dev
```

Once started, Quarkus will automatically connect to your local Ollama instance (defaulting to `http://localhost:11434`).


## The Workshop Progression: The Travel Planner

> [!WARNING]
> **Workshop Methodology: See it, Build it**
>
> We will not be walking through the entire codebase at once. Each step of the AI integration (V1 through V5) will be presented briefly to explain the underlying mechanics.
>
> Once the brief presentation for a step concludes, **you are expected to immediately build and adapt that step within the context of your own project.**

Once the brief presentation for a step concludes, **you are expected to immediately build and adapt that step within the context of your own project.**
:::

This repository is structured into five distinct packages (`v1` through `v5`). Each package represents a paradigm shift in how we integrate LLMs into software architecture.

We will build a **Travel Planner Microservice**, evolving it step-by-step.

### V1: The Basics (Raw Prompting)

**Classes:** `BasicTravelResource`, `BasicTravelAi`

* **Objective:** Establish the foundational connection to the local LLM.
* **Concept:** Sending raw strings to the model and returning raw text.
* **Focus:** Understanding the `@RegisterAiService` annotation and the underlying REST client it generates.

see https://docs.quarkiverse.io/quarkus-langchain4j/dev/prompt-and-template.html

### V2: Structured Output (POJO Mapping)

**Classes:** `StructuredTravelResource`, `StructuredTravelAi`, `TravelPlan` (Record)

* **Objective:** Force the LLM to return deterministic, structured JSON mapped directly to a Java domain object.
* **Concept:** Declarative AI and System prompts.
* **⚠️ Technical Note for Java 25:** When adding validation to your `TravelPlan` record via compact constructors, remember that **the constructor cannot be less visible than the record itself** (e.g., a `public` record cannot have a `private` or package-private compact constructor).

see https://docs.quarkiverse.io/quarkus-langchain4j/dev/guide-prompt-engineering.html

### V3: Conversation (Memory)

**Classes:** `ConversationalTravelResource`, `ConversationalTravelAi`

* **Objective:** Transform a stateless text generator into a contextual chatbot.
* **Concept:** Introducing `@MemoryId` to maintain chat history. You will learn how LangChain4j handles session state invisibly behind the scenes.

see https://docs.quarkiverse.io/quarkus-langchain4j/dev/messages-and-memory.html

### V4: Explicit State Management

**Classes:** `StatefulTravelResource`, `StatefulTravelAi`, `TravelMemory`

* **Objective:** Take control of the framework's magic.
* **Concept:** Pulling memory management back into the application layer using a `ConcurrentHashMap`. This step teaches you the architectural realities of managing state in long-running services before transitioning to an external cache (like Redis).

### V5: Tool Calling (Agents)

**Classes:** `AgenticTravelResource`, `AgenticTravelAi`, `TravelKnowledgeTools`

* **Objective:** Break the LLM out of its isolated sandbox by giving it external capabilities.
* **Concept:** The `@ToolBox` annotation. You will expose local Java methods (like fetching weather or museum hours) that the LLM can autonomously choose to execute to fulfill a user's request.

see https://docs.quarkiverse.io/quarkus-langchain4j/dev/function-calling.html

### V6: (Challenge) RAG 

see https://docs.quarkiverse.io/quarkus-langchain4j/dev/rag.html

### V7: (Challenge) Multi Agents

see https://docs.quarkiverse.io/quarkus-langchain4j/dev/agentic.html