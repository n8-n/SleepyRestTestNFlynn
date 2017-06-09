package com.nathan.app;

import org.junit.*;
import static org.junit.Assert.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;


public class GreetingControllerTest {  
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new GreetingController()).build();

    
    @Test
    public void testApp() {
        assertTrue( true );
    }

    @Test
    public void greetingWithNoParameterShouldReturnDefaultGreeting() throws Exception {
    	mockMvc.perform(get("/greeting"))
    		.andExpect(jsonPath("$.content", is("Hello, World!")));
    	
    }
}
