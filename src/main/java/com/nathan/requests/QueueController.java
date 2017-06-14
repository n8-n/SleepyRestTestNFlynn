package com.nathan.requests;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles all requests made to the REST service.
 * Contains a WorkOrderQueue object that holds all orders.
 *
 * @author Nathan
 */
@RestController
@RequestMapping(value = "queue")
public class QueueController {
    private static WorkOrderQueue workOrderQueue = new WorkOrderQueue();

    /**
     * Gets a list of order IDs, sorted from highest rank
     * to lowest.
     * @return List of longs.
     *         Or empty list if no orders.
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Long> list() {
        return workOrderQueue.getListOfIDs();
    }

    /**
     * Get position of order in queue.
     * @param id of order to find.
     * @return position, or -1 if not present.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public int position(@PathVariable("id") long id) {
        return workOrderQueue.getPositionOfOrder(id);
    }

    /**
     * Get average wait time of all orders.
     * @return wait time, or zero if empty.
     */
    @RequestMapping(value = "/meantime", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public double waitTime() {
        return workOrderQueue.getAverageWaitTime();
    }

    /**
     * Takes in ID and Date and creates new order.
     * @param id of the user making the order
     * @param date of order. Use "now" for current time.
     * @return CREATED code and the created order.
     *         BAD_REQUEST code and empty body if ID is less than 1.
     *         CONFLICT if the ID is already present.
     */
    @RequestMapping(value = "/{id}/{date}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<WorkOrderResponse> enqueue(@PathVariable("id") long id, @PathVariable("date") String date) {
        WorkOrder order = new WorkOrder(id, date);

        if (id < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        else if (workOrderQueue.enqueue(order)) {
            WorkOrderResponse response = new WorkOrderResponse(order.getId(), order.getDateString());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    /**
     * Removes the first order in the queue and returns it.
     * @return The first order and OK.
     *         NOT_FOUND if queue is empty.
     */
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<WorkOrderResponse> dequeue() {
        WorkOrder order = workOrderQueue.dequeue();

        if (order != null) {
            WorkOrderResponse response = new WorkOrderResponse(order.getId(), order.getDateString());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Remove the specified order from the queue.
     * @param id of order
     * @return ID and OK status or
     *         NOT_FOUND if order is not in queue.
     *         BAD_REQUEST if ID less than 1.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Long> remove(@PathVariable("id") long id) {
        if (id < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(id);
        }

        HttpStatus result = workOrderQueue.removeOrder(id) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(result).body(id);
    }

    /**
     * Resets the contents of the queue.
     */
    public void clearQueue() {
        workOrderQueue = new WorkOrderQueue();
    }
}
