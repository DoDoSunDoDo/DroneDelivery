package edu.gatech.cs6310.entity;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "itemline")
@Entity
public class ItemLine implements Display {

    @Id
    private String uid;

    /**
     * The item object for this line.
     */
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "item_id")
    private Item item;
    /**
     * The item's unit cost.
     */
    private int unitCost;
    /**
     * The item's quantity.
     */
    private int quantity;

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "order_id")
    private Order order;

    /**
     * @param item     The items for this line.
     * @param quantity The quantity of the items.
     * @param unitCost The unit cost of the items.
     */
    public ItemLine(final Item item, final int quantity, final int unitCost) {
        this.item = item;
        this.quantity = quantity;
        this.unitCost = unitCost;
        this.uid = UUID.randomUUID().toString();
    }

    /**
     * Default constructor.
     */
    public ItemLine() {
    }

    /**
     * @return The name of the item.
     */
    public String getName() {
        return item.getName();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(final String lineId) {
        this.uid = lineId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(final Item item) {
        this.item = item;
    }

    public int getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(int unitCost) {
        this.unitCost = unitCost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return The weight of this item line.
     */
    public int calculateWeight() {
        return quantity * item.getUnitWeight();
    }

    /**
     * @return The cost of this item line.
     */
    public int calculateCost() {
        return quantity * unitCost;
    }

    /**
     * @return A short string meets the functional requirement.
     */
    public String toSummaryString() {
        return String.format(
                "item_name:%s,total_quantity:%d,total_cost:%d,total_weight:%d",
                item.getName(),
                quantity,
                calculateCost(),
                calculateWeight());
    }
}
