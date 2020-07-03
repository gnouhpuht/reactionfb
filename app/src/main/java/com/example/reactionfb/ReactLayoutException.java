package com.example.reactionfb;

public class ReactLayoutException extends RuntimeException {
    private String message;

    public ReactLayoutException(String message) {
        super(message);
        this.message = message;
    }
}
