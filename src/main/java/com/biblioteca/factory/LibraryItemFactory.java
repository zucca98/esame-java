package com.biblioteca.factory;

import com.biblioteca.exceptions.InvalidDataException;
import com.biblioteca.model.Book;
import com.biblioteca.model.LibraryItem;
import com.biblioteca.model.Magazine;
import com.biblioteca.util.AuthorValidator;

/**
 * Factory Pattern implementation for creating LibraryItem objects.
 */
public class LibraryItemFactory {
    
    public static LibraryItem createBook(String isbn, String title, String author, int pages) 
            throws InvalidDataException {
        validateBookData(isbn, title, author, pages);
        
        return new Book.BookBuilder()
                .setIsbn(isbn)
                .setTitle(title)
                .setAuthor(author)
                .setPages(pages)
                .build();
    }
    
    public static LibraryItem createMagazine(String issn, String title, int issueNumber) 
            throws InvalidDataException {
        validateMagazineData(issn, title, issueNumber);
        
        return new Magazine(issn, title, issueNumber);
    }
    
    private static void validateBookData(String isbn, String title, String author, int pages) 
            throws InvalidDataException {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new InvalidDataException("ISBN cannot be empty");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidDataException("Title cannot be empty");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new InvalidDataException("Author cannot be empty");
        }
        if (!AuthorValidator.isValidAuthor(author)) {
            throw new InvalidDataException(AuthorValidator.getValidationErrorMessage(author));
        }
        if (pages <= 0) {
            throw new InvalidDataException("Pages must be positive");
        }
    }
    
    private static void validateMagazineData(String issn, String title, int issueNumber) 
            throws InvalidDataException {
        if (issn == null || issn.trim().isEmpty()) {
            throw new InvalidDataException("ISSN cannot be empty");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidDataException("Title cannot be empty");
        }
        if (issueNumber <= 0) {
            throw new InvalidDataException("Issue number must be positive");
        }
    }
}