package com.example.demo.service.exceptions;

public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException() {
        super("Data not found!");
    }

    public DataNotFoundException(Long id) {
        super("Data with id = " + id + "not found!");
    }
}
