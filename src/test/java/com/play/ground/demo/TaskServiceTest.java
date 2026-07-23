package com.play.ground.demo;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.play.ground.demo.service.TaskService;
import com.play.ground.demo.dto.TaskRequest;
import com.play.ground.demo.dto.TaskResponse;
import com.play.ground.demo.exception.TaskNotFoundException;
import com.play.ground.demo.dto.UpdateTaskRequest;

public class TaskServiceTest {
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        taskService = new TaskService();
    }

    @Test
    public void testCreateTask() {
        TaskRequest taskRequest = new TaskRequest("Test Task", "This is a test task.");
        TaskResponse taskResponse = taskService.create(taskRequest);
        assert taskResponse.title().equals("Test Task");
        assert taskResponse.description().equals("This is a test task.");
        assert !taskResponse.completed();
    }

    @Test
    public void testGetAllTasks() {
        taskService.create(new TaskRequest("Task 1", "Description 1"));
        taskService.create(new TaskRequest("Task 2", "Description 2"));
        List<TaskResponse> tasks = taskService.getAll();
        assert tasks.size() == 2;
        assert tasks.get(0).title().equals("Task 1");
        assert tasks.get(1).title().equals("Task 2");
        assert tasks.get(0).description().equals("Description 1");
        assert tasks.get(1).description().equals("Description 2");
    }

    @Test
    public void testCountTasks() {
        taskService.create(new TaskRequest("Task 1", "Description 1")); 
        taskService.create(new TaskRequest("Task 2", "Description 2"));
        assert taskService.count() == 2;
    }

    @Test
    public void testGetTaskById() {
        TaskResponse createdTask = taskService.create(new TaskRequest("Task 1", "Description 1"));
        TaskResponse fetchedTask = taskService.getById(createdTask.id());
        assert fetchedTask.id().equals(createdTask.id());
        assert fetchedTask.title().equals("Task 1");
        assert fetchedTask.description().equals("Description 1");  
        assert !fetchedTask.completed();
    }

    @Test
    public void testGetTaskByIdNotFound() {
        assertThatThrownBy(
            () -> taskService.getById(999L))
            .isInstanceOf(TaskNotFoundException.class)
            .hasMessageContaining("Task not found with ID: 999");
    }

    @Test
    public void testIsTaskExists() {
        TaskResponse createdTask = taskService.create(new TaskRequest("Task 1", "Description 1"));
        assert taskService.isExists(createdTask.id());
        assert !taskService.isExists(999L);
    }

    @Test
    public void testUpdateTask() {
        TaskResponse createdTask = taskService.create(new TaskRequest("Task 1", "Description 1"));
        TaskResponse updatedTask = taskService.update(createdTask.id(), new UpdateTaskRequest("Updated Task", "Updated Description"));
        assert updatedTask.id().equals(createdTask.id());
        assert updatedTask.title().equals("Updated Task");
        assert updatedTask.description().equals("Updated Description");
        assert !updatedTask.completed();
    }

    @Test
    public void testUpdateTaskNotFound() {
        assertThatThrownBy(
            () -> taskService.update(999L, new UpdateTaskRequest("Updated Task", "Updated Description")))
            .isInstanceOf(TaskNotFoundException.class)
            .hasMessageContaining("Task not found with ID: 999");
    }

    @Test
    public void testCompleteTask() {
        TaskResponse createdTask = taskService.create(new TaskRequest("Task 1", "Description 1"));
        TaskResponse completedTask = taskService.complete(createdTask.id());
        assert completedTask.id().equals(createdTask.id());
        assert completedTask.completed();
    }

    @Test
    public void testDeleteTask() {
        TaskResponse createdTask = taskService.create(new TaskRequest("Task 1", "Description 1"));
        taskService.delete(createdTask.id());
        assert !taskService.isExists(createdTask.id());
    }

    @Test void testDeleteTaskNotFound() {
        assertThatThrownBy(
            () -> taskService.delete(999L))
            .isInstanceOf(TaskNotFoundException.class)
            .hasMessageContaining("Task not found with ID: 999");
    }
}
