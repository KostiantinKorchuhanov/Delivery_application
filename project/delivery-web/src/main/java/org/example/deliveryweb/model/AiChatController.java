package org.example.deliveryweb.model;

import jakarta.annotation.PostConstruct;
import org.example.deliveryweb.repository.RestaurantRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public class AiChatController {

    private final AiAssistant aiAssistant;
    private final RestaurantRepository restaurantRepository;
    private String cachedContext;

    public AiChatController(AiAssistant aiAssistant, RestaurantRepository restaurantRepository) {
        this.aiAssistant = aiAssistant;
        this.restaurantRepository = restaurantRepository;
    }

    @PostConstruct
    public void initContext() {
        String restaurantsInfo = restaurantRepository.findAll().stream()
                .map(r -> String.format("Name: %s, Description: %s, Address: %s",
                        r.getRestaurantName(),
                        r.getDescription(),
                        r.getAddress()
                ))
                .collect(Collectors.joining("\n"));

        this.cachedContext = """
                FOODAPP KNOWLEDGE BASE:
                - Navigation: 'Home' for restaurants, 'My Orders' for history, 'Cart' for checkout, 'Profile' for user information and account information modification/deletion.
                - Logic: 10% of order price is returned as bonuses.
                - Review: Customer can leave a review to the order and rate the driver after the order completion; Customer can rate a restaurant and leave a review in any time.
                
                RESTAURANTS LIST:
                """ + restaurantsInfo;

        System.out.println("I am not stupid, because context loaded successfully with " + restaurantRepository.count() + " restaurants.");
    }

    @GetMapping("/api/ai/chat")
    public AiResponse chat(@RequestParam("message") String message) {
        try {
            return aiAssistant.chat(cachedContext, message);
        } catch (Exception e) {
            return new AiResponse("Error: " + e.getMessage(), "NONE", 0);
        }
    }
}