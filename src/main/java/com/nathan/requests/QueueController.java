package com.nathan.requests;

import org.springframework.http.HttpStatus;
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
     * @return API information
     */
    @RequestMapping
    @ResponseBody
    public String info() {
        return apiInfo;
    }

    @RequestMapping(value = "/{id}/{date}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void enqueue(@PathVariable("id") long id, @PathVariable("date") String date) {
        workOrderQueue.enqueue(new WorkOrder(id, date));
    }

    @RequestMapping(value = "/dequeue", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void dequeue() {
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Long> list() {
        return workOrderQueue.getListOfIDs();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void remove() {
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void position() {
    }

    @RequestMapping(value = "/waittime", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void waitTime() {
    }
}
