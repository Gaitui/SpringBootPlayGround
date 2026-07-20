package com.play.ground.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;

import com.play.ground.demo.service.TaskService;

import jakarta.validation.Valid;

import com.play.ground.demo.dto.TaskRequest;
import com.play.ground.demo.dto.TaskResponse;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(@Valid @RequestBody TaskRequest taskRequest) {
        return taskService.create(taskRequest);
    }

    @GetMapping
    public java.util.List<TaskResponse> getAllTasks() {
        return taskService.getAll();
    }

    @GetMapping("/count")
    public int countTasks() {
        return taskService.count();
    }

}
