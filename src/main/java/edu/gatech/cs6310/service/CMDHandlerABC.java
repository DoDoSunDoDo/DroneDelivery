package edu.gatech.cs6310.service;

import edu.gatech.cs6310.entity.*;
import edu.gatech.cs6310.exception.DeliveryServiceException;
import edu.gatech.cs6310.exception.IdentifierAlreadyExistsException;
import edu.gatech.cs6310.exception.IdentifierDoesNotExistException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * CMD Handler Abstract Base Class(ABC).
 * <p>
 * Different sub-classes can have different implementation for Data access.
 * <p>
 * We have this class to follow open–closed principle.
 */
public abstract class CMDHandlerABC implements Display {
    /**
     * The tokens for the full command.
     */
    protected String[] tokens;

    /**
     * Is error message?
     */
    protected String errMsg = "";

    protected String command;

    /**
     * @param fullCMD The full command separated with delimiter ,
     *                e.g. make_store,kroger,33000
     */
    public CMDHandlerABC(final String fullCMD) {
        tokens = parseCommand(fullCMD);
        this.command = fullCMD;
    }

    /**
     * Default constructor for IoC.
     */
    public CMDHandlerABC() {
    }


    /**
     * Clear all data in the data source.
     */
    public abstract void deleteAllData();

    /**
     * add log to database.
     *
     * @param fullCommand The full command string
     * @param type        The type of command
     * @param identity    The identity, e.g. accountName
     */
    public abstract void writeToLog(String fullCommand, String type, String identity);

    /**
     * Parse the full command into tokens.
     *
     * @param fullCMD The full command string with delimiter ,
     * @return Tokens array.
     */
    public String[] parseCommand(final String fullCMD) {
        final String delimiter = ",";
        return fullCMD.split(delimiter);
    }

    abstract boolean isStoreExist(String storeName);

    abstract void saveStore(Store s);


    final void makeStore() throws IdentifierAlreadyExistsException {
        String name = tokens[1];
        if (isStoreExist(name)) {
            throw new IdentifierAlreadyExistsException("store_identifier");
        }
        int revenue = Integer.parseInt(tokens[2]);
        Store s = new Store(name, revenue);
        saveStore(s);
        writeToLog(this.command, "make_store", name);
    }

    abstract String collectStores();

    abstract Store getStore(String storeName)
            throws IdentifierDoesNotExistException;

    final void sellItem() throws DeliveryServiceException {
        String storeName = tokens[1];
        String itemName = tokens[2];
        int itemWeight = Integer.parseInt(tokens[3]);
        Store store = getStore(storeName);
        if (store.getItem(itemName) != null) {
            throw new IdentifierAlreadyExistsException("item_identifier");
        }
        store.sellItem(itemName, itemWeight);
        saveStore(store);
        writeToLog(this.command, "sell_item", storeName);
    }

    final String collectItems() throws IdentifierDoesNotExistException {
        String storeName = tokens[1];
        Store store = getStore(storeName);
        writeToLog(this.command, "display_items", storeName);
        return store.collectItems();
    }

    abstract Boolean isPilotExist(String account);

    abstract Collection<Pilot> getAllPilots();

    abstract void savePilot(Pilot p);

    void makePilot() throws IdentifierAlreadyExistsException {
        String account = tokens[1];
        if (isPilotExist(account)) {
            throw new IdentifierAlreadyExistsException("pilot_identifier");
        }
        String licenseId = tokens[5];
        Pilot p = new Pilot(
                account, tokens[2], tokens[3], tokens[4],
                licenseId, tokens[6],
                Integer.parseInt(tokens[7]));
        for (Pilot pp : getAllPilots()) {
            //System.out.println(pp.getTaxID());    fix in Pilot class
            if (pp.getTaxId().equals(licenseId)) {
                throw new IdentifierAlreadyExistsException("pilot_license");
            }
        }
        savePilot(p);
        writeToLog(this.command, "make_pilot", "");
    }

    abstract String collectPilots();

    final void makeDrone() throws DeliveryServiceException {
        String storeName = tokens[1];
        if (!isStoreExist(storeName)) {
            throw new IdentifierDoesNotExistException("store");
        }

        Store s = getStore(storeName);
        String droneId = tokens[2];
        if (s.duplicateID(droneId)) {
            throw new IdentifierAlreadyExistsException("drone_identifier");
        }
        int maxCapacity = Integer.parseInt(tokens[3]);
        int maxTrips = Integer.parseInt(tokens[4]);
        s.addDrone(droneId, maxCapacity, maxTrips);
        saveStore(s);
        writeToLog(this.command, "make_drone", storeName);
    }

