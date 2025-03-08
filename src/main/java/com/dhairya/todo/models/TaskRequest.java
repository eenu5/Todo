package com.dhairya.todo.models;

import java.util.Date;

public class TaskRequest {
    String name;
    String description;
    Date dueDate;
    Task.Priority priority;
    Task.Status status;

    public String getName() {
        return name;
    }

    public TaskRequest setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TaskRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public TaskRequest setDueDate(Date dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public Task.Priority getPriority() {
        return priority;
    }

    public TaskRequest setPriority(Task.Priority priority) {
        this.priority = priority;
        return this;
    }

    public Task.Status getStatus() {
        return status;
    }

    public TaskRequest setStatus(Task.Status status) {
        this.status = status;
        return this;
    }
}
