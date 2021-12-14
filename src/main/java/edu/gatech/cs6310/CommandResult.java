package edu.gatech.cs6310;

public class CommandResult {
    /**
     * The command the controller has received.
     */
    private final String command;

    /**
     * The result content after the command runs.
     */
    private final String returnContent;

    /** This is object to be returned to client.
     *
     * @param command The command has received.
     * @param returnContent The return content for this command.
     */
    public CommandResult(final String command, final String returnContent) {
        this.command = command;
        this.returnContent = returnContent;
    }

    /**
     *
     * @return The command of the result.
     */
    public final String getCommand() {
        return command;
    }

    /**
     *
     * @return The return content of the result.
     */
    public final String getReturnContent() {
        return returnContent;
    }


}
