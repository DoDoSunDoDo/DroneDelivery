package edu.gatech.cs6310.entity;

import javax.persistence.*;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

// The table name 'order' can not be used.
// Otherwise, we see Error
// Caused by: org.h2.jdbc.JdbcSQLSyntaxErrorException: Syntax error in SQL statement
// "DROP TABLE IF EXISTS ORDER[*] CASCADE "; expected "identifier"; SQL statement:
@Table(name = "\"ORDER\"")
@Entity
//@IdClass(OrderKey.class)
public class Order implements Display {

    //    Have a unique if in database.
//    Order should be store specific.
    @Id
//    @GeneratedValue(generator = "system-uuid")
//    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String uid;

    private String orderId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "order")
    @MapKey(name = "uid")
    private Map<String, ItemLine> linesMap;
    private int totalCost;
    //    Performance tuning for LinesMap.
//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @OrderBy
//    private List<ItemLine> itemLine;
    private int totalWeight;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "drone_id")
    private Drone assignedDrone;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "store_id")
    private Store store;
    /**
     * The customer account for this order.
     */
    private String customerAccount;

    /**
     * Order Constructor.
     */
    public Order() {
        this.uid = UUID.randomUUID().toString();
    }

    /**
     * @param orderId         The order ID
     * @param customerAccount The customer unique account.
     */
    public Order(final String orderId, final String customerAccount) {
        this.orderId = orderId;
        this.customerAccount = customerAccount;
        this.linesMap = new TreeMap<String, ItemLine>();
        this.uid = UUID.randomUUID().toString();
    }

    public String getUid() {
        return uid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(final String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return Customer's account for this order.
     */
    public String getCustomerAccount() {
        return customerAccount;
    }

//    public List<ItemLine> getItemLine() {
//        return itemLine;
//    }
//
//    public void setItemLine(final List<ItemLine> itemLine) {
//        this.itemLine = itemLine;
//    }

    public Map<String, ItemLine> getLinesMap() {
        return linesMap;
    }

    public void setLinesMap(final Map<String, ItemLine> linesMap) {
        this.linesMap = linesMap;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(int totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Drone getAssignedDrone() {
        return assignedDrone;
    }

    public void setAssignedDrone(final Drone assignedDrone) {
        this.assignedDrone = assignedDrone;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(final Customer customer) {
        this.customer = customer;
    }

    public Store getStore() {
        return store;
    }

    /**
     * Set Store info.
     *
     * @param store
     */
    public void setStore(final Store store) {
        this.store = store;
    }

    /**
     * Add one item line to the order.
     *
     * @param il ItemLine object.
     */
    public void addLine(final ItemLine il) {
        linesMap.put(il.getUid(), il);
        il.setOrder(this);
        totalCost += il.calculateCost();
        totalWeight += il.calculateWeight();
    }

    /**
     * @param il ItemLine
     * @return If this order has the item name.
     */
    public final boolean checkItemExist(final ItemLine il) {
        TreeMap<String, ItemLine> tempMap = buildSortedItemLines();
        return tempMap.containsKey(il.getName());
    }

    /**
     * Build a sorted items line based on item name.
     * @return a sorted items line.
     */
    public TreeMap<String, ItemLine> buildSortedItemLines() {
        TreeMap<String, ItemLine> tempMap = new TreeMap<>();
        linesMap.forEach((k, v) -> {
            tempMap.put(v.getName(), v);
        });
        return tempMap;
    }

    @Override
    public final String toSummaryString() {
        StringBuffer sb = new StringBuffer();
        sb.append("orderID:" + orderId);
        TreeMap<String, ItemLine> tempMap = buildSortedItemLines();
        tempMap.forEach((name, il) -> {
            appendNewLineDelimiter(sb);
            sb.append(il.toSummaryString());
        });
        return sb.toString();
    }

    /**
     *
     * @return A debug message.
     */
    public String toString() {
        return String.format("uid:%s;%s", uid, toSummaryString());
    }

    /**
     * Display the itemLines's info.
     *
     * @return itemLines's name.
     */
    public Set<String> getItemLineName() {
        return linesMap.keySet();
    }

}
