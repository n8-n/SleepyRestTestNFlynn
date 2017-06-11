package com.nathan.requests;

import org.junit.*;

import javax.validation.constraints.AssertFalse;

import static org.junit.Assert.*;

/**
 * Tests WorkOrderQueue class
 *
 * @author Nathan
 */
public class WorkOrderQueueTest {
    // some example IDs to use in testing.
    private int normalID = 1;
    private int priorityID = 6;
    private int vipID = 10;
    private int managerID = 15;

    private WorkOrderQueue orderQueue;

    /**
     * This creates a fresh WorkOrderQueue before every test
     */
    @Before
    public void createWorkOrderQueue() {
        orderQueue = new WorkOrderQueue();
    }

    // ---------------------------------------------
    // enqueue tests
    // ---------------------------------------------
    @Test
    public void enqueue_AddOrder_OrderShouldBeInQueue() {
        WorkOrder order = new WorkOrder(normalID);
        assertTrue(orderQueue.enqueue(order));
        assertTrue(orderQueue.contains(order));
        assertTrue(orderQueue.size() == 1);
    }
    @Test
    public void enqueue_AddDuplicateOrder_OrderShouldNotBeInQueue() {
        WorkOrder order = new WorkOrder(normalID);
        orderQueue.enqueue(order);
        assertFalse(orderQueue.enqueue(order));
        assertTrue(orderQueue.size() == 1);
    }
}
