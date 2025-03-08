package com.dhairya.todo.repositories;

import com.dhairya.todo.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository  extends JpaRepository<Task, Long> {

    public List<Task> findAllByParentId(Long id);

}
