package com.biblioteca.exceptions;

/**
 * Exception thrown for invalid input data.
 */
public class InvalidDataException extends LibraryException {
    
    public InvalidDataException(String message) {
        super("Invalid data: " + message);
    }
}