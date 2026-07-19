package com.play.ground.demo;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.play.ground.demo.dto.TaskRequest;
import com.play.ground.demo.dto.TaskResponse;
import com.play.ground.demo.service.TaskService;
import com.play.ground.demo.controller.TaskController;

@WebMvcTest(TaskController.class)
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
}
