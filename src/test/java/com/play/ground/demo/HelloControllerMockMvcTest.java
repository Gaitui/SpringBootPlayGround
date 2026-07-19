package com.play.ground.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.play.ground.demo.controller.HelloController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; 
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content; 
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HelloController.class)
public class HelloControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSayHello() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, World!"));
    }

    @Test
    public void testVersion() throws Exception {
        mockMvc.perform(get("/hello/version"))
                .andExpect(status().isOk())
                .andExpect(content().string("1.0.0"));
    }
    
}
