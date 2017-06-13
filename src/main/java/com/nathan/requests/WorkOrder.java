package com.nathan.requests;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    // Pattern for extracting Date
    private static final String dateFormat = "yyyy-MM-dd_HH:mm";

    // Member variables.
    private long ID;
    private Date date;
    private ClassLevel level;

    /**
     * CONSTRUCTOR: Create a new order with ID and specified Date.
     * in format yyyy-MM-dd_HH:mm. If dateString is set to "now",
     * set date time as current time.
     * ClassLevel is calculated based on ID.
     *
     * @param ID of person who made request.
     * @param dateString string representation of date.
     */
    public WorkOrder(long ID, String dateString) {
        this.ID = ID;
        level = calculateLevel();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

        if (dateString.equalsIgnoreCase("now")) {
            date = new Date();
        }
        else {
            try {
                date = simpleDateFormat.parse(dateString);
            } catch (ParseException e) {
                date = new Date();
                e.printStackTrace();
            }
        }
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

        return timeDifference / 1000; // return time in seconds.
    }

    /**
     * Custom compare for sorting WorkOrders.
     * Management IDs are always higher than non-management.
     * If both orders are management, or both are not, then
     * sort by rank.
     */
    @Override
    public int compareTo(WorkOrder order) {
        // if compared order is less than this, return negative value
        // if compared order is greater than this, return positive value
        if ((level == ClassLevel.Manager) &&
                (order.getClassLevel() != ClassLevel.Manager)) {
            return -1;
        }
        else if ((order.getClassLevel() == ClassLevel.Manager) &&
                (level != ClassLevel.Manager)) {
            return 1;
        }
        else {
            return Double.compare(order.calculateRank(), this.calculateRank());
        }
    }

    /**
     * Two work orders are considered equal if they have the same ID.
     * Object must not be null, and must be an instance of WorkOrder
     * @param order WorkOrder
     * @return whether the objects are equal or not.
     */
    @Override
    public boolean equals(Object order) {
        if (order == null) {
            return false;
        }
        else if (!(order instanceof WorkOrder)) {
            return false;
        }
        else {
            return ((WorkOrder) order).getId() == this.ID;
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
    public double calculateRank() {
        double waitTime = getWaitTime();

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

    /**
     * Get the date as a string in specified format.
     * @return date as string.
     */
    public String getDateString() {
        String dateString = "";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        dateString = sdf.format(date);

        return dateString;
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
}
