package edu.gatech.cs6310.exception;

public class IdentifierDoesNotExistException extends DeliveryServiceException {
    /**
     * @param objectName The object name that Identifier does not existed.
     */
    public IdentifierDoesNotExistException(final String objectName) {
        super(objectName + "_identifier_does_not_exist");
    }
}
