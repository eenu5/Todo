package com.dhairya.todo.service;

import com.dhairya.todo.models.ErrorResponse;
import com.dhairya.todo.models.PatchRequest;
import com.dhairya.todo.models.Task;
import com.dhairya.todo.models.TaskRequest;
import com.dhairya.todo.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        List<Task> tasks =  taskRepository.findAll();
        for (Task task : tasks) {
            task.setProgress(findProgress(task.getId()));
        }
        return tasks;
    }

    public List<Task> getTaskById(Long id) {
        List<Task> tasks = new ArrayList<>();
        Optional<Task> task = taskRepository.findById(id);
        if(task.isPresent()) {
            List<Task> subTasks = taskRepository.findAllByParentId(id);
            task.get().setProgress(findProgress(id));
            tasks.addAll(subTasks);
            tasks.add(task.get());
        }
        return tasks;
    }

    private Double findProgress(Long id) {
        int count = 0;
        int completedTasks =0;
        List<Task> subTasks = taskRepository.findAllByParentId(id);
        for (Task subTask : subTasks) {
            count++;
            if(subTask.getStatus() == Task.Status.COMPLETED) {
                completedTasks++;
            }
        }
        Double progress = null;
        if(count>0){
            progress = (double) ((completedTasks * 100) / count);
        }
        return progress;
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

    public ResponseEntity<Object> updateTask(Long id, TaskRequest taskRequest) {
        Optional<Task> task = taskRepository.findById(id);
        if (!task.isPresent()) {
            return ResponseEntity.status(404).body(new ErrorResponse("Parent task not found"));
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
            return ResponseEntity.status(404).body(new ErrorResponse("Parent task not found"));
        }
        Optional<Task> subTask = taskRepository.findById(subTaskId);
        if (!subTask.isPresent()) {
            return ResponseEntity.status(404).body(new ErrorResponse("Sub task not found"));
        }
        subTask.get().setName(taskRequest.getName())
                .setDescription(taskRequest.getDescription())
                .setDueDate(taskRequest.getDueDate()) // If NULL is passed, we assume users wants to delete the value.
                .setPriority(taskRequest.getPriority())
                .setStatus(taskRequest.getStatus());
        taskRepository.save(subTask.get());
        return ResponseEntity.ok(subTask.get());
    }

    public ResponseEntity<Object> updateTaskStatus(Long id, PatchRequest status) {
        Optional<Task> task = taskRepository.findById(id);
        if (!task.isPresent()) {
            return ResponseEntity.status(404).body(new ErrorResponse("Parent task not found"));
        }
        task.get().setStatus(status.getStatus());
        taskRepository.save(task.get());
        return ResponseEntity.ok(task.get());
    }

    public ResponseEntity<Object> updateSubTaskStatus(Long id, Long subTaskId, PatchRequest status) {
        Optional<Task> parentTask = taskRepository.findById(id);
        if (!parentTask.isPresent()) {
            return ResponseEntity.status(404).body(new ErrorResponse("Parent task not found"));
        }
        Optional<Task> subTask = taskRepository.findById(subTaskId);
        if (!subTask.isPresent()) {
            return ResponseEntity.status(404).body(new ErrorResponse("Sub task not found"));
        }
        subTask.get().setStatus(status.getStatus());
        taskRepository.save(subTask.get());
        return ResponseEntity.ok(subTask.get());
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
        List<Task> subTasks = taskRepository.findAllByParentId(id);
        for (Task subTask : subTasks) {
            taskRepository.deleteById(subTask.getId());
        }
    }
}
