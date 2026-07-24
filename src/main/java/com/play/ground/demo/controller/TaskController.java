package com.play.ground.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;

import com.play.ground.demo.service.TaskService;

import jakarta.validation.Valid;

import com.play.ground.demo.dto.TaskRequest;
import com.play.ground.demo.dto.TaskResponse;
import com.play.ground.demo.dto.UpdateTaskRequest;

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

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskService.getById(id);
    }

    @GetMapping("/{id}/exists")
    public boolean isTaskExists(@PathVariable Long id) {
        return taskService.isExists(id);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTaskRequest updateTaskRequest) {
        return taskService.update(id, updateTaskRequest);
    }

    @PatchMapping("/{id}/complete")
    public TaskResponse completeTask(@PathVariable Long id) {
        return taskService.complete(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        taskService.delete(id);
    }

    @GetMapping("/incomplete")
    public List<TaskResponse> getIncompleteTasks() {
        return taskService.getIncompleteTasks();
    }

}
