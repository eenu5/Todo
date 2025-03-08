package com.dhairya.todo.models;

public class ErrorResponse {
    String message;
    String probableCause;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public ErrorResponse setMessage(String message) {
        this.message = message;
        return this;
    }
}
