package org.example.deliveryweb.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("cartSize")
    public int getCartSize(HttpSession session) {
        List<?> cart = (List<?>) session.getAttribute("cart");
        if (cart != null) {
            return cart.size();
        }
        return 0;
    }
}