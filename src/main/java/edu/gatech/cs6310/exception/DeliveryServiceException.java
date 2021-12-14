package edu.gatech.cs6310.exception;

public class DeliveryServiceException extends Exception {
    /**
     * The base Exception for Delivery Service.
     *
     * @param err The error message of the exception
     */
    public DeliveryServiceException(final String err) {
        super(err);
    }
}
