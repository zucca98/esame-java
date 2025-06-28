package com.biblioteca.iterator;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.biblioteca.exceptions.InvalidDataException;
import com.biblioteca.factory.LibraryItemFactory;
import com.biblioteca.model.LibraryItem;

/**
 * JUnit tests for LibraryIterator.
 * Tests Iterator Pattern implementation.
 */
class LibraryIteratorTest {
    
    private LibraryCollection collection;
    
    @BeforeEach
    public void setUp() throws InvalidDataException {
        collection = new LibraryCollection();
        
        LibraryItem book1 = LibraryItemFactory.createBook("123", "Book 1", "Author 1", 100);
        LibraryItem book2 = LibraryItemFactory.createBook("456", "Book 2", "Author 2", 200);
        LibraryItem magazine = LibraryItemFactory.createMagazine("789", "Magazine 1", 1);
        
        collection.addItem(book1);
        collection.addItem(book2);
        collection.addItem(magazine);
    }
    
    @Test
    @DisplayName("Should iterate through all items")
    void testIteratorHasNext() {
        LibraryIterator iterator = collection.createIterator();
        
        assertTrue(iterator.hasNext());
        LibraryItem item1 = iterator.next();
        assertNotNull(item1);
        
        assertTrue(iterator.hasNext());
        LibraryItem item2 = iterator.next();
        assertNotNull(item2);
        
        assertTrue(iterator.hasNext());
        LibraryItem item3 = iterator.next();
        assertNotNull(item3);
        
        assertFalse(iterator.hasNext());
    }
    
    @Test
    @DisplayName("Should throw exception when no more elements")
    void testIteratorNoSuchElement() {
        LibraryIterator iterator = collection.createIterator();
        
        // Skip all elements
        while (iterator.hasNext()) {
            LibraryItem item = iterator.next();
            assertNotNull(item); // Use the result to avoid warnings
        }
        
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, iterator::next);
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("No more items"));
    }
    
    @Test
    @DisplayName("Should reset iterator position")
    void testIteratorReset() {
        LibraryIterator iterator = collection.createIterator();
        
        LibraryItem item1 = iterator.next();
        LibraryItem item2 = iterator.next();
        assertNotNull(item1);
        assertNotNull(item2);
        assertEquals(2, iterator.getCurrentPosition());
        
        iterator.reset();
        assertEquals(0, iterator.getCurrentPosition());
        assertTrue(iterator.hasNext());
    }
    
    @Test
    @DisplayName("Should work with for-each loop")
    void testForEachLoop() {
        int count = 0;
        for (LibraryItem item : collection) {
            assertNotNull(item);
            count++;
        }
        assertEquals(3, count);
    }
}
