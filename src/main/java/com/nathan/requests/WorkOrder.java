package com.nathan.requests;

import java.util.Date;

/**
 * Represents a work order request sent to Tim.
 */
public class WorkOrder {
    /**
     * Definition of class level for each user.
     */
    public enum ClassLevel {
        Normal,
        Priority,
        VIP,
        Manager
    }

    // Used in calculating an order's class level.
    private static final int PRIORITY = 3;
    private static final int VIP = 5;

    // Member variables.
    private long ID;
    private Date date;
    private ClassLevel level;

    /**
     * Creates a new order with ID.
     * Date is set as time of creation.
     * ClassLevel is calculated based on ID.
     * @param ID Identifier of the person who made the request.
     */
    public WorkOrder(long ID) {
        this.ID = ID;
        level = calculateLevel();
        date = new Date();
    }

    /**
     * Calculates how long the order has been waiting in seconds.
     * @return long
     */
    public long getWaitTime() {
        Date now = new Date();
        // gets difference in milliseconds.
        long timeDifference = now.getTime() - date.getTime();

        return timeDifference / 1000; // return time in seconds.
    }

    /**
     * IDs divisible by 3 are priority.
     * Divisible by 5 are VIP.
     * Divisible by 3 and 5 are manager.
     * The rest are normal.
     * @return ClassLevel
     */
    private ClassLevel calculateLevel() {
        if ((ID % PRIORITY == 0) && (ID % VIP == 0)) {
            return ClassLevel.Manager;
        }
        else if (ID % VIP == 0) {
            return ClassLevel.VIP;
        }
        else if (ID % PRIORITY == 0) {
            return ClassLevel.Priority;
        }
        else {
            return ClassLevel.Normal;
        }
    }

    /*
     * Getters and Setters
     */
    public long getId() {
        return ID;
    }

    public ClassLevel getClassLevel() {
        return level;
    }

    public Date getDate() {
        return date;
    }
}
