package edu.gatech.cs6310;

import edu.gatech.cs6310.service.CMDHandlerSpring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
// https://spring.io/blog/2015/06/08/cors-support-in-spring-framework
// https://spring.io/guides/gs/rest-service-cors/
@CrossOrigin(origins = "http://localhost:3001")
public class DeliveryServiceREST {

    private static final Logger LOG = LoggerFactory.getLogger(DeliveryServiceREST.class);


    private CMDHandlerSpring cmdHandler;

    @Autowired
    DeliveryServiceREST(final CMDHandlerSpring cmdHandler) {
        this.cmdHandler = cmdHandler;
    }

    /**
     * @param command The command received from the client.
     * @return The command results for the client.
     */
    @GetMapping("/delivery_service")
    public final CommandResult processCommand(
            @RequestParam(value = "command",
                    defaultValue = "") final String command) {
        String returnContent = cmdHandler.run(command);
        return new CommandResult(command, returnContent);
    }
}
