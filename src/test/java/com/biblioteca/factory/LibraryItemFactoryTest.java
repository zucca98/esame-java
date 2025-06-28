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

    @Test
    @DisplayName("Should throw exception for purely numeric author")
    void testCreateBookNumericOnlyAuthor() {
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> LibraryItemFactory.createBook("123", "Title", "123", 100)
        );
        assertTrue(exception.getMessage().contains("cannot consist entirely of numbers"));
    }

    @Test
    @DisplayName("Should throw exception for numeric author with separators")
    void testCreateBookNumericAuthorWithSeparators() {
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> LibraryItemFactory.createBook("123", "Title", "123-456", 100)
        );
        assertTrue(exception.getMessage().contains("cannot consist entirely of numbers"));
    }

    @Test
    @DisplayName("Should accept valid author with numbers as part of name")
    void testCreateBookValidAuthorWithNumbers() throws InvalidDataException {
        LibraryItem book = LibraryItemFactory.createBook(
                "978-0134685991", "Effective Java", "John Smith Jr. 3rd", 416);

        assertNotNull(book);
        assertTrue(book instanceof Book);
        assertEquals("John Smith Jr. 3rd", ((Book) book).getAuthor());
    }

    @Test
    @DisplayName("Should accept author with mixed alphanumeric characters")
    void testCreateBookMixedAlphanumericAuthor() throws InvalidDataException {
        LibraryItem book = LibraryItemFactory.createBook(
                "978-0134685991", "Programming Book", "Author123", 300);

        assertNotNull(book);
        assertTrue(book instanceof Book);
        assertEquals("Author123", ((Book) book).getAuthor());
    }

    @Test
    @DisplayName("Should throw exception for author with only special characters and numbers")
    void testCreateBookSpecialCharactersAndNumbers() {
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> LibraryItemFactory.createBook("123", "Title", "123@456#789", 100)
        );
        assertTrue(exception.getMessage().contains("cannot consist entirely of numbers"));
    }
}
