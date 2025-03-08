package com.dhairya.todo.service;

import com.dhairya.todo.models.Task;
import com.dhairya.todo.models.TaskRequest;
import com.dhairya.todo.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(TaskRequest taskRequest) {
        Task task = new Task(taskRequest);
        return taskRepository.save(task);
    }

    public ResponseEntity<Object> createSubTask(Long id, TaskRequest taskRequest) {
        Optional<Task> parentTask = taskRepository.findById(id);
        if (!parentTask.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Task task = new Task(id, taskRequest);
        taskRepository.save(task);
        return ResponseEntity.ok(task);
    }

    public ResponseEntity<Task> updateTask(Long id, TaskRequest taskRequest) {
        Optional<Task> task = taskRepository.findById(id);
        if (!task.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        task.get().setName(taskRequest.getName())
                .setDescription(taskRequest.getDescription())
                .setDueDate(taskRequest.getDueDate()) // If NULL is passed, we assume users wants to delete the value.
                .setPriority(taskRequest.getPriority())
                .setStatus(taskRequest.getStatus());
        taskRepository.save(task.get());
        return ResponseEntity.ok(task.get());
    }

    public ResponseEntity<Object> updateSubTask(Long id, Long subTaskId, TaskRequest taskRequest) {
        Optional<Task> parentTask = taskRepository.findById(id);
        if (!parentTask.isPresent()) {
            return ResponseEntity.status(404).body("Parent task not found");
        }
        Optional<Task> subTask = taskRepository.findById(subTaskId);
        if (!subTask.isPresent()) {
            return ResponseEntity.status(404).body("Sub task not found");
        }
        subTask.get().setName(taskRequest.getName())
                .setDescription(taskRequest.getDescription())
                .setDueDate(taskRequest.getDueDate()) // If NULL is passed, we assume users wants to delete the value.
                .setPriority(taskRequest.getPriority())
                .setStatus(taskRequest.getStatus());
        taskRepository.save(subTask.get());
        return ResponseEntity.ok(subTask.get());
    }

    public ResponseEntity<Object> updateTaskStatus(Long id, Integer status) {
        Optional<Task> task = taskRepository.findById(id);
        if (!task.isPresent()) {
            return ResponseEntity.status(404).body("Parent task not found");
        }
        task.get().setStatus(status);
        taskRepository.save(task.get());
        return ResponseEntity.ok(task.get());
    }

    public ResponseEntity<Object> updateSubTaskStatus(Long id, Long subTaskId, Integer status) {
        Optional<Task> parentTask = taskRepository.findById(id);
        if (!parentTask.isPresent()) {
            return ResponseEntity.status(404).body("Parent task not found");
        }
        Optional<Task> subTask = taskRepository.findById(subTaskId);
        if (!subTask.isPresent()) {
            return ResponseEntity.status(404).body("Sub task not found");
        }
        subTask.get().setStatus(status);
        taskRepository.save(subTask.get());
        return ResponseEntity.ok(subTask.get());
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
