package com.dhairya.todo.controllers;

import com.dhairya.todo.models.PatchRequest;
import com.dhairya.todo.models.Task;
import com.dhairya.todo.models.TaskRequest;
import com.dhairya.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        if (tasks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        tasks = tasks.stream().filter(t->t.getParentId() == null).collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Task>> getTaskById(@PathVariable Long id) {
        List<Task> tasks = taskService.getTaskById(id);
        if (tasks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public Task createTask(@RequestBody TaskRequest task) {
        return taskService.createTask(task);
    }

    @PostMapping("/{id}/sub-tasks")
    public ResponseEntity<Object> createSubTask(@PathVariable Long id, @RequestBody TaskRequest task) {
        return taskService.createSubTask(id, task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable Long id, @RequestBody TaskRequest task) {
        return taskService.updateTask(id, task);
    }

    @PutMapping("/{id}/sub-tasks/{subTaskId}")
    public ResponseEntity<Object> updateTask(@PathVariable Long id, @PathVariable Long subTaskId, @RequestBody TaskRequest task) {
        return taskService.updateSubTask(id, subTaskId, task);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Object> updateTaskStatus(@PathVariable Long id, @RequestBody PatchRequest status) {
        return taskService.updateTaskStatus(id, status);
    }

    @PatchMapping("/{id}/sub-tasks/{subTaskId}/status")
    public ResponseEntity<Object> updateSubTaskStatus(@PathVariable Long id, @PathVariable Long subTaskId, @RequestBody PatchRequest status) {
        return taskService.updateSubTaskStatus(id, subTaskId, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

}
