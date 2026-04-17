package org.example.deliveryweb.model;

public record AiResponse(
        String answer,
        String suggestedAction,
        Integer restaurantId
) {
}
