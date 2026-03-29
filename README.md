# The project is built using a Maven multi-module architecture:
- ```core``` – Shared JPA/Hibernate entities, services, and core business logic.
- ```delivery-web``` – Spring Boot interface (Thymeleaf) for customer and driver registration.
- ```delivery-desktop``` – Administrative and Restaurant Owner dashboard built with JavaFX.

# To run this project, you need:
- Java 21
- MySQL Create a database named ```foodApp```)
- Spring-boot

# To launch the system, follow these steps:
**Web Portal (Registration):**
- Navigate to delivery-web module.
- Run the DeliveryWebApplication.java file.
- Open your browser at: ```http://localhost:8080/register``` and enjoy with almoust empty page because I am not an HTML programmer
  <img width="1408" height="768" alt="jojo" src="https://github.com/user-attachments/assets/f4f5364f-f9cf-4b1f-a0e0-bffbbb6fc421" />

**Desktop App (Management):**
- Navigate to delivery-desktop module.
- Run the Main.java file.
