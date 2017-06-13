package com.nathan.requests;

import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static com.nathan.requests.TestConstants.*;

/**
 * Tests WorkOrderQueue class
 *
 * @author Nathan
 */
public class WorkOrderQueueTest {
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
        assertTrue(orderQueue.enqueue(new WorkOrder(NORMAL_ID, CURRENT_TIME)));
        assertTrue(orderQueue.contains(NORMAL_ID));
    }
    @Test
    public void enqueue_AddDuplicateOrder_OrderShouldNotBeInQueue() {
        WorkOrder order = new WorkOrder(NORMAL_ID, CURRENT_TIME);
        orderQueue.enqueue(order);
        assertFalse(orderQueue.enqueue(order));
    }
    @Test
    public void enqueue_AddNegativeID_OrderShouldNotBeInQueue() {
        WorkOrder negativeID = new WorkOrder(NEGATIVE_ID, CURRENT_TIME);
        orderQueue.enqueue(negativeID);
        assertFalse(orderQueue.enqueue(negativeID));
    }

    // ---------------------------------------------
    // dequeue
    // ---------------------------------------------
    @Test
    public void dequeue_RemoveOrder_OrderShouldNotBeInQueue() {
        orderQueue.enqueue(new WorkOrder(NORMAL_ID, CURRENT_TIME));
        orderQueue.dequeue();
        assertFalse(orderQueue.contains(NORMAL_ID));
    }
    @Test
    public void dequeue_RemoveOrder_OrderShouldReturnCorrectID() {
        orderQueue.enqueue(new WorkOrder(NORMAL_ID, CURRENT_TIME));

        WorkOrder dequeued = orderQueue.dequeue();
        assertEquals(NORMAL_ID, dequeued.getId());
    }
    @Test
    public void dequeue_RemoveOrder_HighestRankedOrderShouldBeRemoved() throws InterruptedException {
        orderQueue.enqueue(new WorkOrder(VIP_ID, CURRENT_TIME));
        Thread.sleep(SLEEP_TIME);
        orderQueue.enqueue(new WorkOrder(MANAGER_ID, CURRENT_TIME));
        Thread.sleep(SLEEP_TIME);
        orderQueue.enqueue(new WorkOrder(NORMAL_ID, CURRENT_TIME));
        Thread.sleep(SLEEP_TIME);

        // It's expected that the MANAGER_ID will be the highest ranked
        WorkOrder dequeued = orderQueue.dequeue();
        assertEquals(MANAGER_ID, dequeued.getId());
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
        orderQueue.enqueue(new WorkOrder(VIP_ID, CURRENT_TIME));
        Thread.sleep(SLEEP_TIME);
        orderQueue.enqueue(new WorkOrder(MANAGER_ID, CURRENT_TIME));
        Thread.sleep(SLEEP_TIME);
        orderQueue.enqueue(new WorkOrder(NORMAL_ID, CURRENT_TIME));
        Thread.sleep(SLEEP_TIME);

        List<Long> expected = Arrays.asList(MANAGER_ID, VIP_ID, NORMAL_ID);
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
        orderQueue.enqueue(new WorkOrder(NORMAL_ID, CURRENT_TIME));
        orderQueue.enqueue(new WorkOrder(PRIORITY_ID, CURRENT_TIME));
        orderQueue.enqueue(new WorkOrder(VIP_ID, CURRENT_TIME));

        assertTrue(orderQueue.removeOrder(NORMAL_ID));
    }
    @Test
    public void removeOrder_TryRemoveOrder_ShouldBeRemovedFromList() {
        orderQueue.enqueue(new WorkOrder(NORMAL_ID, CURRENT_TIME));
        orderQueue.enqueue(new WorkOrder(PRIORITY_ID, CURRENT_TIME));
        orderQueue.enqueue(new WorkOrder(VIP_ID, CURRENT_TIME));

        orderQueue.removeOrder(NORMAL_ID);
        assertFalse(orderQueue.contains(NORMAL_ID));
    }
    @Test
    public void removeOrder_TryRemoveNonPresentOrder_ShouldReturnFalse() {
        orderQueue.enqueue(new WorkOrder(NORMAL_ID, CURRENT_TIME));
        orderQueue.enqueue(new WorkOrder(PRIORITY_ID, CURRENT_TIME));

        assertFalse(orderQueue.removeOrder(VIP_ID));
    }
    @Test
    public void removeOrder_TryRemoveNegativeID_ShouldReturnFalse() {
        orderQueue.enqueue(new WorkOrder(NORMAL_ID, CURRENT_TIME));
        assertFalse(orderQueue.removeOrder(-1));
    }
    @Test
    public void removeOrder_TryRemoveFromEmptyList_ShouldReturnFalse() {
        assertFalse(orderQueue.removeOrder(VIP_ID));
    }

    // --------------------------------------------------
    // getPositionOfOrder
    // --------------------------------------------------
    @Test
    public void getPositionOfOrder_GetPosition_ShouldBeCorrectPosition() throws InterruptedException {
        orderQueue.enqueue(new WorkOrder(VIP_ID, CURRENT_TIME));
        Thread.sleep(SLEEP_TIME);
        orderQueue.enqueue(new WorkOrder(MANAGER_ID, CURRENT_TIME));
        Thread.sleep(SLEEP_TIME);
        orderQueue.enqueue(new WorkOrder(NORMAL_ID, CURRENT_TIME));
        Thread.sleep(SLEEP_TIME);

        int expectedPosition = 2;   // third in queue
        int actualPosition = orderQueue.getPositionOfOrder(NORMAL_ID);
        assertEquals(actualPosition, expectedPosition);
    }
    @Test
    public void getPositionOfOrder_TryGetNonPresentID_ShouldReturnMinusOne() {
        orderQueue.enqueue(new WorkOrder(MANAGER_ID, CURRENT_TIME));
        orderQueue.enqueue(new WorkOrder(NORMAL_ID, CURRENT_TIME));

        int expectedPosition = -1;   // not in queue
        int actualPosition = orderQueue.getPositionOfOrder(VIP_ID);
        assertEquals(actualPosition, expectedPosition);
    }

    // --------------------------------------------------
    // getAverageWaitTime
    // --------------------------------------------------
    @Test
    public void getAverageWaitTime_GetAverage_ShouldBeAverageOfWaitTimes() throws InterruptedException {
        orderQueue.enqueue(new WorkOrder(VIP_ID, CURRENT_TIME));
        Thread.sleep(SLEEP_TIME);
        orderQueue.enqueue(new WorkOrder(MANAGER_ID, CURRENT_TIME));
        Thread.sleep(SLEEP_TIME);
        orderQueue.enqueue(new WorkOrder(NORMAL_ID, CURRENT_TIME));
        Thread.sleep(SLEEP_TIME);

        // Three orders added, with wait time of 100 ms between each
        // Approximate average will be (300ms + 200ms + 100ms) / 3 =
        // 200ms = 0.2s
        double expectedAverage = 0.2;
        double actualAverage = orderQueue.getAverageWaitTime();

        // delta = 0.1s
        assertEquals(expectedAverage, actualAverage, 0.1);
    }
    @Test
    public void getAverageWaitTime_GetAverageFromEmptyList_ShouldReturnZero() {
        assertEquals(0, orderQueue.getAverageWaitTime(), 0);
    }
}
