package model;

public class LoginLog {
    private int id;
    private int userId;
    private String actionType;
    private String timestamp;

    /**
     * Get the login log id.
     *
     * @return log id
     */
    public int getId() {
        return id;
    }

    /**
     * Set the login log id.
     *
     * @param id log id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the user id associated with this login log.
     *
     * @return user id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Set the user id for this login log.
     *
     * @param userId user id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Get the action type (e.g. login/logout).
     *
     * @return action type string
     */
    public String getActionType() {
        return actionType;
    }

    /**
     * Set the action type.
     *
     * @param actionType action type string
     */
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    /**
     * Get the timestamp for this login log.
     *
     * @return timestamp string
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Set the timestamp for this login log.
     *
     * @param timestamp timestamp string
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
