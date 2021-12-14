package edu.gatech.cs6310.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "item")
public class Item implements Display {
    /**
     * Item's unit weight.
     */
    private int unitWeight;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String uid;

    /**
     * Items unique name.
     */
    private String name;

    public void setStore(final Store store) {
        this.store = store;
    }

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "store_id")
    private Store store;

    public int getUnitWeight() {
        return unitWeight;
    }

    public void setUnitWeight(int unitWeight) {
        this.unitWeight = unitWeight;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @param name       The name of this item.
     * @param unitWeight The unit weight.
     */
    public Item(final String name, final int unitWeight) {
        this.name = name;
        this.unitWeight = unitWeight;
        this.uid = UUID.randomUUID().toString();
    }

    /**
     * Default constructor for JPA.
     */
    public Item() {

    }

    /**
     *
     * @return A short string meets the functional requirement.
     */
    public String toSummaryString() {
        return name + "," + unitWeight;
    }
}
