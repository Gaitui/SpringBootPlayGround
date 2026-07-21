package com.play.ground.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.play.ground.demo.dto.TaskResponse;
import com.play.ground.demo.model.TaskModel;
import com.play.ground.demo.dto.TaskRequest;

import com.play.ground.demo.exception.TaskNotFoundException;

@Service
public class TaskService {
    private final List<TaskModel> tasks = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public TaskResponse create(TaskRequest request) {
        TaskModel task = new TaskModel(idGenerator.incrementAndGet(), request.title(), request.description(), false);

        tasks.add(task);
        return toResponse(task);
    }

    public List<TaskResponse> getAll() {
        // List<TaskResponse> responses = new ArrayList<>();
        // for (TaskModel task : tasks) {
        //     responses.add(toResponse(task));
        // }
        // return responses;
        return tasks.stream()
                .map(this::toResponse)
                .toList();
    }

    public int count() {
        return tasks.size();
    }

    public TaskResponse getById(Long id) {
        TaskModel task =  tasks.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(id));
        return toResponse(task);
    }

    public boolean isExists(Long id) {
        return tasks.stream()
                .anyMatch(item -> item.getId().equals(id));
    }

    private TaskResponse toResponse(TaskModel task) {
        return new TaskResponse(task.getId(), task.getName(), task.getDescription(), task.isCompleted());
    }
}
