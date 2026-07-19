package com.play.ground.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/rest")
public class RestController {
    
    @GetMapping
    @ResponseBody
    public String sayHello() {
        return "Hello, World!";
    }

    @GetMapping("course")
    @ResponseBody
    public String sayCourse() {
        return "Welcome to the course!";
    }

    @GetMapping("{name}")
    @ResponseBody
    public String sayHelloTo(@PathVariable String name) {
        return "Hello, " + name + "!";
    }

    @GetMapping("greeting")
    @ResponseBody
    public String greeting(@RequestParam(value = "greetName", defaultValue = "Wah") String name, @RequestParam(value = "greetAge", defaultValue = "18") int age) {
        return "Hello, " + name + "! You are " + age + " years old.";
    }

    @GetMapping("greeting2")
    public String greeting2(Model m, @RequestParam(value = "greetName", defaultValue = "Wah") String name, @RequestParam(value = "greetAge", defaultValue = "18") int age) {
        m.addAttribute("name", name);
        m.addAttribute("age", age);
        return "greeting";
    }
}
