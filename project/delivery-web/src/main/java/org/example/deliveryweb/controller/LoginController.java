package org.example.deliveryweb.controller;

import com.example.entity.user.Customer;
import com.example.entity.user.Driver;
import com.example.validation.subvalidation.PasswordValidation;
import org.example.deliveryweb.repository.CustomerRepository;
import org.example.deliveryweb.repository.DriverRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    private final CustomerRepository customerRepository;
    private final DriverRepository driverRepository;

    public LoginController(CustomerRepository customerRepository, DriverRepository driverRepository) {
        this.customerRepository = customerRepository;
        this.driverRepository = driverRepository;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {

        Optional<Driver> driver = driverRepository.findDriverByUsernameOrEmail(username, username);
        if (driver.isPresent() && PasswordValidation.validatePassword(driver.get().getPasswordHash(), password)) {
            return "redirect:/driver/dashboard";
        }

        Optional<Customer> customer = customerRepository.findCustomerByUsernameOrEmail(username, username);
        if (customer.isPresent() && PasswordValidation.validatePassword(customer.get().getPasswordHash(), password)) {
            return "redirect:/customer/home";
        }

        return "redirect:/login?error";
    }
}