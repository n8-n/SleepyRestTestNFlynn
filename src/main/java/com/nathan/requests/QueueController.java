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
    private static final String apiInfo = "Welcome to Tim's work order API.";

    /**
     * Default path value.
     *
     * @return API information
     */
    @RequestMapping
    @ResponseBody
    public String info() {
        return apiInfo;
    }

    /**
     * Takes in ID and Date and creates new order.
     * @param id of the user making the order
     * @param date of order. Use "now" for current time.
     * @return CREATED code and the created order.
     *         Or a BAD_REQUEST code and empty body.
     */
    @RequestMapping(value = "/{id}/{date}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<WorkOrderResponse> enqueue(@PathVariable("id") long id, @PathVariable("date") String date) {
        WorkOrder order = new WorkOrder(id, date);

        if (workOrderQueue.enqueue(order)) {
            WorkOrderResponse response = new WorkOrderResponse(order.getId(), order.getDateString());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Removes the first order in the queue and returns it.
     * @return The first order and OK.
     *         NOT_FOUND if queue is empty.
     */
    @RequestMapping(value = "/top", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<WorkOrderResponse> dequeue() {
        return null;
    }

    /**
     * Gets a list of order IDs, sorted from highest rank
     * to lowest.
     * @return List of longs.
     *         Or empty list if no orders.
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Long> list() {
        return workOrderQueue.getListOfIDs();
    }

    /**
     * Remove the specified order from the queue.
     * @param id of order
     * @return The specified order and OK.
     *         NOT_FOUND if order is not in queue.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<WorkOrderResponse> remove(@PathVariable("id") long id) {
        return null;
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
        return 0;
    }

    /**
     * Get average wait time of all orders.
     * @return wait time, or zero if empty.
     */
    @RequestMapping(value = "/averagewait", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public double waitTime() {
        return 0;
    }

    /**
     * Resets the contents of the queue.
     */
    public void clearQueue() {
        workOrderQueue = new WorkOrderQueue();
    }
}
