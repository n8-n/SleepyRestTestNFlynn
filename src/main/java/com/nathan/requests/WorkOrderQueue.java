package com.nathan.requests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the queue of work order requests.
 * Consists of an ArrayList of WorkOrder objects and
 * various methods to manipulate this list.
 *
 * @author Nathan
 */
public class WorkOrderQueue {
    private List<WorkOrder> queue;

    /**
     * CONSTRUCTOR: Instantiates queue.
     */
    public WorkOrderQueue() {
        queue = new ArrayList<WorkOrder>();
    }

    /**
     * Adds a new order to the queue, then sorts.
     * Queue is sorted after every add to recalculate order's rank.
     * @param order to add to queue.
     */
    public void enqueue(WorkOrder order) {
        queue.add(order);
        Collections.sort(queue);
    }

    /**
     * Removes the highest ranked order from the queue.
     * @return top order
     */
    public WorkOrder dequeue() {
        WorkOrder order = queue.remove(0);
        Collections.sort(queue);
        return order;
    }

    /**
     * Gets a list of all order IDs,
     * sorted by highest rank to lowest.
     * @return ArrayList<long>
     */
    public List<Long> getListOfIDs() {
        List<Long> IDs = new ArrayList<Long>();

        for (WorkOrder order : queue) {
            IDs.add(order.getId());
        }

        return IDs;
    }

    /**
     * Removes specified order.
     * @param ID of order to remove
     */
    public void removeOrder(long ID) {

    }

    /**
     * Get an orders current position in the queue.
     * @param ID of order to query.
     * @return position of order, indexed from 0.
     *         -1 if ID is not present.
     */
    public int getPositionOfOrder(long ID) {
        return 0;
    }

    /**
     * Get the average wait time of all orders.
     * @return average wait time.
     */
    public double getAverageWaitTime() {
        return 0;
    }
}

