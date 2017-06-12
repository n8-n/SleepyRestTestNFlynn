package com.nathan.requests;

import org.junit.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * Tests WorkOrder class
 *
 * @author Nathan
 */
public class WorkOrderTest {
    // some example IDs to use in testing.
    private final long normalID = 1;
    private final long priorityID = 6;
    private final long vipID = 10;
    private final long managerID = 15;
    private final long sleepTime = 100; // used to increase WorkOrder waitTime

    // --------------------------------------------------
    // WorkOrder Construction
    // --------------------------------------------------
    @Test
    public void newWorkOrder_IDsNotDivisibleBy3And5_ShouldBeNormal() {
        WorkOrder workOrder = new WorkOrder(normalID);
        assertEquals(workOrder.getClassLevel(), WorkOrder.ClassLevel.Normal);
    }
    @Test
    public void WorkOrder_IDsDivisibleBy3_ShouldBePriority() {
        WorkOrder workOrder = new WorkOrder(priorityID);
        assertEquals(workOrder.getClassLevel(), WorkOrder.ClassLevel.Priority);
    }
    @Test
    public void WorkOrder_IDsDivisibleBy5_ShouldBeVIP() {
        WorkOrder workOrder = new WorkOrder(vipID);
        assertEquals(workOrder.getClassLevel(), WorkOrder.ClassLevel.VIP);
    }
    @Test
    public void WorkOrder_IDsDivisibleBy3And5_ShouldBeManager() {
        WorkOrder workOrder = new WorkOrder(managerID);
        assertEquals(workOrder.getClassLevel(), WorkOrder.ClassLevel.Manager);
    }
    @Test
    public void WorkOrder_DateStringConstructor_ShouldBeParsedCorrectly() {
        WorkOrder workOrder = new WorkOrder(normalID, "1994-01-07_12:02");
        Calendar cal = new GregorianCalendar(1994, 0, 7, 12, 2);
        Date testDate = cal.getTime();

        assertEquals(testDate, workOrder.getDate());
    }


    // --------------------------------------------------
    // getWaitTime
    // --------------------------------------------------
    @Test
    public void getWaitTime_WaitTime_ShouldBeGreaterThanZero() throws InterruptedException {
        WorkOrder workOrder = new WorkOrder(normalID);

        // sleep for a few milliseconds.
        Thread.sleep(sleepTime);
        double waitTime = workOrder.getWaitTime();
        assertTrue(waitTime > 0);
    }

    // --------------------------------------------------
    // calculateRank
    // --------------------------------------------------
    @Test
    public void calculateRank_NormalRank_ShouldBeEqualToWaitTime() throws InterruptedException {
        WorkOrder workOrder = new WorkOrder(normalID);
        Thread.sleep(sleepTime);
        double waitTime = workOrder.getWaitTime();
        double rank = workOrder.calculateRank(waitTime);

        // 0 indicates delta: amount that values can be off by.
        assertEquals(rank, waitTime, 0);
    }
    @Test
    public void calculateRank_PriorityRank_ShouldBeEqualToMaxOfThreeAndNLogN() throws InterruptedException {
        WorkOrder workOrder = new WorkOrder(priorityID);
        Thread.sleep(sleepTime);
        double waitTime = workOrder.getWaitTime();
        double rank = workOrder.calculateRank(waitTime);

        waitTime = Math.max(3, waitTime * Math.log(waitTime));
        assertEquals(rank, waitTime, 0);
    }
    @Test
    public void calculateRank_VIPRank_ShouldBeEqualToMaxOfFourAndNLogN() throws InterruptedException {
        WorkOrder workOrder = new WorkOrder(vipID);
        Thread.sleep(sleepTime);
        double waitTime = workOrder.getWaitTime();
        double rank = workOrder.calculateRank(waitTime);

        waitTime = Math.max(4, 2 * waitTime * Math.log(waitTime));
        assertEquals(rank, waitTime, 0);
    }
    @Test
    public void calculateRank_ManagerRank_ShouldBeEqualToWaitTime() throws InterruptedException {
        WorkOrder workOrder = new WorkOrder(managerID);
        Thread.sleep(sleepTime);
        double waitTime = workOrder.getWaitTime();
        double rank = workOrder.calculateRank(waitTime);

        assertEquals(rank, waitTime, 0);
    }

    // --------------------------------------------------
    // compareTo
    // --------------------------------------------------
    @Test
    public void compareTo_ManagerComparedToNormal_ResultShouldBeMinusNegative() {
        WorkOrder managerOrder = new WorkOrder(managerID);
        WorkOrder normalOrder = new WorkOrder(normalID);

        assertTrue(managerOrder.compareTo(normalOrder) < 0);
    }
    @Test
    public void compareTo_NormalComparedToManager_ResultShouldBePositive() {
        WorkOrder managerOrder = new WorkOrder(managerID);
        WorkOrder normalOrder = new WorkOrder(normalID);

        assertTrue(normalOrder.compareTo(managerOrder) > 0);
    }
    @Test
    public void compareTo_TwoNormalOrders_HigherWaitTimeShouldHaveHigherRank() throws InterruptedException {
        WorkOrder order1 = new WorkOrder(normalID);
        Thread.sleep(sleepTime);
        WorkOrder order2 = new WorkOrder(normalID);

        // result negative because order2 is less than order1
        assertTrue(order1.compareTo(order2) < 0);
    }


    // --------------------------------------------------
    // equals
    // --------------------------------------------------
    @Test
    public void equals_NullObject_ShouldBeFalse() {
        WorkOrder order = new WorkOrder(normalID);
        assertFalse(order.equals(null));
    }
    @Test
    public void equals_NonWorkOrderObject_ShouldBeFalse() {
        WorkOrder order = new WorkOrder(normalID);
        assertFalse(order.equals(normalID));
    }
    @Test
    public void equals_DifferentIDs_ShouldBeFalse() {
        WorkOrder order1 = new WorkOrder(normalID);
        WorkOrder order2 = new WorkOrder(vipID);
        assertFalse(order1.equals(order2));
    }
    @Test
    public void equals_SameIDsDifferentWaitTime_ShouldBeTrue() throws InterruptedException {
        WorkOrder order1 = new WorkOrder(normalID);
        Thread.sleep(sleepTime);
        WorkOrder order2 = new WorkOrder(normalID);
        assertTrue(order1.equals(order2));
    }
}