    abstract String collectDrones() throws IdentifierDoesNotExistException;

    abstract Pilot getPilot(String pilotAccount) throws IdentifierDoesNotExistException;

    void saveDrone(final Drone drone) {

    }

    Drone getDrone(final Store store, final String droneId) {
        Drone drone = store.getDrone(droneId);
        return drone;
    }

    @Transactional
    void flyDrone() throws IdentifierDoesNotExistException {
        String storeName = tokens[1];
        Store store = getStore(storeName);
        String droneId = tokens[2];
        Drone drone = getDrone(store, droneId);
        if (drone == null) {
            // throw new
            throw new IdentifierDoesNotExistException("drone");
        }
        String pilotAccount = tokens[3];
        Pilot pilot = getPilot(pilotAccount);
        Pilot prevPilot = drone.getPilot();
        if (prevPilot != null) {
            prevPilot.assignDrone(null);
            savePilot(prevPilot);
        }
        Drone prevDrone = pilot.getDrone();
        if (prevDrone != null) {
            prevDrone.assignPilot(null);
            saveDrone(prevDrone);
        }

        pilot.assignDrone(drone);
        savePilot(pilot);
        drone.assignPilot(pilot);
        saveDrone(drone);
        writeToLog(this.command, "fly_drone", storeName);
    }

    abstract boolean isCustomerExist(String accountName);

    abstract void saveCustomer(Customer c);

    final void makeCustomer() throws IdentifierAlreadyExistsException {
        String accountName = tokens[1];
        if (isCustomerExist(accountName)) {
            throw new IdentifierAlreadyExistsException("customer_identifier");
        }
        Customer c = new Customer(
                accountName, tokens[2], tokens[3], tokens[4],
                Integer.parseInt(tokens[5]), Integer.parseInt(tokens[6]));
        saveCustomer(c);
        writeToLog(this.command, "make_customer", "");
    }

    abstract String collectCustomers();

    abstract Customer getCustomer(String customerAccount) throws IdentifierDoesNotExistException;

    void startOrder() throws DeliveryServiceException {
        String storeName = tokens[1];
        String orderId = tokens[2];
        String droneId = tokens[3];
        String customerAccount = tokens[4];
        Customer customer = getCustomer(customerAccount);
        Store store = getStore(storeName);
        Drone drone = store.getDrone(droneId);
        if (drone == null) {
            // throw new
            throw new IdentifierDoesNotExistException("drone");
        }
        Order order = store.startOrder(orderId, customerAccount);
        order.setAssignedDrone(drone);
        drone.lifeOrder(order);
        customer.addOrder(order);
        saveStore(store);
        saveCustomer(customer);
        writeToLog(this.command, "start_order", storeName);
    }

    String collectOrders() throws IdentifierDoesNotExistException {
        String storeName = tokens[1];
        Store store = getStore(storeName);
        writeToLog(this.command, "display_orders", storeName);
        return store.collectOrders();
    }


    boolean checkCustomer(final ItemLine itemLine, final Order order)
            throws IdentifierDoesNotExistException {
        if (order == null) {
            // throw new
            throw new IdentifierDoesNotExistException("order");
        }
        int totalCost = itemLine.calculateCost() + order.getTotalCost();
        String customerAccount = order.getCustomerAccount();
        Customer customer = getCustomer(customerAccount);
        return customer.isCapable(totalCost);
    }

    final void requestItem() throws DeliveryServiceException {
        String storeName = tokens[1];
        String orderName = tokens[2];
        String itemName = tokens[3];
        int quantity = Integer.parseInt(tokens[4]);
        int unitCost = Integer.parseInt(tokens[5]);
        Store store = getStore(storeName);
        Order order = store.getOrder(orderName);
        if (order == null) {
            throw new IdentifierDoesNotExistException("order");
        }
        Item item = store.getItem(itemName);
        if (item == null) {
            // throw new
            throw new IdentifierDoesNotExistException("item");
        }
        ItemLine itemLine = new ItemLine(item, quantity, unitCost);
        if (order.checkItemExist(itemLine)) {
            throw new DeliveryServiceException("item_already_ordered");
        }
        boolean isCustomerCapable = checkCustomer(itemLine, order);
        if (!isCustomerCapable) {
            throw new DeliveryServiceException("customer_cant_afford_new_item");
        }
        // Check drone's capacity
        int itemWeight = itemLine.calculateWeight();
        Drone drone = order.getAssignedDrone();
        boolean isDroneCapable = drone.isCapable(itemWeight);
        if (!isDroneCapable) {
            throw new DeliveryServiceException("drone_cant_carry_new_item");
        }

        order.addLine(itemLine);
        drone.lifeWeight(itemWeight);
        saveStore(store);
        writeToLog(this.command, "request_item", storeName);
    }

