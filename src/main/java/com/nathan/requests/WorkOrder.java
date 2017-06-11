package com.nathan.requests;

import java.util.Date;

/**
 * Represents a work order request.
 * A work order consists of an ID, date of creation,
 * and class level, calculated using ID.
 *
 * Implements comparable interface to allow a
 * custom sorting method.
 *
 * @author Nathan
 */
public class WorkOrder implements Comparable<WorkOrder> {
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
     * CONSTRUCTOR: Creates a new order with ID.
     * Date is set as time of creation.
     * ClassLevel is calculated based on ID.
     *
     * @param ID of the person who made the request.
     */
    public WorkOrder(long ID) {
        this.ID = ID;
        level = calculateLevel();
        date = new Date();
    }

    /**
     * Calculates how long the order has been waiting in seconds.
     *
     * @return wait time
     */
    public double getWaitTime() {
        Date now = new Date();
        // gets difference in milliseconds.
        double timeDifference = now.getTime() - date.getTime();

        System.out.println("In Seconds: " +timeDifference / 1000);

        return timeDifference / 1000; // return time in seconds.
    }

    /**
     * IDs divisible by 3 are priority.
     * Divisible by 5 are VIP.
     * Divisible by 3 and 5 are manager.
     * The rest are normal.
     *
     * @return ClassLevel
     */
    private ClassLevel calculateLevel() {
        if ((ID % PRIORITY == 0) && (ID % VIP == 0)) {
            return ClassLevel.Manager;
        } else if (ID % VIP == 0) {
            return ClassLevel.VIP;
        } else if (ID % PRIORITY == 0) {
            return ClassLevel.Priority;
        } else {
            return ClassLevel.Normal;
        }
    }

    /**
     * Custom compare for sorting WorkOrders.
     * Management IDs are always higher than non-management.
     * If both orders are management, or both are not, then
     * sort by rank.
     */
    @Override
    public int compareTo(WorkOrder order) {
        // if objects are equal, return 0
        // if this is less than order, return negative value
        // if this is greater than order, return positive value
        if ((level == ClassLevel.Manager) &&
                (order.getClassLevel() != ClassLevel.Manager)) {
            return 1;
        }
        else if ((order.getClassLevel() == ClassLevel.Manager) &&
                (level != ClassLevel.Manager)) {
            return -1;
        }
        else {
            return Double.compare(this.calculateRank(), order.calculateRank());
        }
    }

    /**
     * Calculate an orders rank based on class level and wait time.
     * Priority orders rank is equal to max(3, n log n),
     * where n is waitTime.
     * VIP orders rank is equal to max(4, 2n log n)
     * Management and normal orders rank is equal to wait time.
     * @return rank
     */
    public double calculateRank(double waitTime) {
        if (level == ClassLevel.Priority) {
            return Math.max(3, waitTime * Math.log(waitTime));
        }
        else if (level == ClassLevel.VIP) {
            return Math.max(4, 2 * waitTime * Math.log(waitTime));
        }
        else {
            return waitTime;
        }
    }

    /**
     * Calculate an orders rank based on class level and wait time.
     * If no waitTime is given, calculate it.
     * @return rank
     */
    public double calculateRank() {
        double waitTime = getWaitTime();
        return calculateRank(waitTime);
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
