package com.biblioteca.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.biblioteca.exceptions.InvalidDataException;
import com.biblioteca.model.Book;
import com.biblioteca.model.LibraryItem;
import com.biblioteca.model.Magazine;

/**
 * JUnit tests for LibraryItemFactory.
 * Demonstrates JUnit Testing technology requirement.
 */
class LibraryItemFactoryTest {
    
    @Test
    @DisplayName("Should create book with valid data")
    void testCreateBookValid() throws InvalidDataException {
        LibraryItem book = LibraryItemFactory.createBook(
                "978-0134685991", "Effective Java", "Joshua Bloch", 416);
        
        assertNotNull(book);
        assertTrue(book instanceof Book);
        assertEquals("978-0134685991", book.getId());
        assertEquals("Effective Java", book.getTitle());
        assertEquals("Book", book.getType());
        assertTrue(book.isAvailable());
    }
    
    @Test
    @DisplayName("Should create magazine with valid data")
    void testCreateMagazineValid() throws InvalidDataException {
        LibraryItem magazine = LibraryItemFactory.createMagazine(
                "1234-5678", "Java Magazine", 45);
        
        assertNotNull(magazine);
        assertTrue(magazine instanceof Magazine);
        assertEquals("1234-5678", magazine.getId());
        assertEquals("Java Magazine", magazine.getTitle());
        assertEquals("Magazine", magazine.getType());
        assertTrue(magazine.isAvailable());
    }
    
    @Test
    @DisplayName("Should throw exception for empty ISBN")
    void testCreateBookEmptyIsbn() {
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> LibraryItemFactory.createBook("", "Title", "Author", 100)
        );
        assertTrue(exception.getMessage().contains("ISBN cannot be empty"));
    }
    
    @Test
    @DisplayName("Should throw exception for negative pages")
    void testCreateBookNegativePages() {
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> LibraryItemFactory.createBook("123", "Title", "Author", -1)
        );
        assertTrue(exception.getMessage().contains("Pages must be positive"));
    }
    
    @Test
    @DisplayName("Should throw exception for null title")
    void testCreateBookNullTitle() {
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> LibraryItemFactory.createBook("123", null, "Author", 100)
        );
        assertTrue(exception.getMessage().contains("Title cannot be empty"));
    }
    
    @Test
    @DisplayName("Should throw exception for invalid magazine issue")
    void testCreateMagazineInvalidIssue() {
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> LibraryItemFactory.createMagazine("1234-5678", "Title", 0)
        );
        assertTrue(exception.getMessage().contains("Issue number must be positive"));
    }
}