    final Order getOrder(final Store store, final String orderName)
            throws IdentifierDoesNotExistException {
        Order order = store.getOrder(orderName);
        if (order == null) {
            // throw new
            throw new IdentifierDoesNotExistException("order");
        }
        return order;
    }

    final void purchaseOrder() throws DeliveryServiceException {
        String storeName = tokens[1];
        Store store = getStore(storeName);
        String orderName = tokens[2];
        Order order = getOrder(store, orderName);
        if (!order.getAssignedDrone().hasFuel()) {
            throw new DeliveryServiceException("drone_needs_fuel");
        }
        if (!order.getAssignedDrone().isPilotAssigned()) {
            throw new DeliveryServiceException("drone_needs_pilot");
        }
        int totalCost = order.getTotalCost();
        String customerAccount = order.getCustomerAccount();
        Customer customer = getCustomer(customerAccount);
        customer.minusCredit(totalCost);
        customer.removeOrder(order.getUid());
        store.purchaseOrder(orderName, totalCost);
        saveCustomer(customer);
        saveStore(store);
        writeToLog(this.command, "purchase_order", storeName);
    }

    abstract String showLog() throws IdentifierDoesNotExistException;

    final void cancelOrder() throws IdentifierDoesNotExistException {
        Store store = getStore(tokens[1]);
        String orderName = tokens[2];
        Order order = getOrder(store, orderName);
        String customerAccount = order.getCustomerAccount();
        Customer customer = getCustomer(customerAccount);
        customer.removeOrder(order.getUid());
        store.cancelOrder(orderName);
        saveStore(store);
        saveCustomer(customer);
        writeToLog(this.command, "cancel_order", tokens[1]);
    }

    /**
     * Get the command name from the full command.
     *
     * @return The command name.
     */
    private String getCmdName() {
        return tokens[0];
    }

    /**
     * Run the CMD Handler for this command.
     *
     * @return The rc message after running this command.
     */
    public final String run() {
        String cmdName = getCmdName();
        String rcMsg = "";
        try {
            switch (cmdName) {
                case "make_store":
                    makeStore();
                    break;
                case "display_stores":
                    rcMsg = collectStores();
                    break;
                case "sell_item":
                    sellItem();
                    break;
                case "display_items":
                    rcMsg = collectItems();
                    break;
                case "make_pilot":
                    makePilot();
                    break;
                case "display_pilots":
                    rcMsg = collectPilots();
                    break;
                case "make_drone":
                    makeDrone();
                    break;
                case "display_drones":
                    rcMsg = collectDrones();
                    break;
                case "fly_drone":
                    flyDrone();
                    break;
                case "make_customer":
                    makeCustomer();
                    break;
                case "display_customers":
                    rcMsg = collectCustomers();
                    break;
                case "start_order":
                    startOrder();
                    break;
                case "display_orders":
                    rcMsg = collectOrders();
                    break;
                case "request_item":
                    requestItem();
                    break;
                case "purchase_order":
                    purchaseOrder();
                    break;
                case "cancel_order":
                    cancelOrder();
                    break;
                case "show_log":
                    rcMsg = showLog();
                    break;
                case "stop":
                    rcMsg = "stop acknowledged";
                    break;
                case "delete_all":
                    deleteAllData();
                    break;
                default:
                    rcMsg = "command " + cmdName + " NOT acknowledged";
                    break;
            }
        } catch (DeliveryServiceException e) {
            errMsg = e.getMessage();
        }
        String rc;
        if (isStop()) {
            rc = rcMsg;
        } else {
            if (isOK()) {
                rc = "";
                if (isDisplay()) {
                    rc += rcMsg;
                    rc += "OK:display_completed";
                } else {
                    rc += "OK:change_completed";
                }
            } else {
                rc = "ERROR:" + errMsg;
            }
        }
        return rc;
    }

    private boolean isDisplay() {
        return tokens[0].toLowerCase().startsWith("display");
    }

    /**
     * Is it a stop command?
     *
     * @return True if this is a stop command.
     */
    public final boolean isStop() {
        return tokens[0].equals("stop");
    }

    /**
     * @return True if there is no error message.
     */
    public final boolean isOK() {
        return errMsg.isBlank();
    }

    @Override
    public final String toSummaryString() {
        return "Command:" + String.join(",", tokens);
    }

}
