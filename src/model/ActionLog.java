package model;

public class ActionLog {
    private int id;
    private int userId;
    private String actionDescription;
    private String timestamp;

    /**
     * Get log id.
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Set log id.
     *
     * @param id log id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the id of the user who performed the action.
     *
     * @return user id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Set the user id associated with this action.
     *
     * @param userId user id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Get the action description.
     *
     * @return description text
     */
    public String getActionDescription() {
        return actionDescription;
    }

    /**
     * Set the action description.
     *
     * @param actionDescription description text
     */
    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }

    /**
     * Get the timestamp for the action log.
     *
     * @return timestamp string
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Set the timestamp for the action log.
     *
     * @param timestamp timestamp string
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
