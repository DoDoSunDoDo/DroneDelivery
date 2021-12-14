package edu.gatech.cs6310.exception;

public class IdentifierAlreadyExistsException extends DeliveryServiceException {
    /**
     * @param objectName The object name that Identifier already existed.
     */
    public IdentifierAlreadyExistsException(final String objectName) {
        super(objectName + "_already_exists");
    }
}
