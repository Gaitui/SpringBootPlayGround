package com.play.ground.demo.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String sayHello() {
        return "Hello, World!";
    }
    
    public String createSayHello(String name) {
        return "Hello, " + name + "!";
    }

    public String createCourseMsg() {
        return "Welcome to the course!";
    }

    public String createGreetingWithAge(String name, int age) {
        return "Hello, " + name + "! You are " + age + " years old.";
    }

    public String getVersion() {
        return "1.0.0";
    }

    public String getStudentInfo(String name) {
        return "Student info for " + name;
    }

    public String getLanguageInfo(String lang) {
        return "Programming language: " + lang;
    }
}
