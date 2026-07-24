package com.play.ground.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.play.ground.demo.dto.TaskResponse;
import com.play.ground.demo.dto.UpdateTaskRequest;
import com.play.ground.demo.model.TaskModel;
import com.play.ground.demo.dto.TaskRequest;
import com.play.ground.demo.respository.TaskRepository;

import com.play.ground.demo.exception.TaskNotFoundException;

@Service
public class TaskService {
    // private final List<TaskModel> tasks = new ArrayList<>();
    // private final AtomicLong idGenerator = new AtomicLong(0);

    // public TaskResponse create(TaskRequest request) {
    //     TaskModel task = new TaskModel(idGenerator.incrementAndGet(), request.title(), request.description(), false);

    //     tasks.add(task);
    //     return toResponse(task);
    // }

    private final TaskRepository tasks;

    public TaskService(TaskRepository taskRepository) {
        this.tasks = taskRepository;
    }

    public TaskResponse create(TaskRequest request) {
        TaskModel task = new TaskModel(
            request.title(),
            request.description(),
            false
        );

        TaskModel savedTask = tasks.save(task);
        return toResponse(savedTask);
    }



    public List<TaskResponse> getAll() {
        // List<TaskResponse> responses = new ArrayList<>();
        // for (TaskModel task : tasks) {
        //     responses.add(toResponse(task));
        // }
        // return responses;
        return tasks.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public int count() {
        return tasks.findAll().size();
    }

    public TaskResponse getById(Long id) {
        TaskModel task = findTaskById(id);
        return toResponse(task);
    }

    public boolean isExists(Long id) {
        // return tasks.findAll().stream()
        //         .anyMatch(item -> item.getId().equals(id));
        return tasks.existsById(id);
    }

    public TaskResponse update(Long id, UpdateTaskRequest request) {
        TaskModel task = findTaskById(id);

        task.setName(request.title());
        task.setDescription(request.description());

        TaskModel updatedTask = tasks.save(task);

        return toResponse(updatedTask);
    }

    public TaskResponse complete(Long id) {
        TaskModel task = findTaskById(id);
        task.setCompleted(true);
        TaskModel completedTask = tasks.save(task);
        return toResponse(completedTask);
    }

    public void delete(Long id) {
        TaskModel task = findTaskById(id);
        tasks.delete(task);
    }

    public List<TaskResponse> getIncompleteTasks() {
        return tasks.findByCompleted(false).stream()
                .map(this::toResponse)
                .toList();
    }

    private TaskModel findTaskById(Long id) {
        return tasks.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    private TaskResponse toResponse(TaskModel task) {
        return new TaskResponse(task.getId(), task.getName(), task.getDescription(), task.isCompleted());
    }
}
