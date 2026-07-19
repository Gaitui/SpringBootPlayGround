package com.play.ground.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController 
@RequestMapping("/hello")
public class HelloController {

    @GetMapping
    public String sayHello() {
        return "Hello, World!";
    }

    @GetMapping("course")
    public String sayCourse() {
        return "Welcome to the course!";
    }

    @GetMapping("/{name}")
    public String sayHelloTo(@PathVariable String name) {
        return "Hello, " + name + "!";
    }

    @GetMapping("/greeting")
    public String greeting(
        @RequestParam(value = "greetName", defaultValue = "Wah") String name, 
        @RequestParam(value = "greetAge", defaultValue = "18") int age
    ) {
        return "Hello, " + name + "! You are " + age + " years old.";
    }

    @GetMapping("/version")
    public String getVersion() {
        return "1.0.0";
    }

    @GetMapping("/student/{name}")
    public String getStudentInfo(@PathVariable String name) {
        return "Student info for " + name;
    }

    @GetMapping("/language")
    public String getLanguageInfo(
        @RequestParam(value = "lang", defaultValue = "Java") String lang
    ) {
        return "Programming language: " + lang;
    }

}
