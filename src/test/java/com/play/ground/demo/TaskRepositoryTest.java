package com.play.ground.demo;

import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.play.ground.demo.model.TaskModel;
import com.play.ground.demo.respository.TaskRepository;

@DataJpaTest
class TaskRepositoryTest {
    
    @Autowired
    private TaskRepository taskRepository;

    @Test
    void testSaveTask() {
        TaskModel task = new TaskModel("Task 1", "Description 1", false);
        TaskModel savedTask = taskRepository.save(task);

        assert savedTask.getId() != null;
        assert savedTask.getName().equals("Task 1");
        assert savedTask.getDescription().equals("Description 1");
        assert !savedTask.isCompleted();
    }

    @Test
    void testFindById() {
        TaskModel task = new TaskModel("Task 2", "Description 2", false);
        TaskModel savedTask = taskRepository.save(task);

        Optional<TaskModel> foundTask = taskRepository.findById(savedTask.getId());

        assert foundTask.isPresent();
        assert foundTask.orElseThrow().getName().equals("Task 2");
        assert foundTask.orElseThrow().getDescription().equals("Description 2");
        assert !foundTask.orElseThrow().isCompleted();
    }

    @Test
    void testDeleteTask() {
        TaskModel task = new TaskModel("Task 3", "Description 3", false);
        TaskModel savedTask = taskRepository.save(task);

        taskRepository.deleteById(savedTask.getId());

        Optional<TaskModel> deletedTask = taskRepository.findById(savedTask.getId());
        assert deletedTask.isEmpty();
    }

    @Test
    void testFindIncomplete() {
        TaskModel task1 = new TaskModel("Task 1", "Description 1", false);
        TaskModel task2 = new TaskModel("Task 2", "Description 2", true);
        TaskModel task3 = new TaskModel("Task 3", "Description 3", false);

        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        List<TaskModel> incompleteTasks = taskRepository.findByCompleted(false);

        assert incompleteTasks.size() == 2;
        assert incompleteTasks.stream().anyMatch(t -> t.getName().equals("Task 1"));
        assert incompleteTasks.stream().anyMatch(t -> t.getName().equals("Task 3"));
    }
}