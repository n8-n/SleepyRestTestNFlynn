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
    private static final String currentTime = "now";

    /**
     * CONSTRUCTOR: Instantiates queue.
     */
    public WorkOrderQueue() {
        queue = new ArrayList<WorkOrder>();
    }

    /**
     * Adds a new order to the queue, then sorts.
     * Queue is sorted after every add to recalculate order's rank.
     * Only one order from each ID is allowed in queue.
     * ID must be positive.
     * @param order to add to queue.
     * @return whether enqueue was successful.
     */
    public boolean enqueue(WorkOrder order) {
        boolean result;

        if (queue.contains(order) || (order.getId() < 1)) {
            result =  false;
        }
        else {
            result = queue.add(order);
            Collections.sort(queue);
        }

        return result;
    }

    /**
     * Removes the highest ranked order from the queue.
     * If queue is empty, return null.
     * @return top order
     */
    public WorkOrder dequeue() {
        if (queue.size() == 0) {
            return null;
        }
        else {
            WorkOrder order = queue.remove(0);
            Collections.sort(queue);
            return order;
        }
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
     * Removes specified order if ID is positive
     * and queue is not empty.
     * @param ID of order to remove
     * @return whether removal was successful or not
     */
    public boolean removeOrder(long ID) {
        boolean result = false;

        if ((queue.size() > -1) || (ID > 0)) {
            result = queue.remove(new WorkOrder(ID, currentTime));
            Collections.sort(queue);
        }

        return result;
    }

    /**
     * Get an orders current position in the queue.
     * @param ID of order to query.
     * @return position of order, indexed from 0.
     *         -1 if ID is not present.
     */
    public int getPositionOfOrder(long ID) {
        return queue.indexOf(new WorkOrder(ID, currentTime));
    }

    /**
     * Get the average wait time of all orders.
     * @return average wait time.
     *         0 if empty list
     */
    public double getAverageWaitTime() {
        double total = 0;

        if (queue.size() != 0) {
            for (WorkOrder order : queue) {
                total += order.getWaitTime();
            }

            total /= queue.size();
        }
        return total;
    }

    /**
     * Check if order ID is in queue.
     * @param ID to search
     * @return true if it's in queue
     */
    public boolean contains(long ID) {
        return queue.contains(new WorkOrder(ID, currentTime));
    }

    /**
     * @return number of orders in queue.
     */
    public int size() {
        return queue.size();
    }
}

