package edu.gatech.cs6310.service;

import edu.gatech.cs6310.entity.*;
import edu.gatech.cs6310.exception.IdentifierDoesNotExistException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

/**
 * CMD Hanlder with in memory data storage.
 * <p>
 * Used for initial assignment.
 */
public class CMDHandlerInMemoryData extends CMDHandlerABC {
    /**
     * The Customers in this system.
     */
    private static final TreeMap<String, Customer> CUSTOMERS =
            new TreeMap<>();
    /**
     * The STORES in this system.
     */
    private static final TreeMap<String, Store> STORES =
            new TreeMap<>();
    /**
     * The pilot in this system.
     */
    private static final TreeMap<String, Pilot> PILOTS =
            new TreeMap<>();

    private static final ArrayList<String> LOG = new ArrayList<>();

    /**
     * @param fullCMD The full command separated with delimiter ,
     *                e.g. make_store,kroger,33000
     */
    public CMDHandlerInMemoryData(final String fullCMD) {
        super(fullCMD);
    }

    /**
     * add log to database.
     * @param fullCommand     The full command string
     * @param type            The type of command
     * @param identity        The identity, e.g. accountName
     */
    public final void writeToLog(final String fullCommand, final String type, final String identity) {
        LOG.add(fullCommand);
    }

    /**
     * Clear All global/Static data.
     * <p>
     * Unit Tests or restart cases may need to clear all static data.
     */
    public final void deleteAllData() {
        CUSTOMERS.clear();
        STORES.clear();
        PILOTS.clear();
    }

    final boolean isStoreExist(final String storeName) {
        return STORES.containsKey(storeName);
    }

    final void saveStore(final Store s) {
        STORES.put(s.getName(), s);
    }

    final String collectStores() {
        StringBuffer sb = new StringBuffer();
        STORES.forEach((name, store) -> {
            sb.append(store.toSummaryString());
            appendNewLineDelimiter(sb);
        });
        writeToLog(this.command, "display_stores", "");
        return sb.toString();
    }

    final Store getStore(final String storeName)
            throws IdentifierDoesNotExistException {
        Store store = STORES.get(storeName);
        if (store == null) {
            throw new IdentifierDoesNotExistException("store");
        }
        return store;
    }


    final Boolean isPilotExist(final String account) {
        return PILOTS.containsKey(account);
    }

    final Collection<Pilot> getAllPilots() {
        return PILOTS.values();
    }

    final void savePilot(final Pilot p) {
        PILOTS.put(p.getAccount(), p);
    }

    final String collectPilots() {
        StringBuffer sb = new StringBuffer();
        PILOTS.forEach((name, pilot) -> {
            sb.append(pilot.toSummaryString());
            appendNewLineDelimiter(sb);
        });
        writeToLog(this.command, "display_pilots", "");
        return sb.toString();
    }

    final String collectDrones() throws IdentifierDoesNotExistException {
        String storeName = tokens[1];
        if (!STORES.containsKey(storeName)) {
            throw new IdentifierDoesNotExistException("store");
        }
        Store s = STORES.get(storeName);
        return s.collectDrones();
    }

    final Pilot getPilot(final String pilotAccount) throws IdentifierDoesNotExistException {
        Pilot pilot = PILOTS.get(pilotAccount);
        if (pilot == null) {
            // throw new
            throw new IdentifierDoesNotExistException("pilot");
        }
        return pilot;
    }

    final boolean isCustomerExist(final String accountName) {
        return CUSTOMERS.containsKey(accountName);
    }

    final void saveCustomer(final Customer c) {
        CUSTOMERS.put(c.getAccountName(), c);
    }

    final String collectCustomers() {
        StringBuffer sb = new StringBuffer();
        CUSTOMERS.forEach((k, v) -> {
            sb.append(v.toSummaryString());
            appendNewLineDelimiter(sb);
        });
        writeToLog(this.command, "display_customers", "");
        return sb.toString();
    }

    final Customer getCustomer(final String customerAccount)
        throws IdentifierDoesNotExistException {
        Customer customer = CUSTOMERS.get(customerAccount);
        if (customer == null) {
            // throw new
            throw new IdentifierDoesNotExistException("customer");
        }
        return customer;
    }

    final String showLog() {
        StringBuffer sb = new StringBuffer();
        for (String log : LOG) {
            sb.append(log);
        }
        return sb.toString();
    }
}
