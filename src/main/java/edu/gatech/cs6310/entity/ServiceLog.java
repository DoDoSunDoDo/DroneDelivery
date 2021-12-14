package edu.gatech.cs6310.entity;

import edu.gatech.cs6310.JsonProperties;
import edu.gatech.cs6310.dao.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ServiceLog {

    private final Set<String> adminCommands = Set.of("display_stores", "make_store", "display_customers",
            "display_pilots", "show_log", "stop",
            "make_pilot", "make_customer");
    private final Set<String> storeCommands = Set.of("make_store", "sell_item", "display_items",
            "make_drone", "display_drones", "fly_drone",
            "start_order", "display_orders", "request_item",
            "purchase_order", "cancel_order");
    private LogRepository logRepository;
    private String command;

//    @Autowired
    private JsonProperties prop;

    /**
     * Default constructor.
     *
     * @param logRepository The log JPA repository.
     * @param prop         Config file.
     */
    @Autowired
    public ServiceLog(final LogRepository logRepository, final JsonProperties prop) {
        this.logRepository = logRepository;
        this.prop = prop;
    }


    /**
     * Constructor.
     *
     * @param command The command
     */
    public ServiceLog(final String command) {
        this.command = command;
    }

    /**
     * Default constructor.
     */
    public ServiceLog() {
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(final String command) {
        this.command = command;
    }

    /**
     * get all existing log.
     * @return log string
     */
    public String getAllLog() {
        StringBuffer sb = new StringBuffer();
        for (LogDO log : logRepository.findAll()) {
            sb.append(log.toSummaryString());
        }
        return sb.toString();
    }

    /**
     * get Admin log.
     * @return log string
     */
    public String getAdminLog() {
        StringBuffer sb = new StringBuffer();
        for (LogDO log : logRepository.findByType("Admin")) {
            sb.append(log.toSummaryString());
        }
        return sb.toString();
    }

    /**
     * get store log.
     *
     * @param identifier Log's identifier.
     * @return log string
     */
    public String getStoreLog(final String identifier) {
        StringBuffer sb = new StringBuffer();
        for (LogDO log : logRepository.findByTypeAndIdentifier("Store", identifier)) {
            sb.append(log.toSummaryString());
        }
        return sb.toString();
    }

    /**
     * Print all existing log.
     */
    public void printAllLog() {
        for (LogDO log : logRepository.findAll()) {
            System.out.println(log);
        }
    }

    /**
     * Print Admin log.
     */
    public void printAdminLog() {
        for (LogDO log : logRepository.findByType("Admin")) {
            System.out.println(log);
        }
    }

    /**
     * Print store log.
     *
     * @param identifier Log's identifier.
     */
    public void printStoreLog(final String identifier) {
        for (LogDO log : logRepository.findByTypeAndIdentifier("Store", identifier)) {
            System.out.println(log);
        }
    }

    private void addAdminLog(final String identifier) {
        LogDO logDO = new LogDO(command);
        logDO.setType("Admin");
        logRepository.saveAndFlush(logDO);
    }

    private void addStoreLog(final String identifier) {
        LogDO logDO = new LogDO(command);
        logDO.setType("Store");
        logDO.setIdentifier(identifier);
        logRepository.saveAndFlush(logDO);
    }

    /**
     * Add command to log.
     *
     * @param type       The command type
     * @param identifier The identifier, e.g. customer accountName
     */
    public final void addLog(final String type, final String identifier) {
        boolean enableLog = prop.getEnable();
        if (!enableLog) {
            return;
        }
        if (adminCommands.contains(type)) {
            addAdminLog(identifier);
            return;
        }
        if (storeCommands.contains(type)) {
            addStoreLog(identifier);
            return;
        }
    }
}
