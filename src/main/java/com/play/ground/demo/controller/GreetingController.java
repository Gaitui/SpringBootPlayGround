package com.play.ground.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {
    @RequestMapping(path = "/greeting", method = RequestMethod.GET)
    public String greeting(
        Model m, 
        @RequestParam(value = "greetName", defaultValue = "Wah") String name
    ) {
        m.addAttribute("name", name);
        return "greeting";
    }
}
