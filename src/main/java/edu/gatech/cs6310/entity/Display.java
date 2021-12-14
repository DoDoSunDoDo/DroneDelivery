package edu.gatech.cs6310.entity;


/**
 * Display interface.
 */
public interface Display {
    /**
     * @return A Summary String the object.
     * <p>
     * Useful for both CLI and REST API.
     */
    String toSummaryString();

    /**
     * Add a new line to the String Buffer.
     *
     * @param sb String Buffer
     */
    default void appendNewLineDelimiter(StringBuffer sb) {
        sb.append(System.getProperty("line.separator"));
    }

}
