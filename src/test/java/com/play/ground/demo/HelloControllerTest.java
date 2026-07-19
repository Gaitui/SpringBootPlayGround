package com.play.ground.demo;

import org.junit.jupiter.api.Test;

import com.play.ground.demo.controller.HelloController;

public class HelloControllerTest {

    @Test
    public void testSayHello() {
        // Test logic for sayHello method
        HelloController controller = new HelloController();
        String response = controller.sayHello();
        assert response.equals("Hello, World!");
    }
    
}
