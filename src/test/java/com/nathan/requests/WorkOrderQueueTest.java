package com.nathan.requests;

import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Tests WorkOrderQueue class
 *
 * @author Nathan
 */
public class WorkOrderQueueTest {
    // some example IDs to use in testing.
    private final long normalID = 1;
    private final long priorityID = 6;
    private final long vipID = 10;
    private final long managerID = 15;
    private final long sleepTime = 100; // used to increase WorkOrder waitTime

    private WorkOrderQueue orderQueue;

    /**
     * This creates a fresh WorkOrderQueue before every test
     */
    @Before
    public void createWorkOrderQueue() {
        orderQueue = new WorkOrderQueue();
    }

    // ---------------------------------------------
    // enqueue
    // ---------------------------------------------
    @Test
    public void enqueue_AddOrder_OrderShouldBeInQueue() {
        assertTrue(orderQueue.enqueue(new WorkOrder(normalID)));
        assertTrue(orderQueue.contains(normalID));
    }
    @Test
    public void enqueue_AddDuplicateOrder_OrderShouldNotBeInQueue() {
        WorkOrder order = new WorkOrder(normalID);
        orderQueue.enqueue(order);
        assertFalse(orderQueue.enqueue(order));
    }
    @Test
    public void enqueue_AddNegativeID_OrderShouldNotBeInQueue() {
        WorkOrder negativeID = new WorkOrder(-1);
        orderQueue.enqueue(negativeID);
        assertFalse(orderQueue.enqueue(negativeID));
    }

    // ---------------------------------------------
    // dequeue
    // ---------------------------------------------
    @Test
    public void dequeue_RemoveOrder_OrderShouldNotBeInQueue() {
        orderQueue.enqueue(new WorkOrder(normalID));
        orderQueue.dequeue();
        assertFalse(orderQueue.contains(normalID));
    }
    @Test
    public void dequeue_RemoveOrder_OrderShouldReturnCorrectID() {
        orderQueue.enqueue(new WorkOrder(normalID));

        WorkOrder dequeued = orderQueue.dequeue();
        assertEquals(normalID, dequeued.getId());
    }
    @Test
    public void dequeue_RemoveOrder_HighestRankedOrderShouldBeRemoved() throws InterruptedException {
        orderQueue.enqueue(new WorkOrder(vipID));
        Thread.sleep(sleepTime);
        orderQueue.enqueue(new WorkOrder(managerID));
        Thread.sleep(sleepTime);
        orderQueue.enqueue(new WorkOrder(normalID));
        Thread.sleep(sleepTime);

        // It's expected that the managerID will be the highest ranked
        WorkOrder dequeued = orderQueue.dequeue();
        assertEquals(managerID, dequeued.getId());
    }
    @Test
    public void dequeue_RemoveOrderFromEmptyQueue_ShouldReturnNull() {
        WorkOrder dequeued = orderQueue.dequeue();
        assertNull(dequeued);
    }

    // ---------------------------------------------
    // getListOfIDs
    // ---------------------------------------------
    @Test
    public void getListOfIDs_GetList_ShouldBeInRankedOrder() throws InterruptedException {
        orderQueue.enqueue(new WorkOrder(vipID));
        Thread.sleep(sleepTime);
        orderQueue.enqueue(new WorkOrder(managerID));
        Thread.sleep(sleepTime);
        orderQueue.enqueue(new WorkOrder(normalID));
        Thread.sleep(sleepTime);

        List<Long> expected = Arrays.asList(managerID, vipID, normalID);
        List<Long> IDs = orderQueue.getListOfIDs();

        assertThat(IDs, is(expected));
    }
    @Test
    public void getListOfIDs_TryGetListFromEmpty_ShouldBeEmptyList() {
        List<Long> expected = new ArrayList<Long>();
        List<Long> IDs = orderQueue.getListOfIDs();
        assertThat(IDs, is(expected));
    }

    // --------------------------------------------------
    // removeOrder
    // --------------------------------------------------
    @Test
    public void removeOrder_TryRemoveOrder_ShouldReturnTrueIfInList() {
        orderQueue.enqueue(new WorkOrder(normalID));
        orderQueue.enqueue(new WorkOrder(priorityID));
        orderQueue.enqueue(new WorkOrder(vipID));

        assertTrue(orderQueue.removeOrder(normalID));
    }
    @Test
    public void removeOrder_TryRemoveOrder_ShouldBeRemovedFromList() {
        orderQueue.enqueue(new WorkOrder(normalID));
        orderQueue.enqueue(new WorkOrder(priorityID));
        orderQueue.enqueue(new WorkOrder(vipID));

        orderQueue.removeOrder(normalID);
        assertFalse(orderQueue.contains(normalID));
    }
    @Test
    public void removeOrder_TryRemoveNonPresentOrder_ShouldReturnFalse() {
        orderQueue.enqueue(new WorkOrder(normalID));
        orderQueue.enqueue(new WorkOrder(priorityID));

        assertFalse(orderQueue.removeOrder(vipID));
    }
    @Test
    public void removeOrder_TryRemoveNegativeID_ShouldReturnFalse() {
        orderQueue.enqueue(new WorkOrder(normalID));
        assertFalse(orderQueue.removeOrder(-1));
    }
    @Test
    public void removeOrder_TryRemoveFromEmptyList_ShouldReturnFalse() {
        assertFalse(orderQueue.removeOrder(vipID));
    }

    // --------------------------------------------------
    // getPositionOfOrder
    // --------------------------------------------------
    @Test
    public void getPositionOfOrder_GetPosition_ShouldBeCorrectPosition() throws InterruptedException {
        orderQueue.enqueue(new WorkOrder(vipID));
        Thread.sleep(sleepTime);
        orderQueue.enqueue(new WorkOrder(managerID));
        Thread.sleep(sleepTime);
        orderQueue.enqueue(new WorkOrder(normalID));
        Thread.sleep(sleepTime);

        int expectedPosition = 2;   // third in queue
        int actualPosition = orderQueue.getPositionOfOrder(normalID);
        assertEquals(actualPosition, expectedPosition);
    }
    @Test
    public void getPositionOfOrder_TryGetNonPresentID_ShouldReturnMinusOne() {
        orderQueue.enqueue(new WorkOrder(managerID));
        orderQueue.enqueue(new WorkOrder(normalID));

        int expectedPosition = -1;   // not in queue
        int actualPosition = orderQueue.getPositionOfOrder(vipID);
        assertEquals(actualPosition, expectedPosition);
    }

    // --------------------------------------------------
    // getAverageWaitTime
    // --------------------------------------------------
    @Test
    public void getAverageWaitTime_GetAverage_ShouldBeAverageOfWaitTimes() throws InterruptedException {
        orderQueue.enqueue(new WorkOrder(vipID));
        Thread.sleep(sleepTime);
        orderQueue.enqueue(new WorkOrder(managerID));
        Thread.sleep(sleepTime);
        orderQueue.enqueue(new WorkOrder(normalID));
        Thread.sleep(sleepTime);

        // Three orders added, with wait time of 100 ms between each
        // Approximate average will be (300ms + 200ms + 100ms) / 3 =
        // 200ms = 0.2s
        double expectedAverage = 0.2;
        double actualAverage = orderQueue.getAverageWaitTime();

        System.out.println("Actual:" +actualAverage);
        // delta = 0.1s
        assertEquals(expectedAverage, actualAverage, 0.1);
    }
    @Test
    public void getAverageWaitTime_GetAverageFromEmptyList_ShouldReturnZero() {
        assertEquals(0, orderQueue.getAverageWaitTime(), 0);
    }
}
