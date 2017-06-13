package com.nathan.requests;

import org.junit.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;
import static com.nathan.requests.TestConstants.*;

/**
 * Tests WorkOrder class
 *
 * @author Nathan
 */
public class WorkOrderTest {
    // --------------------------------------------------
    // WorkOrder Construction
    // --------------------------------------------------
    @Test
    public void newWorkOrder_IDsNotDivisibleBy3And5_ShouldBeNormal() {
        WorkOrder workOrder = new WorkOrder(NORMAL_ID, CURRENT_TIME);
        assertEquals(workOrder.getClassLevel(), WorkOrder.ClassLevel.Normal);
    }
    @Test
    public void WorkOrder_IDsDivisibleBy3_ShouldBePriority() {
        WorkOrder workOrder = new WorkOrder(PRIORITY_ID, CURRENT_TIME);
        assertEquals(workOrder.getClassLevel(), WorkOrder.ClassLevel.Priority);
    }
    @Test
    public void WorkOrder_IDsDivisibleBy5_ShouldBeVIP() {
        WorkOrder workOrder = new WorkOrder(VIP_ID, CURRENT_TIME);
        assertEquals(workOrder.getClassLevel(), WorkOrder.ClassLevel.VIP);
    }
    @Test
    public void WorkOrder_IDsDivisibleBy3And5_ShouldBeManager() {
        WorkOrder workOrder = new WorkOrder(MANAGER_ID, CURRENT_TIME);
        assertEquals(workOrder.getClassLevel(), WorkOrder.ClassLevel.Manager);
    }
    @Test
    public void WorkOrder_DateStringConstructor_ShouldBeParsedCorrectly() {
        WorkOrder workOrder = new WorkOrder(NORMAL_ID, "1994-01-07_12:02");
        Calendar cal = new GregorianCalendar(1994, 0, 7, 12, 2);
        Date testDate = cal.getTime();

        assertEquals(testDate, workOrder.getDate());
    }


    // --------------------------------------------------
    // getWaitTime
    // --------------------------------------------------
    @Test
    public void getWaitTime_WaitTime_ShouldBeGreaterThanZero() throws InterruptedException {
        WorkOrder workOrder = new WorkOrder(NORMAL_ID, CURRENT_TIME);

        // sleep for a few milliseconds.
        Thread.sleep(SLEEP_TIME);
        double waitTime = workOrder.getWaitTime();
        assertTrue(waitTime > 0);
    }

    // --------------------------------------------------
    // calculateRank
    // --------------------------------------------------
    @Test
    public void calculateRank_NormalRank_ShouldBeEqualToWaitTime() throws InterruptedException {
        WorkOrder workOrder = new WorkOrder(NORMAL_ID, CURRENT_TIME);
        Thread.sleep(SLEEP_TIME);
        double waitTime = workOrder.getWaitTime();
        double rank = workOrder.calculateRank();

        // 0 indicates delta: amount that values can be off by.
        assertEquals(rank, waitTime, 0.01);
    }
    @Test
    public void calculateRank_PriorityRank_ShouldBeEqualToMaxOfThreeAndNLogN() throws InterruptedException {
        WorkOrder workOrder = new WorkOrder(PRIORITY_ID, CURRENT_TIME);
        Thread.sleep(SLEEP_TIME);
        double waitTime = workOrder.getWaitTime();
        double rank = workOrder.calculateRank();

        waitTime = Math.max(3, waitTime * Math.log(waitTime));
        assertEquals(rank, waitTime, 0.01);
    }
    @Test
    public void calculateRank_VIPRank_ShouldBeEqualToMaxOfFourAndNLogN() throws InterruptedException {
        WorkOrder workOrder = new WorkOrder(VIP_ID, CURRENT_TIME);
        Thread.sleep(SLEEP_TIME);
        double waitTime = workOrder.getWaitTime();
        double rank = workOrder.calculateRank();

        waitTime = Math.max(4, 2 * waitTime * Math.log(waitTime));
        assertEquals(rank, waitTime, 0.01);
    }
    @Test
    public void calculateRank_ManagerRank_ShouldBeEqualToWaitTime() throws InterruptedException {
        WorkOrder workOrder = new WorkOrder(MANAGER_ID, CURRENT_TIME);
        Thread.sleep(SLEEP_TIME);
        double waitTime = workOrder.getWaitTime();
        double rank = workOrder.calculateRank();

        assertEquals(rank, waitTime, 0.01);
    }

    // --------------------------------------------------
    // compareTo
    // --------------------------------------------------
    @Test
    public void compareTo_ManagerComparedToNormal_ResultShouldBeMinusNegative() {
        WorkOrder managerOrder = new WorkOrder(MANAGER_ID, CURRENT_TIME);
        WorkOrder normalOrder = new WorkOrder(NORMAL_ID, CURRENT_TIME);

        assertTrue(managerOrder.compareTo(normalOrder) < 0);
    }
    @Test
    public void compareTo_NormalComparedToManager_ResultShouldBePositive() {
        WorkOrder managerOrder = new WorkOrder(MANAGER_ID, CURRENT_TIME);
        WorkOrder normalOrder = new WorkOrder(NORMAL_ID, CURRENT_TIME);

        assertTrue(normalOrder.compareTo(managerOrder) > 0);
    }
    @Test
    public void compareTo_TwoNormalOrders_HigherWaitTimeShouldHaveHigherRank() throws InterruptedException {
        WorkOrder order1 = new WorkOrder(NORMAL_ID, CURRENT_TIME);
        Thread.sleep(SLEEP_TIME);
        WorkOrder order2 = new WorkOrder(NORMAL_ID, CURRENT_TIME);

        // result negative because order2 is less than order1
        assertTrue(order1.compareTo(order2) < 0);
    }


    // --------------------------------------------------
    // equals
    // --------------------------------------------------
    @Test
    public void equals_NullObject_ShouldBeFalse() {
        WorkOrder order = new WorkOrder(NORMAL_ID, CURRENT_TIME);
        assertFalse(order.equals(null));
    }
    @Test
    public void equals_NonWorkOrderObject_ShouldBeFalse() {
        WorkOrder order = new WorkOrder(NORMAL_ID, CURRENT_TIME);
        assertFalse(order.equals(NORMAL_ID));
    }
    @Test
    public void equals_DifferentIDs_ShouldBeFalse() {
        WorkOrder order1 = new WorkOrder(NORMAL_ID, CURRENT_TIME);
        WorkOrder order2 = new WorkOrder(VIP_ID, CURRENT_TIME);
        assertFalse(order1.equals(order2));
    }
    @Test
    public void equals_SameIDsDifferentWaitTime_ShouldBeTrue() throws InterruptedException {
        WorkOrder order1 = new WorkOrder(NORMAL_ID, CURRENT_TIME);
        Thread.sleep(SLEEP_TIME);
        WorkOrder order2 = new WorkOrder(NORMAL_ID, CURRENT_TIME);
        assertTrue(order1.equals(order2));
    }

    // --------------------------------------------------
    // getDateString
    // -------------------------------------------------
    @Test
    public void getDateString_GetString_ShouldBeEqualToCorrectDate() {
        String expected = "2017-06-01_12:23";
        WorkOrder order = new WorkOrder(NORMAL_ID, expected);

        assertEquals(expected, order.getDateString());
    }

}
