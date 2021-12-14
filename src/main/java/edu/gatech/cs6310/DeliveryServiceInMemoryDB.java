package edu.gatech.cs6310;

import edu.gatech.cs6310.service.CMDHandlerInMemoryData;

import java.util.Scanner;

public class DeliveryServiceInMemoryDB {

    /**
     * The main loop to process input command.
     * It reads the command from CLI and process the command.
     */
    public final void commandLoop() {
        Scanner commandLineInput = new Scanner(System.in);
        String wholeInputLine;

        System.out.println("Welcome to the Grocery Express Delivery Service!");
        CMDHandlerInMemoryData cmd;
        while (commandLineInput.hasNextLine()) {
            // Determine the next command and
            // echo it to the monitor for testing purposes
            wholeInputLine = commandLineInput.nextLine();
            System.out.println("> " + wholeInputLine);
            cmd = new CMDHandlerInMemoryData(wholeInputLine);
            String rc = cmd.run();
            System.out.println(rc);

            if (cmd.isStop()) {
                break;
            }
        }

        System.out.println("simulation terminated");
        commandLineInput.close();
    }
}
