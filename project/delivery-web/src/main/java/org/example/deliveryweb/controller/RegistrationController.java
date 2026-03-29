package org.example.deliveryweb.controller;

import com.example.entity.user.Customer;
import com.example.entity.user.Driver;
import com.example.entity.user.User;
import com.example.validation.GeneralValidation;
import com.example.validation.subvalidation.PasswordValidation;
import org.example.deliveryweb.repository.CustomerRepository;
import org.example.deliveryweb.repository.DriverRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {

    private final CustomerRepository customerRepository;
    private final DriverRepository driverRepository;
    private final GeneralValidation generalValidation =   new GeneralValidation();

    public RegistrationController(CustomerRepository customerRepository, DriverRepository driverRepository) {
        this.customerRepository = customerRepository;
        this.driverRepository = driverRepository;
    }

    @GetMapping("/register")
    public String showForm() {
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String role,
            @RequestParam String username,
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String email,
            @RequestParam String phoneNumber,
            @RequestParam String passwordHash,
            @RequestParam String passwordRepeat) {

        if (!generalValidation.validatePasswordMatch(passwordHash, passwordRepeat)) {
            return "registration";
        }
        if ("driver".equalsIgnoreCase(role)) {
            Driver driver = new Driver();
            fillData(driver, username, name, surname, email, phoneNumber, PasswordValidation.hashPassword(passwordHash));
            driverRepository.save(driver);
        } else {
            Customer customer = new Customer();
            fillData(customer, username, name, surname, email, phoneNumber,  PasswordValidation.hashPassword(passwordHash));
            customerRepository.save(customer);
        }

        return "redirect:/register?success";
    }

    private void fillData(User user, String username, String name, String surname,
                          String email, String phoneNumber, String passwordHash) {
        user.setUsername(username);
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setPasswordHash(passwordHash);
    }
}