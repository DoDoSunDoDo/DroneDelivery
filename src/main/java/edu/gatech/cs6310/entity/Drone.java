package edu.gatech.cs6310.entity;

import javax.persistence.*;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

@Entity
@Table(name = "drone")
public class Drone implements Display {
    /**
     * The Unique ID for this drone.
     */
    @Id
    private String uid;
    private String droneId;
    /**
     * Total capacity this drone can lift.
     */
    private int totalCap;
    /**
     * The orders currently in the drone.
     */
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "assignedDrone",
            orphanRemoval = true)
    @MapKey(name = "uid")
    private Map<String, Order> orders;
    /**
     * Trips left before going to maintainence.
     */
    private int tripsLeft;
    /**
     * The remaining capacity of the drone.
     */
    private int remainCapacity;
    /**
     * The pilot of the drone.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Pilot pilot;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "store_id")
    private Store store;

    /**
     * @param droneId     The unique ID of the drone in that store.
     * @param maxTrips    The maximum trips the drone can
     *                    drive before maintenance.
     * @param maxCapacity The maximum capacity the drone can lift.
     */
    public Drone(final String droneId,
                 final int maxTrips,
                 final int maxCapacity) {
        this.droneId = droneId;
        this.uid = UUID.randomUUID().toString();
        this.tripsLeft = maxTrips;
        this.remainCapacity = maxCapacity;
        this.totalCap = maxCapacity;
        this.orders = new TreeMap<String, Order>();
    }

    /**
     * Default constructor.
     */
    public Drone() {
    }

    public String getDroneId() {
        return droneId;
    }

    public String getUid() {
        return uid;
    }

    public int getTotalCap() {
        return totalCap;
    }

    public void setTotalCap(int totalCap) {
        this.totalCap = totalCap;
    }

    public Map<String, Order> getOrders() {
        return orders;
    }

    public void setOrders(final Map<String, Order> orders) {
        this.orders = orders;
    }

    public int getTripsLeft() {
        return tripsLeft;
    }

    public void setTripsLeft(int tripsLeft) {
        this.tripsLeft = tripsLeft;
    }

    public int getRemainCapacity() {
        return remainCapacity;
    }

    public void setRemainCapacity(int remainCapacity) {
        this.remainCapacity = remainCapacity;
    }

    public Pilot getPilot() {
        return pilot;
    }

    public void setPilot(final Pilot pilot) {
        this.pilot = pilot;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(final Store store) {
        this.store = store;
    }

    /**
     * @param o The order object.
     */
    public void lifeOrder(final Order o) {
        orders.put(o.getUid(), o);
    }

    /**
     * @param weight The order weight of incoming order.
     * @return True if it can lift.
     */
    public boolean isCapable(final int weight) {
        return remainCapacity >= weight;
    }

    /**
     * @return True if it has enough trips left.
     */
    public final boolean hasFuel() {
        return tripsLeft >= 1;
    }

    /**
     * @param weight The order weight of incoming order.
     * @return True it has lifed.
     */
    public boolean lifeWeight(final int weight) {
        if (remainCapacity < weight) {
            return false;
        }
        remainCapacity -= weight;
        return true;
    }

    /**
     * @param aPilot The aPilot for this drone.
     */
    public void assignPilot(final Pilot aPilot) {
        this.pilot = aPilot;
    }

    /**
     * @param orderUID The order name to be delievered.
     */
    public void delivery(final String orderUID) {
        if (tripsLeft >= 1 && pilot != null) {
            tripsLeft -= 1;
            pilot.finishTrip();
            removeOrder(orderUID);
        }
    }

    /**
     * @return True If pilot exists.
     */
    public final boolean isPilotAssigned() {
        return pilot != null;
    }

    /**
     * @param orderUID The order name to be removed.
     */
    public void removeOrder(final String orderUID) {
        Order order = orders.remove(orderUID);
        remainCapacity += order.getTotalWeight();
    }

    /**
     * @return A short string meets the functional requirement.
     */
    public String toSummaryString() {
        String pilotInfo = "";
        if (pilot == null) {  // this one couldn't work?? why??
            pilotInfo = "";
        } else {
            pilotInfo = ",flown_by:" + pilot.getFullName();
        }
        return String.format(
                "droneID:%s,total_cap:%d,num_orders:%d,"
                        + "remaining_cap:%d,trips_left:%d%s",
                droneId,
                totalCap,
                orders.size(),
                remainCapacity,
                tripsLeft,
                pilotInfo);
    }
}
