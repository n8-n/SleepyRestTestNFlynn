package com.nathan.requests;

import io.restassured.path.json.JsonPath;
import org.junit.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.nathan.requests.TestConstants.*;

/**
 * Used to test QueueController class
 */
public class QueueControllerTest {
    private MockMvc mockMvc;
    private QueueController queueContRollerMock;

    @Before
    public void setUpMockController() {
        queueContRollerMock = new QueueController();
        mockMvc = MockMvcBuilders.standaloneSetup(queueContRollerMock).build();
    }
    @After
    public void tearDownMockController() {
        queueContRollerMock.clearQueue();
        mockMvc = null;
    }

    // ---------------------------------------------
    // enqueue
    // ---------------------------------------------
    @Test
    public void enqueue_AddOrder_ShouldReturnCreatedCode() throws Exception {
        mockMvc.perform(post("/queue/{id}/{date}", NORMAL_ID, CURRENT_TIME))
                .andExpect(status().isCreated());
    }
    @Test
    public void enqueue_AddOrder_ShouldReturnOrderInBody() throws Exception {
        String resultString = mockMvc.perform(post("/queue/{id}/{date}", NORMAL_ID, CURRENT_TIME))
                .andReturn().getResponse().getContentAsString();
        long actualID = JsonPath.with(resultString).getLong("id");

        assertEquals(NORMAL_ID, actualID);
    }
    @Test
    public void enqueue_AddOrderTwice_ShouldReturnConflictCode() throws Exception {
        mockMvc.perform(post("/queue/{id}/{date}", NORMAL_ID, CURRENT_TIME));
        mockMvc.perform(post("/queue/{id}/{date}", NORMAL_ID, CURRENT_TIME))
                .andExpect(status().isConflict());
    }
    @Test
    public void enqueue_AddNegativeIDOrder_ShouldReturnBadRequestCode() throws Exception {
        mockMvc.perform(post("/queue/{id}/{date}", NEGATIVE_ID, CURRENT_TIME))
                .andExpect(status().isBadRequest());
    }

    // ---------------------------------------------
    // dequeue
    // ---------------------------------------------
    @Test
    public void dequeue_GetTopOrder_ShouldReturnOKCode() throws Exception {
        mockMvc.perform(post("/queue/{id}/{date}", NORMAL_ID, CURRENT_TIME));
        mockMvc.perform(delete("/queue/top"))
                .andExpect(status().isOk());
    }
    @Test
    public void dequeue_GetTopOrderEmptyQueue_ShouldReturnNotFoundCode() throws Exception {
        mockMvc.perform(delete("/queue/top"))
                .andExpect(status().isNotFound());
    }
    @Test
    public void dequeue_GetTopOrder_ShouldReturnOrderInBody() throws Exception {
        mockMvc.perform(post("/queue/{id}/{date}", NORMAL_ID, CURRENT_TIME));
        String resultString = mockMvc.perform(delete("/queue/top"))
                .andReturn().getResponse().getContentAsString();

        long actualID = JsonPath.with(resultString).getLong("id");
        assertEquals(NORMAL_ID, actualID);
    }

    // ---------------------------------------------
    // remove
    // ---------------------------------------------
    @Test
    public void remove_RemoveOrder_ShouldReturnOKCode() throws Exception {
        mockMvc.perform(post("/queue/{id}/{date}", NORMAL_ID, CURRENT_TIME));
        mockMvc.perform(delete("/queue/{id}", NORMAL_ID))
                .andExpect(status().isOk());
    }
    @Test
    public void remove_RemoveNonExistentOrder_ShouldReturnNotFoundCode() throws Exception {
        mockMvc.perform(delete("/queue/{id}", NORMAL_ID))
                .andExpect(status().isNotFound());
    }
    @Test
    public void remove_RemoveNegativeID_ShouldReturnBadRequestCode() throws Exception {
        mockMvc.perform(delete("/queue/{id}", NEGATIVE_ID))
                .andExpect(status().isBadRequest());
    }

    // ---------------------------------------------
    // list
    // ---------------------------------------------
    @Test
    public void list_GetList_ShouldReturnOKCode() throws Exception {
        mockMvc.perform(get("/queue/list"))
                .andExpect(status().isOk());
    }

    // ---------------------------------------------
    // position
    // ---------------------------------------------
    @Test
    public void position_GetPosition_ShouldReturnOKCode() throws Exception {
        mockMvc.perform(get("/queue/{id}", NORMAL_ID))
                .andExpect(status().isOk());
    }

    // ---------------------------------------------
    // waitTime
    // ---------------------------------------------
    @Test
    public void waitTime_GetWaitTime_ShouldReturnOKCode() throws Exception {
        mockMvc.perform(get("/queue/averagewait"))
                .andExpect(status().isOk());
    }
}
