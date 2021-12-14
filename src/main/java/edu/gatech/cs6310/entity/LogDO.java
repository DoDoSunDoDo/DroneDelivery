package edu.gatech.cs6310.entity;

import javax.persistence.*;

@Entity
@Table(name = "log")
public class LogDO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int logId;

    private String command;

    private String type;

    private String identifier;

    public int getLogId() {
        return logId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(final String command) {
        this.command = command;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return command;
    }

    /**
     * Get log.
     * @return log string
     */
    public String toSummaryString() {
        return command;
    }

    /**
     * Default constructor.
     */
    public LogDO() { }

    /**
     * Constructor with command.
     * @param command The command
     */
    public LogDO(final String command) {
        this.command = command;
    }
}
