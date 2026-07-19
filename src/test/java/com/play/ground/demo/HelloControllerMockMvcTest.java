package com.play.ground.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.play.ground.demo.controller.HelloController;
import com.play.ground.demo.service.HelloService;

import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; 
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content; 
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HelloController.class)
public class HelloControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HelloService helloService;

    @Test
    public void testSayHello() throws Exception {
        when(helloService.sayHello())
            .thenReturn("Hello, World!");
        
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, World!"));

        verify(helloService)
            .sayHello();
    }

    @Test
    public void testVersion() throws Exception {
        when(helloService.getVersion())
            .thenReturn("1.0.0");

        mockMvc.perform(get("/hello/version"))
                .andExpect(status().isOk())
                .andExpect(content().string("1.0.0"));
        
        verify(helloService)
            .getVersion();
    }
    
    @Test
    public void testSayHelloTo() throws Exception {
        String name = "Alice";
        when(helloService.createSayHello(name))
            .thenReturn("Hello, Alice!");

        mockMvc.perform(get("/hello/{name}", name))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Alice!"));

        verify(helloService)
            .createSayHello(name);
    }

    @Test
    public void testGreeting() throws Exception {
        String name = "Bob";
        int age = 25;
        when(helloService.createGreetingWithAge(name, age))
            .thenReturn("Hello, Bob! You are 25 years old.");   
        
        mockMvc.perform(get("/hello/greeting")
                .param("greetName", name)
                .param("greetAge", String.valueOf(age)))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Bob! You are 25 years old."));
    
        verify(helloService)
            .createGreetingWithAge(name, age);
    }

    @Test
    public void testGreetingWithDefaultParams() throws Exception {
        when(helloService.createGreetingWithAge("Wah", 18))
            .thenReturn("Hello, Wah! You are 18 years old.");
        
        mockMvc.perform(get("/hello/greeting"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Wah! You are 18 years old."));
    
        verify(helloService)
            .createGreetingWithAge("Wah", 18);
    }
}
