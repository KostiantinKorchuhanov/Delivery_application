package org.example.deliveryweb.controller;

import com.example.entity.user.Customer;
import com.example.entity.user.Driver;
import com.example.entity.user.User;
import com.example.validation.GeneralValidation;
import com.example.validation.subvalidation.PasswordValidation;
import org.example.deliveryweb.repository.CustomerRepository;
import org.example.deliveryweb.repository.DriverRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    private final CustomerRepository customerRepository;
    private final DriverRepository driverRepository;
    private final GeneralValidation generalValidation = new GeneralValidation();

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
            @RequestParam("role") String role,
            @RequestParam("username") String username,
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("passwordHash") String passwordHash,
            @RequestParam("passwordRepeat") String passwordRepeat) {

        if (!generalValidation.validatePasswordMatch(passwordHash, passwordRepeat)) {
            return "registration";
        }

        String hashed = PasswordValidation.hashPassword(passwordHash);

        if ("driver".equalsIgnoreCase(role)) {
            Driver driver = new Driver();
            fillData(driver, username, name, surname, email, phoneNumber, hashed);
            driverRepository.save(driver);
        } else {
            Customer customer = new Customer();
            fillData(customer, username, name, surname, email, phoneNumber, hashed);
            customerRepository.save(customer);
        }

        return "redirect:/login?success";
    }

    private void fillData(User user, String username, String name, String surname, String email, String phoneNumber, String passwordHash) {
        user.setUsername(username);
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setPasswordHash(passwordHash);
    }
}