package com.nathan.requests;

/**
 * Containment class for constants used in testing.
 *
 * @author Nathan
 */
public class TestConstants {
    // example IDs to use in testing.
    public static final long NORMAL_ID = 1;
    public static final long PRIORITY_ID = 6;
    public static final long VIP_ID = 10;
    public static final long MANAGER_ID = 15;
    public static final long NEGATIVE_ID = -1;

    // used to increase WorkOrder waitTime
    public static final long SLEEP_TIME = 100;

    // used when instantiating WorkOrders with current time
    public static final String CURRENT_TIME = "now";
}
