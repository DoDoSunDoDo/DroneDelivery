package edu.gatech.cs6310.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class User {
    /**
     * The first name of the user.
     */
    @Column(length = 32)
    private String firstName;

    /**
     * The last name of the user.
     */
    @Column(length = 32)
    private String lastName;

    /**
     * The phone number of the user.
     */
    @Column(length = 32)
    private String phoneNumber;

    /**
     * @param firstName   User's first name
     * @param lastName    User's last name
     * @param phoneNumber User's phone number
     */
    public User(final String firstName,
                final String lastName,
                final String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Default constructor for JPA.
     */
    public User() {

    }

    /**
     * @return The user's phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @return The full name of this user.
     */
    public String getFullName() {
        return firstName + "_" + lastName;
    }


    public String getLastName() {
        return lastName;
    }
}
