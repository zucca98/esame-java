
package com.biblioteca.exceptions;

/**
 * Exception thrown when a book is not found in the library.
 */
public class BookNotFoundException extends LibraryException {
    
    public BookNotFoundException(String isbn) {
        super("Book with ISBN " + isbn + " not found");
    }
}
