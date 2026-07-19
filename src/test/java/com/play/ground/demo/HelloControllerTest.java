package com.play.ground.demo;

import org.junit.jupiter.api.Test;

import com.play.ground.demo.controller.HelloController;
import com.play.ground.demo.service.HelloService;

public class HelloControllerTest {

    @Test
    public void testSayHello() {
        // Test logic for sayHello method
        HelloService helloService = new HelloService(); 
        HelloController controller = new HelloController(helloService);
        String response = controller.sayHello();
        assert response.equals("Hello, World!");
    }
    
}
