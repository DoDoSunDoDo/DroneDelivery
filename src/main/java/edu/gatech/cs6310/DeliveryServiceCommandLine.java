package edu.gatech.cs6310;

import java.util.Scanner;
import edu.gatech.cs6310.service.CMDHandlerSpring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication(scanBasePackages = {
//        "edu.gatech.cs6310",
//        "edu.gatech.cs6310.dao",
//        "edu.gatech.cs6310.entity"})
public class DeliveryServiceCommandLine implements CommandLineRunner {

    private CMDHandlerSpring cmdHandler;

    @Autowired
    DeliveryServiceCommandLine(final CMDHandlerSpring cmdHandler) {
        this.cmdHandler = cmdHandler;
    }

    /**
     * Run the Sprint boot app.
     *
     * @param args
     */
    public static void main(final String[] args) {
        SpringApplication.run(DeliveryServiceCommandLine.class, args);
    }

    @Override
    public void run(final String... args) {
        Scanner commandLineInput = new Scanner(System.in);
        String wholeInputLine;
        System.out.println("Welcome to the Grocery Express Delivery Service!");

        while (commandLineInput.hasNextLine()) {
            // Determine the next command and
            // echo it to the monitor for testing purposes
            wholeInputLine = commandLineInput.nextLine();
            System.out.println("> " + wholeInputLine);
            String rc = cmdHandler.run(wholeInputLine);
            System.out.println(rc);

            if (cmdHandler.isStop()) {
                break;
            }
        }

        System.out.println("simulation terminated");
        commandLineInput.close();
    }
}
