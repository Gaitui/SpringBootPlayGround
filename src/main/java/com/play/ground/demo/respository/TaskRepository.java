package com.play.ground.demo.respository;

import com.play.ground.demo.model.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskModel, Long> {

    List<TaskModel> findByCompleted(boolean completed);
    
}
