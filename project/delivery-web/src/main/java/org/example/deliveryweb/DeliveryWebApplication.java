package org.example.deliveryweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;import org.springframework.boot.autoconfigure.domain.EntityScan;import org.springframework.context.annotation.ComponentScan;import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.example.entity")
@EnableJpaRepositories(basePackages = {"org.example.deliveryweb.repository"})
@ComponentScan(basePackages = {"com.example", "org.example.deliveryweb"})
public class DeliveryWebApplication {
	public static void main(String[] args) {
		SpringApplication.run(DeliveryWebApplication.class, args);
	}
}
