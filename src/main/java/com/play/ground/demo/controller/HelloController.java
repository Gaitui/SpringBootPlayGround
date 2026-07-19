package com.play.ground.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.play.ground.demo.service.HelloService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController 
@RequestMapping("/hello")
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping
    public String sayHello() {
        return helloService.sayHello();
    }

    @GetMapping("course")
    public String sayCourse() {
        return helloService.createCourseMsg();
    }

    @GetMapping("/{name}")
    public String sayHelloTo(@PathVariable String name) {
        return helloService.createSayHello(name);
    }

    @GetMapping("/greeting")
    public String greeting(
        @RequestParam(value = "greetName", defaultValue = "Wah") String name, 
        @RequestParam(value = "greetAge", defaultValue = "18") int age
    ) {
        return helloService.createGreetingWithAge(name, age);
    }

    @GetMapping("/version")
    public String getVersion() {
        return helloService.getVersion();
    }

    @GetMapping("/student/{name}")
    public String getStudentInfo(@PathVariable String name) {
        return helloService.getStudentInfo(name);
    }

    @GetMapping("/language")
    public String getLanguageInfo(
        @RequestParam(value = "lang", defaultValue = "Java") String lang
    ) {
        return helloService.getLanguageInfo(lang);
    }

}
