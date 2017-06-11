package com.nathan.requests;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Tests WorkOrder class
 *
 * @author Nathan
 */
public class WorkOrderTest {
    // some example IDs to use in testing.
    private int normalID = 1;
    private int priorityID = 6;
    private int vipID = 10;
    private int managerID = 15;

    // Order wait time should always be a positive number
    @Test
    public void getWaitTime_WaitTime_ShouldBeGreaterThanZero() throws InterruptedException {
        WorkOrder workOrder = new WorkOrder(normalID);

        // sleep for a few milliseconds.
        Thread.sleep( 1234 );
        double waitTime = workOrder.getWaitTime();

        assertTrue( waitTime > 0 );
    }

    // Tests to check that class levels are correct
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


    // Test to make sure that rank calculations are correct
    @Test
    public void calculateRank_NormalRank_ShouldBeEqualToWaitTime() throws InterruptedException {
        WorkOrder workOrder = new WorkOrder(normalID);
        Thread.sleep(200);
        double waitTime = workOrder.getWaitTime();
        double rank = workOrder.calculateRank(waitTime);

        // 0 indicates delta: amount that values can be off by.
        assertEquals(rank, waitTime, 0);
    }
    @Test
    public void calculateRank_PriorityRank_ShouldBeEqualToMaxOf3andNLogN() throws InterruptedException {
        WorkOrder workOrder = new WorkOrder(priorityID);
        Thread.sleep(200);
        double waitTime = workOrder.getWaitTime();
        double rank = workOrder.calculateRank(waitTime);

        waitTime = Math.max(3, waitTime * Math.log(waitTime));

        assertEquals(rank, waitTime, 0);
    }
    @Test
    public void calculateRank_VIPRank_ShouldBeEqualToMaxOf4and2NLogN() throws InterruptedException {
        WorkOrder workOrder = new WorkOrder(vipID);
        Thread.sleep(200);
        double waitTime = workOrder.getWaitTime();
        double rank = workOrder.calculateRank(waitTime);

        waitTime = Math.max(4, 2 * waitTime * Math.log(waitTime));

        assertEquals(rank, waitTime, 0);
    }
    @Test
    public void calculateRank_ManagerRank_ShouldBeEqualToWaitTime() throws InterruptedException {
        WorkOrder workOrder = new WorkOrder(managerID);
        Thread.sleep(200);
        double waitTime = workOrder.getWaitTime();
        double rank = workOrder.calculateRank(waitTime);

        assertEquals(rank, waitTime, 0);
    }

}
