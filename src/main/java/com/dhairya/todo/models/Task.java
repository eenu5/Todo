package com.dhairya.todo.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String description;
    Date dueDate;
    @Enumerated(EnumType.STRING)
    Priority priority;
    @Enumerated(EnumType.STRING)
    Status status;
    Long parentId;
    Double progress;

    public Task(TaskRequest taskRequest) {
        this.name = taskRequest.getName();
        this.description = taskRequest.getDescription();
        this.dueDate = taskRequest.getDueDate();
        this.priority = taskRequest.getPriority() != null ? taskRequest.getPriority() : Priority.LOW;// Default priority is LOW
        this.status = taskRequest.getStatus() != null ? taskRequest.getStatus() : Status.PENDING; // Default status is PENDING
    }

    public Task(Long parentId, TaskRequest taskRequest) {
        this.parentId = parentId;
        this.name = taskRequest.getName();
        this.description = taskRequest.getDescription();
        this.dueDate = taskRequest.getDueDate();
        this.priority = taskRequest.getPriority() != null ? taskRequest.getPriority() : Priority.LOW;// Default priority is LOW
        this.status = taskRequest.getStatus() != null ? taskRequest.getStatus() : Status.PENDING; // Default status is PENDING
    }

    public Task() {

    }


    public enum Status { // To add more types of status
        PENDING(100),
        COMPLETED(200);

        private final int value;

        Status(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }

        @JsonCreator
        public static Status fromValue(int value) {
            for (Status status : Status.values()) {
                if (status.value == value) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Invalid Status value: " + value);
        }
    }

    public enum Priority { // To add more types of priority
        LOW(100),
        MEDIUM(200),
        HIGH(300);

        private final int value;

        Priority(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }

        @JsonCreator
        public static Priority fromValue(int value) {
            for (Priority priority : Priority.values()) {
                if (priority.value == value) {
                    return priority;
                }
            }
            throw new IllegalArgumentException("Invalid priority value: " + value);
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Task setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Task setDescription(String description) {
        this.description = description;
        return this;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Task setDueDate(Date dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public Priority getPriority() {
        return priority;
    }

    public Task setPriority(Priority priority) {
        this.priority = priority;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public Task setStatus(Integer status) {
        this.status = Status.fromValue(status);
        return this;
    }

    public Task setStatus(Status status) {
        this.status = status;
        return this;
    }

    public Long getParentId() {
        return parentId;
    }

    public Task setParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public Double getProgress() {
        return progress;
    }

    public Task setProgress(Double progress) {
        this.progress = progress;
        return this;
    }
}
