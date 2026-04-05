package org.example.deliveryweb.controller;

import org.example.deliveryweb.repository.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DriverController {

    private final OrderRepository orderRepository;

    public DriverController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/driver/dashboard")
    public String driverPage(Model model) {
        model.addAttribute("availableOrders", orderRepository.findOrdersByStatus("PENDING"));
        return "driver_page";
    }
}