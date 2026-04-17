package org.example.deliveryweb.controller;

import com.example.entity.order.Orders;
import com.example.entity.user.Driver;
import jakarta.servlet.http.HttpSession;
import org.example.deliveryweb.repository.DriverRepository;
import org.example.deliveryweb.repository.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DriverController {

    private final OrderRepository orderRepository;
    private final DriverRepository driverRepository;

    public DriverController(OrderRepository orderRepository, DriverRepository driverRepository) {
        this.orderRepository = orderRepository;
        this.driverRepository = driverRepository;
    }

    @GetMapping("/driver/dashboard")
    public String driverPage(Model model, HttpSession session) {
        Driver currentDriver = (Driver) session.getAttribute("user");
        if (currentDriver == null) return "redirect:/login";
        Driver driverFromDb = driverRepository.findById(currentDriver.getUserId()).orElse(currentDriver);
        model.addAttribute("driver", driverFromDb);
        model.addAttribute("availableOrders", orderRepository.findOrdersByStatus("PENDING"));
        return "driver_page";
    }

    @PostMapping("/driver/order/accept")
    public String acceptOrder(@RequestParam("orderId") int orderId, HttpSession session) {
        Driver sessionDriver = (Driver) session.getAttribute("user");
        if (sessionDriver == null) return "redirect:/login";
        Driver driver = driverRepository.findById(sessionDriver.getUserId()).get();
        if (driver.getCurrentOrder() != null) {
            return "redirect:/driver/dashboard?error=busy";
        }
        Orders order = orderRepository.findById(orderId).orElse(null);
        if (order != null && "PENDING".equals(order.getStatus())) {
            order.setStatus("ACCEPTED");
            order.setDriver(driver);
            driver.setCurrentOrder(order);
            orderRepository.save(order);
            driverRepository.save(driver);
        }
        return "redirect:/driver/dashboard";
    }

    @PostMapping("/driver/order/deliver")
    public String deliverOrder(HttpSession session) {
        Driver driver = (Driver) session.getAttribute("user");
        Driver driverDb = driverRepository.findById(driver.getUserId()).orElseThrow();
        Orders order = driverDb.getCurrentOrder();
        if (order != null) {
            order.setStatus("DELIVERED");
            orderRepository.save(order);
        }
        return "redirect:/driver/dashboard";
    }

    @GetMapping("/driver/order/details/{id}")
    public String orderDetails(@PathVariable("id") int id, Model model, HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";

        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));

        model.addAttribute("order", order);
        return "available_order_details";
    }

    @GetMapping("/driver/order/current")
    public String currentOrderDetails(Model model, HttpSession session) {
        Driver sessionDriver = (Driver) session.getAttribute("user");
        if (sessionDriver == null) return "redirect:/login";
        Driver driver = driverRepository.findById(sessionDriver.getUserId()).orElseThrow();
        if (driver.getCurrentOrder() == null) {
            return "redirect:/driver/dashboard";
        }
        model.addAttribute("order", driver.getCurrentOrder());
        return "current_order_details";
    }
}