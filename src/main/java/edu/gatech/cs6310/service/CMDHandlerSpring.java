package edu.gatech.cs6310.service;

import edu.gatech.cs6310.JsonProperties;
import edu.gatech.cs6310.dao.*;
import edu.gatech.cs6310.entity.*;
import edu.gatech.cs6310.exception.IdentifierDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CMDHandlerSpring extends CMDHandlerABC {

    private final CustomerRepository customerRepo;

    private final PilotRepository pilotRepo;

    private final StoreRepository storeRepo;

    private final ServiceLog serviceLog;

    private final DroneRepository droneRepo;

    private final ItemRepository itemRepo;

    private final ItemLineRepository itemLineRepo;

    private final OrderRepository orderRepo;

    private final JsonProperties prop;

    private final LogRepository logRepo;

    /**
     * Have a CMD Handler for Spring IoC.
     *
     * @param customerRepo  Customer JPA Repository.
     * @param pilotRepo     Pilot JPA Repository.
     * @param storeRepo     Store JPA Repository.
     * @param droneRepo     Drone JPA Repository.
     * @param itemRepo      Item JPA Repository.
     * @param itemLineRepo  ItemLine JPA Repository.
     * @param orderRepo     Order JPA Repository.
     * @param logRepo      Log JPA Repository.
     * @param prop         Config file.
     * @param serviceLog   The log service.
     */
    @Autowired
    public CMDHandlerSpring(final CustomerRepository customerRepo,
                            final PilotRepository pilotRepo,
                            final StoreRepository storeRepo,
                            final DroneRepository droneRepo,
                            final ItemRepository itemRepo,
                            final ItemLineRepository itemLineRepo,
                            final OrderRepository orderRepo,
                            final LogRepository logRepo,
                            final JsonProperties prop,
                            final ServiceLog serviceLog) {
        this.customerRepo = customerRepo;
        this.pilotRepo = pilotRepo;
        this.storeRepo = storeRepo;
        this.droneRepo = droneRepo;
        this.itemRepo = itemRepo;
        this.itemLineRepo = itemLineRepo;
        this.orderRepo = orderRepo;
        this.serviceLog = serviceLog;
        this.logRepo = logRepo;
        this.prop = prop;
    }

    /**
     * Clear All data in the repo.
     */
    public final void deleteAllData() {
        this.itemLineRepo.deleteAll();
        this.itemRepo.deleteAll();
        this.orderRepo.deleteAll();
        this.droneRepo.deleteAll();
        this.customerRepo.deleteAll();
        this.pilotRepo.deleteAll();
        this.storeRepo.deleteAll();
    }

    final Customer getCustomer(final String customerAccount)
            throws IdentifierDoesNotExistException {
        Customer customer = customerRepo.findById(customerAccount).orElse(null);
        if (customer == null) {
            // throw new
            throw new IdentifierDoesNotExistException("customer");
        }
        return customer;
    }

    /**
     * Run the Command and collect results.
     *
     * @param fullCMD The full command string.
     * @return The result message.
     */
    public final String run(final String fullCMD) {
        tokens = parseCommand(fullCMD);
        this.command = fullCMD;
        String rc = super.run();
        return rc;
    }

    /**
     * add log to database.
     *
     * @param fullCommand The full command string
     * @param type        The type of command
     * @param identity    The identity, e.g. accountName
     */
    public final void writeToLog(final String fullCommand, final String type, final String identity) {
        serviceLog.setCommand(fullCommand);
        serviceLog.addLog(type, identity);
    }

    final boolean isStoreExist(final String storeName) {
        return storeRepo.existsById(storeName);
    }

    final void saveStore(final Store s) {
        storeRepo.saveAndFlush(s);
    }


    final String collectStores() {
        StringBuffer sb = new StringBuffer();
        storeRepo.findAll(Sort.by(Sort.Direction.ASC, "name")).forEach((store) -> {
            sb.append(store.toSummaryString());
            appendNewLineDelimiter(sb);
        });
        return sb.toString();
    }


    final Store getStore(final String storeName)
            throws IdentifierDoesNotExistException {
        Store store = storeRepo.findById(storeName).orElse(null);
        if (store == null) {
            throw new IdentifierDoesNotExistException("store");
        }
        return store;
    }

    Drone getDrone(final Store store, final String droneId) {
        Drone drone = store.getDrone(droneId);
        if (drone == null) {
            return null;
        }
        return droneRepo.findById(drone.getUid()).orElse(null);
    }


    final Boolean isPilotExist(final String account) {
        return pilotRepo.existsByAccount(account);
    }

    final Collection<Pilot> getAllPilots() {
        return pilotRepo.findAll();
    }

    final void savePilot(final Pilot p) {
        pilotRepo.saveAndFlush(p);
    }

    final String collectPilots() {
        StringBuffer sb = new StringBuffer();
        pilotRepo.findAll(Sort.by(Sort.Direction.ASC, "account")).forEach((pilot) -> {
            sb.append(pilot.toSummaryString());
            appendNewLineDelimiter(sb);
        });
        writeToLog(this.command, "display_pilots", "");
        return sb.toString();
    }

    final String collectDrones() throws IdentifierDoesNotExistException {
        String storeName = tokens[1];
        if (!storeRepo.existsById(storeName)) {
            throw new IdentifierDoesNotExistException("store");
        }
        Store s = getStore(storeName);
        return s.collectDrones();
    }

    final Pilot getPilot(final String pilotAccount)
            throws IdentifierDoesNotExistException {
        List<Pilot> pilots = pilotRepo.findByAccount(pilotAccount);
        if (pilots.size() == 0) {
            // throw new
            throw new IdentifierDoesNotExistException("pilot");
        }
        return pilots.get(0);
    }

    final void saveDrone(final Drone drone) {
        droneRepo.saveAndFlush(drone);
    }

    final boolean isCustomerExist(final String accountName) {
        return customerRepo.existsById(accountName);
    }

    final void saveCustomer(final Customer c) {
        customerRepo.saveAndFlush(c);
    }

    final String collectCustomers() {
        StringBuffer sb = new StringBuffer();
        customerRepo.findAll(Sort.by(Sort.Direction.ASC, "accountName")).forEach((c) -> {
            sb.append(c.toSummaryString());
            appendNewLineDelimiter(sb);
        });
        writeToLog(this.command, "display_customers", "");
        return sb.toString();
    }

    final String showLog() throws IdentifierDoesNotExistException {
        ServiceLog log = new ServiceLog(logRepo, prop);
        if (tokens.length == 1) {
            return log.getAllLog();
        }
        String type = tokens[1];
        if (type.equals("Admin")) {
            return log.getAdminLog();
        }
        String identifier = tokens[2];
        if (type.equals("Store")) {
            Store store = storeRepo.findById(identifier).orElse(null);
            if (store == null) {
                throw new IdentifierDoesNotExistException("store");
            }
            return log.getStoreLog(identifier);
        }
        return "";
    }

}
