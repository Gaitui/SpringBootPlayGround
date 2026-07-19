package com.play.ground.demo;

import org.junit.jupiter.api.Test;

import com.play.ground.demo.service.HelloService;

public class HelloServiceTest {
    private final HelloService helloService = new HelloService();

    @Test
    public void testSayHello() {
        String response = helloService.sayHello();
        assert response.equals("Hello, World!");
    }

    @Test
    public void testGetVersion() {
        String response = helloService.getVersion();
        assert response.equals("1.0.0");
    }

    @Test
    public void testCreateSayHello() {
        String response = helloService.createSayHello("Alice");
        assert response.equals("Hello, Alice!");
    }

    @Test
    public void testCreateGreetingWithAge() {
        String response = helloService.createGreetingWithAge("Alice", 30);
        assert response.equals("Hello, Alice! You are 30 years old.");
    }
    
}
