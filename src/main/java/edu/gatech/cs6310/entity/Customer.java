package edu.gatech.cs6310.entity;

import edu.gatech.cs6310.exception.DeliveryServiceException;

import javax.persistence.*;
import java.util.Map;
import java.util.TreeMap;

@Entity
@Table(name = "customer")
public class Customer extends User implements Display {
    /**
     * Customers have a rating as an integer from one (1) to five (5).
     * <p>
     * where a higher number indicates that
     * the customer has been more reliable in ordering items over time
     */
    @Column
    private int ratings;
    /**
     * The orders this customer has set.
     */

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "customer",
            orphanRemoval = true)
    @MapKey(name = "uid")
    private Map<String, Order> orders;
    /**
     * Every customer should have a unique account name.
     */
    @Id
    private String accountName;
    /**
     * The number of dollars that they have to request orders.
     */
    @Column
    private int credit;

    /**
     * Customer Constructor.
     *
     * @param uAccountName   The unique account name
     * @param firstName      customer's first name
     * @param lastName       customer's last name
     * @param phoneNumber    customer's phone number
     * @param initialRatings customer's initial ratings, rating from 1 to 5.
     * @param initialCredit  customer's initial credit $.
     */
    public Customer(final String uAccountName,
                    final String firstName,
                    final String lastName,
                    final String phoneNumber,
                    final int initialRatings,
                    final int initialCredit) {
        super(firstName, lastName, phoneNumber);
        this.accountName = uAccountName;
        this.ratings = initialRatings;
        this.credit = initialCredit;
        this.orders = new TreeMap<>();
    }

    /**
     * Default constructor for the error.
     * No default constructor for entity:  : edu.gatech.cs6310.entity.CustomerDO;
     * nested exception is org.hibernate.InstantiationException:
     */
    public Customer() {

    }

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public Map<String, Order> getOrders() {
        return orders;
    }

    public void setOrders(final Map<String, Order> orders) {
        this.orders = orders;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(final String accountName) {
        this.accountName = accountName;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    /**
     * Check if the customer is capable to pre-authorize the requestCredit.
     *
     * @param requestCredit The requesting order's Credit.
     * @return True if there is enough credit, otherwise false.
     */
    public boolean isCapable(final int requestCredit) {
        return this.credit >= requestCredit;
    }

    /**
     * Deduct the requestCredit after the order is done.
     *
     * @param requestCredit The requesting order's Credit.
     * @return True if there is enough credit, otherwise false.
     */
    public boolean minusCredit(final int requestCredit) {
        if (this.credit < requestCredit) {
            return false;
        }
        this.credit -= requestCredit;
        return true;
    }

    /**
     * Add the order into the customer.
     *
     * @param o The order object.
     */
    public void addOrder(final Order o) throws DeliveryServiceException {
        if (minusCredit(o.getTotalCost())) {
            orders.put(o.getUid(), o);
        } else {
            throw new DeliveryServiceException(
                    "customer_cant_afford_new_item");
        }
    }

    /**
     * Remove the order.
     *
     * @param orderUID The unique order id string.
     */
    public void removeOrder(final String orderUID) {
        orders.remove(orderUID);
    }

    /**
     * @return A short string meets the functional requirement.
     */
    public String toSummaryString() {
        return String.format("name:%s,phone:%s,rating:%d,credit:%d",
                getFullName(),
                getPhoneNumber(),
                ratings,
                credit);
    }

}
