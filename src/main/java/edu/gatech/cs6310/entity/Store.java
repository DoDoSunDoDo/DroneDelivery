package edu.gatech.cs6310.entity;

import edu.gatech.cs6310.exception.IdentifierAlreadyExistsException;

import javax.persistence.*;
import java.util.Map;
import java.util.TreeMap;

@Entity
@Table(name = "store")
public class Store implements Display {
    /**
     * The name of the store.
     */
    @Id
    private String name;
    /**
     * The drones belong to this store.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "store")
    @MapKey(name = "uid")
    private Map<String, Drone> drones;
    /**
     * The orders in progress in this store.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "store",
            orphanRemoval = true)
    @MapKey(name = "uid")
    private Map<String, Order> orders;

    /**
     * The current items in this store.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "store")
    @MapKey(name = "uid")
    private Map<String, Item> items;
    /**
     * The store's revenue.
     */
    @Column
    private int revenue;


    /**
     * @param name           The store name.
     * @param initialRevenue The initial initialRevenue.
     */
    public Store(final String name, final int initialRevenue) {
        this.name = name;
        this.revenue = initialRevenue;
        drones = new TreeMap<String, Drone>();
        orders = new TreeMap<String, Order>();
        items = new TreeMap<String, Item>();
    }

    /**
     * Store Constructor.
     */
    public Store() {

    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Map<String, Drone> getDrones() {
        return drones;
    }

    public void setDrones(final Map<String, Drone> drones) {
        this.drones = drones;
    }

    public Map<String, Order> getOrders() {
        return orders;
    }

    public void setOrders(final Map<String, Order> orders) {
        this.orders = orders;
    }

    public Map<String, Item> getItems() {
        return items;
    }

    public void setItems(final Map<String, Item> items) {
        this.items = items;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    /**
     * @return A short string meets the functional requirement.
     */
    @Override
    public String toString() {
        return "name:" + name + ",revenue:" + revenue;
    }


    /**
     * Build a temporary tree map for display.
     *
     * @return A TrepMap with sorted Order based on name.
     */
    public TreeMap<String, Drone> buildSortedDrones() {
        TreeMap<String, Drone> tempOrders = new TreeMap<>();
        drones.forEach((k, v) -> {
            tempOrders.put(v.getDroneId(), v);
        });
        return tempOrders;
    }

    /**
     * Add Drone to this store.
     *
     * @param droneId     The unique ID of the drone.
     * @param maxCapacity The maximum Capacity.
     * @param maxTrips    The maximum Trip of this drone.
     * @return Added Drone.
     */
    public Drone addDrone(final String droneId,
                          final int maxCapacity,
                          final int maxTrips) {
        Drone d = new Drone(droneId, maxTrips, maxCapacity);
        drones.put(d.getUid(), d);
        d.setStore(this);
        return d;
    }

    /**
     * Check current store has used the drone's ID.
     *
     * @param droneId The drone's ID.
     * @return Check if the drone has duplicate ID>
     */
    public boolean duplicateID(final String droneId) {
        TreeMap<String, Drone> tempOrders = buildSortedDrones();
        return tempOrders.containsKey(droneId);
    }

    /**
     * @param orderId         The order ID
     * @param customerAccount The Customer account
     * @return The order started.
     */
    public Order startOrder(final String orderId,
                            final String customerAccount)
            throws IdentifierAlreadyExistsException {
        Order order = getOrder(orderId);
        if (order != null) {
            throw new IdentifierAlreadyExistsException("order_identifier");
        }
        Order o = new Order(orderId, customerAccount);
        orders.put(o.getUid(), o);
        o.setStore(this);
        return o;
    }

    /**
     * Purchase Order.
     *
     * @param orderName The order name
     * @param totalCost The total cost of the order.
     */
    public void purchaseOrder(final String orderName, final int totalCost) {
        Order order = getOrder(orderName);
        Drone drone = order.getAssignedDrone();
        drone.delivery(order.getUid());
        orders.remove(order.getUid());
        addRevenue(totalCost);
    }

    /**
     * Cancle the order.
     *
     * @param orderName The unique order name.
     */
    public void cancelOrder(final String orderName) {
        Order order = getOrder(orderName);
        Drone drone = order.getAssignedDrone();
        drone.removeOrder(order.getUid());
        orders.remove(order.getUid());
    }

    /**
     * @param droneId The drone's UID.
     * @return The drone for this UID.
     */
    public Drone getDrone(final String droneId) {
        TreeMap<String, Drone> tempDrones = buildSortedDrones();
        return tempDrones.get(droneId);
    }

    /**
     * @param orderName The order name.
     * @return The order object.
     */
    public Order getOrder(final String orderName) {
        TreeMap<String, Order> tempOrders = buildSortedOrders();
        return tempOrders.get(orderName);
    }

    /**
     * @param itemName Item name
     * @return The item object.
     */
    public Item getItem(final String itemName) {
        TreeMap<String, Item> tempItems = buildSortedItems();
        return tempItems.get(itemName);
    }

    /**
     * Add the item in the store's selling list.
     *
     * @param itemName   The item name
     * @param itemWeight The item weight.
     * @return The item added.
     */
    public Item sellItem(final String itemName, final int itemWeight) {
        Item it = new Item(itemName, itemWeight);
        items.put(itemName, it);
        it.setStore(this);
        return it;
    }

    /**
     * Add revenue to this store after the order finished.
     *
     * @param orderRevenue The order's revenue
     */
    public void addRevenue(final int orderRevenue) {
        revenue += orderRevenue;
    }

    /**
     * Collect all drones' info.
     *
     * @return The string for all drone's info.
     */
    public String collectDrones() {
        StringBuffer sb = new StringBuffer();
        TreeMap<String, Drone> tempDrones = buildSortedDrones();
        tempDrones.forEach((k, v) -> {
            sb.append(v.toSummaryString());
            appendNewLineDelimiter(sb);
        });
        return sb.toString();
    }

    /**
     * Build a temporary tree map for display.
     *
     * @return A TrepMap with sorted Order based on name.
     */
    public TreeMap<String, Order> buildSortedOrders() {
        TreeMap<String, Order> tempOrders = new TreeMap<String, Order>();
        orders.forEach((k, v) -> {
            tempOrders.put(v.getOrderId(), v);
        });
        return tempOrders;
    }

    /**
     * Collect all orders' info.
     *
     * @return The string for all orders' info.
     */
    public String collectOrders() {
        StringBuffer sb = new StringBuffer();
        TreeMap<String, Order> tempOrders = buildSortedOrders();
        tempOrders.forEach((k, v) -> {
            sb.append(v.toSummaryString());
            appendNewLineDelimiter(sb);
        });
        return sb.toString();
    }

    /**
     * Build a temporary tree map for display.
     *
     * @return A TrepMap with sorted Order based on name.
     */
    public TreeMap<String, Item> buildSortedItems() {
        TreeMap<String, Item> tempItems = new TreeMap<String, Item>();
        items.forEach((k, v) -> {
            tempItems.put(v.getName(), v);
        });
        return tempItems;
    }

    /**
     * Collect all items' info.
     *
     * @return The string for all items' info.
     */
    public String collectItems() {
        StringBuffer sb = new StringBuffer();
        TreeMap<String, Item> tempItems = buildSortedItems();
        tempItems.forEach((k, v) -> {
            sb.append(v.toSummaryString());
            appendNewLineDelimiter(sb);
        });
        return sb.toString();
    }

    /**
     * @return A short string meets the functional requirement.
     */
    public String toSummaryString() {
        return "name:" + name + ",revenue:" + revenue;
    }

}
