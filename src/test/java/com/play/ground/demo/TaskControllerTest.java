package com.play.ground.demo;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.play.ground.demo.dto.TaskRequest;
import com.play.ground.demo.dto.TaskResponse;
import com.play.ground.demo.service.TaskService;
import com.play.ground.demo.controller.TaskController;
import com.play.ground.demo.exception.TaskNotFoundException;
import com.play.ground.demo.common.GlobalExceptionHandler;

@WebMvcTest(TaskController.class)
@Import(GlobalExceptionHandler.class)
public class TaskControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;


    @Test
    public void testCreateTask() throws Exception {
        TaskRequest taskRequest = new TaskRequest("Test Task", "This is a test task.");
        TaskResponse taskResponse = new TaskResponse(1L, "Test Task", "This is a test task.", false);

        when(taskService.create(any())).thenReturn(taskResponse);

        mockMvc.perform(post("/tasks")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(taskResponse)));

        verify(taskService).create(any());
    }

    @Test
    public void testGetAllTasks() throws Exception {
        List<TaskResponse> taskResponses = List.of(
            new TaskResponse(1L, "Task 1", "Description 1", false),
            new TaskResponse(2L, "Task 2", "Description 2", true)
        );

        when(taskService.getAll()).thenReturn(taskResponses);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[0].description").value("Description 1"))
                .andExpect(jsonPath("$[0].completed").value(false))
                .andExpect(jsonPath("$[1].title").value("Task 2"))
                .andExpect(jsonPath("$[1].description").value("Description 2"))
                .andExpect(jsonPath("$[1].completed").value(true));

        verify(taskService).getAll();
    }

    @Test
    public void testCountTasks() throws Exception {
        when(taskService.count()).thenReturn(5);
        mockMvc.perform(get("/tasks/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
        
        verify(taskService).count();
    }

    @Test
    public void testCountTasksEmpty() throws Exception {
        when(taskService.getAll()).thenReturn(List.of());
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        
        verify(taskService).getAll();
    }

    @Test
    public void testCreateTaskTitleBlank() throws Exception {
        TaskRequest invalidRequest = new TaskRequest("", "This is a test task.");
        mockMvc.perform(post("/tasks")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Validation failed for one or more fields."))
                .andExpect(jsonPath("$.errors.title").value("Title is required"));
        
        verifyNoInteractions(taskService);
    }

    @Test
    public void testCreateTaskTitleMissing() throws Exception {
        String invalidJson = "{\"description\": \"This is a test task.\"}";
        mockMvc.perform(post("/tasks")
                .contentType("application/json")
                .content(invalidJson))
                .andExpect(status().isBadRequest());
        
        verifyNoInteractions(taskService);
    }

    @Test
    public void testCreateTaskTitleTooLong() throws Exception {
        String longTitle = "A".repeat(101);
        TaskRequest invalidRequest = new TaskRequest(longTitle, "This is a test task.");
        mockMvc.perform(post("/tasks")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        
        verifyNoInteractions(taskService);
    }

    @Test
    public void testCreateTaskTitleHundredChars() throws Exception {
        String longTitle = "A".repeat(100);
        TaskRequest validRequest = new TaskRequest(longTitle, "This is a test task.");
        TaskResponse taskResponse = new TaskResponse(1L, longTitle, "This is a test task.", false);

        when(taskService.create(any())).thenReturn(taskResponse);

        mockMvc.perform(post("/tasks")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(taskResponse)));

        verify(taskService).create(any());
    }

    @Test
    public void testGetTaskById() throws Exception {
        TaskResponse taskResponse = new TaskResponse(1L, "Task 1", "Description 1", false);
        when(taskService.getById(1L)).thenReturn(taskResponse);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(taskResponse)));

        verify(taskService).getById(1L);
    }

    @Test
    public void testGetTaskByIdNotFound() throws Exception {
        when(taskService.getById(999L))
        .thenThrow(new TaskNotFoundException(999L));

        mockMvc.perform(get("/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("TASK_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Task not found with ID: 999"))
                .andExpect(jsonPath("$.path").value("/tasks/999"));

        verify(taskService).getById(999L);
    }

    @Test
    public void testGetTaskByIdInvalidId() throws Exception {
        mockMvc.perform(get("/tasks/abc"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(taskService);
    }

    @Test
    public void testTaskIdExists() throws Exception {
        when(taskService.isExists(1L)).thenReturn(true);

        mockMvc.perform(get("/tasks/1/exists"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(taskService).isExists(1L);
    }

    @Test
    public void testTaskIdNotExists() throws Exception {
        when(taskService.isExists(999L)).thenReturn(false);

        mockMvc.perform(get("/tasks/999/exists"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(taskService).isExists(999L);
    }
}