package com.play.ground.demo;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.play.ground.demo.service.TaskService;
import com.play.ground.demo.dto.TaskRequest;
import com.play.ground.demo.dto.TaskResponse;
import com.play.ground.demo.exception.TaskNotFoundException;
import com.play.ground.demo.model.TaskModel;
import com.play.ground.demo.respository.TaskRepository;
import com.play.ground.demo.dto.UpdateTaskRequest;

public class TaskServiceTest {
    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    public void testCreateTask() {
        TaskRequest taskRequest = new TaskRequest("Test Task", "This is a test task.");
        TaskModel taskModel = new TaskModel("Test Task", "This is a test task.", false);
        
        when(taskRepository.save(
            any(TaskModel.class)
        )).thenReturn(taskModel);

        TaskResponse taskResponse = taskService.create(taskRequest);
        assert taskResponse.title().equals("Test Task");
        assert taskResponse.description().equals("This is a test task.");
        assert !taskResponse.completed();

        verify(taskRepository).save(any(TaskModel.class));
    }

    @Test
    public void testGetAllTasks() {
        // taskService.create(new TaskRequest("Task 1", "Description 1"));
        // taskService.create(new TaskRequest("Task 2", "Description 2"));
        // List<TaskResponse> tasks = taskService.getAll();
        when(taskRepository.findAll())
        .thenReturn(List.of(
            new TaskModel("Task 1", "Description 1", false),
            new TaskModel("Task 2", "Description 2", false)
        ));

        List<TaskResponse> tasks = taskService.getAll();

        assert tasks.size() == 2;
        assert tasks.get(0).title().equals("Task 1");
        assert tasks.get(1).title().equals("Task 2");
        assert tasks.get(0).description().equals("Description 1");
        assert tasks.get(1).description().equals("Description 2");

        verify(taskRepository).findAll();
    }

    @Test
    public void testCountTasks() {
        // taskService.create(new TaskRequest("Task 1", "Description 1")); 
        // taskService.create(new TaskRequest("Task 2", "Description 2"));
        when(taskRepository.findAll())
        .thenReturn(List.of(
            new TaskModel("Task 1", "Description 1", false),
            new TaskModel("Task 2", "Description 2", false)
        ));
        assert taskService.count() == 2;
        verify(taskRepository).findAll();
    }

    @Test
    public void testGetTaskById() {
        // TaskResponse createdTask = taskService.create(new TaskRequest("Task 1", "Description 1"));
        TaskModel taskModel = new TaskModel("Task 1", "Description 1", false);
        ReflectionTestUtils.setField(taskModel, "id", 1L); // Set the ID field using reflection
        when(taskRepository.findById(1L))
        .thenReturn(
            Optional.of(taskModel)
        );

        TaskResponse fetchedTask = taskService.getById(1L);
        assert fetchedTask.id().equals(1L);
        assert fetchedTask.title().equals("Task 1");
        assert fetchedTask.description().equals("Description 1");  
        assert !fetchedTask.completed();
        verify(taskRepository).findById(1L);
    }

    @Test
    public void testGetTaskByIdNotFound() {
        when(taskRepository.findById(999L))
        .thenReturn(Optional.empty());

        assertThatThrownBy(
            () -> taskService.getById(999L))
            .isInstanceOf(TaskNotFoundException.class)
            .hasMessageContaining("Task not found with ID: 999");
    }

    @Test
    public void testIsTaskExists() {
        // TaskResponse createdTask = taskService.create(new TaskRequest("Task 1", "Description 1"));
        
        when(taskRepository.existsById(1L))
        .thenReturn(true);
        when(taskRepository.existsById(999L))
        .thenReturn(false);

        assert taskService.isExists(1L);
        assert !taskService.isExists(999L);

        verify(taskRepository).existsById(1L);
        verify(taskRepository).existsById(999L);
    }

    @Test
    public void testUpdateTask() {
        when(taskRepository.findById(1L))
        .thenReturn(Optional.of(new TaskModel("Task 1", "Description 1", false)));
        when(taskRepository.save(any(TaskModel.class)))
        .thenReturn(new TaskModel("Updated Task", "Updated Description", false));
        // TaskResponse createdTask = taskService.create(new TaskRequest("Task 1", "Description 1"));
        TaskResponse updatedTask = taskService.update(1L, new UpdateTaskRequest("Updated Task", "Updated Description"));
        // assert updatedTask.id().equals(1L);
        assert updatedTask.title().equals("Updated Task");
        assert updatedTask.description().equals("Updated Description");
        assert !updatedTask.completed();

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(TaskModel.class));
    }

    @Test
    public void testUpdateTaskNotFound() {
        when(taskRepository.findById(999L))
        .thenReturn(Optional.empty());
        assertThatThrownBy(
            () -> taskService.update(999L, new UpdateTaskRequest("Updated Task", "Updated Description")))
            .isInstanceOf(TaskNotFoundException.class)
            .hasMessageContaining("Task not found with ID: 999");

        verify(taskRepository).findById(999L);
        verify(taskRepository, never()).save(any(TaskModel.class));
    }

    @Test
    public void testCompleteTask() {
        when(taskRepository.findById(1L))
        .thenReturn(Optional.of(new TaskModel("Task 1", "Description 1", false)));
        when(taskRepository.save(any(TaskModel.class)))
        .thenReturn(new TaskModel("Task 1", "Description 1", true));
        // TaskResponse createdTask = taskService.create(new TaskRequest("Task 1", "Description 1"));
        TaskResponse completedTask = taskService.complete(1L);
        // assert completedTask.id().equals(1L);
        assert completedTask.completed();

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(TaskModel.class));
    }

    @Test
    public void testDeleteTask() {
        when(taskRepository.findById(1L))
        .thenReturn(Optional.of(new TaskModel("Task 1", "Description 1", false)));
        // TaskResponse createdTask = taskService.create(new TaskRequest("Task 1", "Description 1"));
        taskService.delete(1L);
        // assert !taskService.isExists(1L);
        verify(taskRepository).findById(1L);
        verify(taskRepository).delete(any(TaskModel.class));  
    }

    @Test void testDeleteTaskNotFound() {
        when(taskRepository.findById(999L))
        .thenReturn(Optional.empty());
        assertThatThrownBy(
            () -> taskService.delete(999L))
            .isInstanceOf(TaskNotFoundException.class)
            .hasMessageContaining("Task not found with ID: 999");

        verify(taskRepository).findById(999L);
        verify(taskRepository, never()).deleteById(999L);
    }
}
