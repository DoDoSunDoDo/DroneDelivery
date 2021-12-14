package edu.gatech.cs6310.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class Employee extends User {

    /**
     * @return The Tax ID for this employee.
     */
    public String getTaxId() {
        return taxId;
    }

    /**
     * Tax Identifier for this employee.
     */
    @Column(length = 32)
    private String taxId;
    /**
     * Employee's salary.
     */
    private float salary;
    /**
     * Start date. The salary will be increased for longer service time.
     */
    private Date startDate;

    /**
     * Default constructor for JPA.
     */
    public Employee() {
    }

    /**
     *
     * @param firstName The first name
     * @param lastName The last name
     * @param phoneNumber The phone number
     * @param taxId The unique Tax ID
     */
    public Employee(final String firstName,
                    final String lastName,
                    final String phoneNumber,
                    final String taxId) {
        super(firstName, lastName, phoneNumber);
        this.taxId = taxId;
    }

    final float accrueWorkingMonths() {
        throw new java.lang.UnsupportedOperationException("Not implement yet");
    }
}
