package edu.gatech.cs6310;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "edu.gatech.cs6310",
        "edu.gatech.cs6310.dao",
        "edu.gatech.cs6310.entity"})
public class DeliveryServiceApplication {

    /**
     * Run the Sprint boot app.
     *
     * @param args
     */
    public static void main(final String[] args) {
        SpringApplication.run(DeliveryServiceApplication.class, args);
    }
}
