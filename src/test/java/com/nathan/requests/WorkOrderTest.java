package com.nathan.requests;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Tests WorkOrder class
 */
public class WorkOrderTest {

    // Order wait time should always be a positive number
    @Test
    public void workOrderWaitTimeShouldNotBeNegative() throws InterruptedException {
        WorkOrder workOrder = new WorkOrder(1);

        // sleep for a few milliseconds.
        Thread.sleep( 1234 );
        long waitTime = workOrder.getWaitTime();

        assertTrue( waitTime > -1 );
    }
    @Test
    public void workOrderWaitTimeShouldNotBeZero() throws InterruptedException {
        WorkOrder workOrder = new WorkOrder(111);

        // sleep for a few milliseconds.
        Thread.sleep( 1234 );
        long waitTime = workOrder.getWaitTime();

        assertTrue( waitTime != 0 );
    }

    // Tests to check that class levels are correct
    @Test
    public void IDsDivisibleBy3And5ShouldBeManager() {
        WorkOrder workOrder = new WorkOrder(30);
        assertEquals(workOrder.getClassLevel(), WorkOrder.ClassLevel.Manager);
    }
    @Test
    public void IDsDivisibleBy5ShouldBeVIP() {
        WorkOrder workOrder = new WorkOrder(20);
        assertEquals(workOrder.getClassLevel(), WorkOrder.ClassLevel.VIP);
    }
    @Test
    public void IDsDivisibleBy3ShouldBePriority() {
        WorkOrder workOrder = new WorkOrder(3);
        assertEquals(workOrder.getClassLevel(), WorkOrder.ClassLevel.Priority);
    }
    @Test
    public void IDsNotDivisibleBy3And5ShouldBeNormal() {
        WorkOrder workOrder = new WorkOrder(1);
        assertEquals(workOrder.getClassLevel(), WorkOrder.ClassLevel.Normal);
    }
}
