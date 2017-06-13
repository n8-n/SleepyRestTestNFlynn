package com.nathan.requests;

/**
 * Response object used to send WorkOrder ID and Date
 *
 * @author Nathan
 */
public class WorkOrderResponse {
    private long ID;
    private String date;

    /**
     * CONSTRUCTOR
     * @param ID of work order
     * @param date created, in string format
     */
    public WorkOrderResponse(long ID, String date) {
        this.ID = ID;
        this.date = date;
    }

    public long getID() {
        return ID;
    }

    public String getDate() {
        return date;
    }
}
