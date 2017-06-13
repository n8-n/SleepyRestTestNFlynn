package com.nathan.requests;

import io.restassured.path.json.JsonPath;
import org.junit.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
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
    public void enqueue_AddOrder_ShouldReturnHTTPCreatedCode() throws Exception {
        mockMvc.perform(post("/queue/{id}/{date}", NORMAL_ID, CURRENT_TIME))
                .andExpect(status().isCreated());
    }

    @Test
    public void enqueue_AddOrder_ShouldReturnOrderIDAndDateInBody() throws Exception {
        String resultString = mockMvc.perform(post("/queue/{id}/{date}", NORMAL_ID, CURRENT_TIME))
                .andReturn().getResponse().getContentAsString();
        long actualID = JsonPath.with(resultString).getLong("id");

        assertEquals(NORMAL_ID, actualID);
    }

    @Test
    public void enqueue_AddOrderTwice_ShouldReturnHTTPBadRequestCode() throws Exception {
        mockMvc.perform(post("/queue/{id}/{date}", NORMAL_ID, CURRENT_TIME));
        mockMvc.perform(post("/queue/{id}/{date}", NORMAL_ID, CURRENT_TIME))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void enqueue_AddNegativeIDOrder_ShouldReturnHTTPBadRequestCode() throws Exception {
        mockMvc.perform(post("/queue/{id}/{date}", NEGATIVE_ID, CURRENT_TIME))
                .andExpect(status().is4xxClientError());
    }
}
