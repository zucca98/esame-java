package com.biblioteca.exceptions;

/**
 * Base exception class for library operations.
 * Implements Exception Shielding pattern by hiding internal errors.
 */
public class LibraryException extends Exception {
    
    public LibraryException(String message) {
        super(message);
    }
    
    public LibraryException(String message, Throwable cause) {
        super(message, cause);
    }
}