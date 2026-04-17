package org.example.deliveryweb.model;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface AiAssistant {
    @SystemMessage("""
            You are a dedicated support assistant for FoodApp.
            
            STRICT RULES:
            1. Answer ONLY based on the provided context: {{appContext}}
            2. If the user asks about something outside of FoodApp (e.g., world history, coding, other apps), 
               politely refuse and say you can only help with FoodApp services and say what you can help with.
            3. Respond ONLY in English.
            4. Always output valid JSON.
            """)
    AiResponse chat(@V("appContext") String appContext, @UserMessage String userMessage);
}