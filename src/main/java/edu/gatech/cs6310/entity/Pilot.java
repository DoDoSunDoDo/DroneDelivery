package edu.gatech.cs6310.entity;


import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "pilot")
public class Pilot extends Employee implements Display {
    /**
     * The unique account for this pilot.
     */
    @Id
    private String uid;
    private String account;
    /**
     * The license ID.
     */
    @Column
    private String licenseId;
    /**
     * The trips this pilot finished.
     */
    @Column
    private int experience;
    /**
     * The drone this pilots drive.
     */
    @OneToOne
    @JoinColumn
    private Drone drone;

    /**
     * Pilot Data Object Constructor.
     */
    public Pilot() {
        account = "";
        licenseId = "";
        uid = UUID.randomUUID().toString();
    }

    /**
     * Pilot Data Object.
     *
     * @param account     The employee's unique account
     * @param firstName   First name.
     * @param lastName    Last Name
     * @param phoneNumber Phone number
     * @param taxId       Tax ID.
     * @param licenseId   License ID.
     * @param experience  The trips this pilot finished.
     */
    public Pilot(final String account,
                 final String firstName,
                 final String lastName,
                 final String phoneNumber,
                 final String taxId,
                 final String licenseId,
                 final int experience) {
        super(firstName, lastName, phoneNumber, taxId);
        this.account = account;
        this.licenseId = licenseId;
        this.experience = experience;
        this.uid = UUID.randomUUID().toString();
    }

    public String getUid() {
        return uid;
    }

    public String getAccount() {
        return account;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }


    /**
     * Assign drone to this pilot.
     *
     * @param aDrone The drone object.
     */
    public void assignDrone(final Drone aDrone) {
        this.drone = aDrone;
    }

    /**
     * @return Drone Object.
     */
    public Drone getDrone() {
        return drone;
    }

    /**
     * Finish trip to get +1 experiences.
     */
    public void finishTrip() {
        experience += 1;
    }

    /**
     * @return A short string meets the functional requirement.
     */
    public String toSummaryString() {
        return String.format(
                "name:%s,phone:%s,taxID:%s,licenseID:%s,experience:%d",
                getFullName(),
                getPhoneNumber(),
                getTaxId(),
                licenseId,
                experience);
    }

    /**
     * @return Return a string for debugging.
     */
    public String toString() {
        String droneInfo = "";
        if (drone != null) {
            droneInfo = drone.toSummaryString();
        }
        return String.format("\n#%s;uid:%s\n%s:\nwith:%s", this.hashCode(), uid,
                toSummaryString(), droneInfo);
    }
}
