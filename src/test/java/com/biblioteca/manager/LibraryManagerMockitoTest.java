package com.biblioteca.manager;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.biblioteca.exceptions.InvalidDataException;
import com.biblioteca.exceptions.LibraryException;
import com.biblioteca.factory.LibraryItemFactory;
import com.biblioteca.iterator.LibraryCollection;
import com.biblioteca.model.LibraryItem;
import com.biblioteca.strategy.SearchStrategy;

/**
 * Test avanzati per LibraryManager utilizzando Mockito.
 * Dimostra mocking di dipendenze, stubbing, verification e testing del Singleton.
 */
@ExtendWith(MockitoExtension.class)
class LibraryManagerMockitoTest {

    @Mock
    private LibraryCollection mockCollection;
    
    @Mock
    private SearchStrategy mockSearchStrategy;
    
    @Mock
    private LibraryItem mockBook;
    
    @Mock
    private LibraryItem mockMagazine;
    
    private LibraryManager libraryManager;
    private Map<String, LibraryItem> mockItemsById;

    /**
     * Metodo di setup chiamato automaticamente da JUnit 5 prima di ogni test.
     * Resetta il Singleton e inietta le dipendenze mock.
     */
    @BeforeEach
    public void setUp() throws Exception {
        // Reset del Singleton per ogni test
        resetSingleton();

        // Crea una nuova istanza e inietta i mock
        libraryManager = LibraryManager.getInstance();
        mockItemsById = new HashMap<>();

        // Inietta i mock usando reflection
        injectMockDependencies();
    }

    @Test
    @DisplayName("Should add book successfully with mocked dependencies")
    void testAddBookWithMockedDependencies() throws LibraryException {
        // Arrange
        String isbn = "978-0134685991";
        String title = "Effective Java";
        String author = "Joshua Bloch";
        int pages = 416;
        
        try (MockedStatic<LibraryItemFactory> mockedFactory = mockStatic(LibraryItemFactory.class)) {
            // Mock del factory per restituire il nostro mock book
            mockedFactory.when(() -> LibraryItemFactory.createBook(isbn, title, author, pages))
                        .thenReturn(mockBook);
            
            // Act
            libraryManager.addBook(isbn, title, author, pages);
            
            // Assert & Verify
            mockedFactory.verify(() -> LibraryItemFactory.createBook(isbn, title, author, pages));
            verify(mockCollection).addItem(mockBook);
            assertTrue(mockItemsById.containsKey(isbn));
            assertEquals(mockBook, mockItemsById.get(isbn));
        }
    }

    @Test
    @DisplayName("Should handle duplicate ISBN when adding book")
    void testAddBookWithDuplicateISBN() throws LibraryException {
        // Arrange
        String isbn = "978-0134685991";
        mockItemsById.put(isbn, mockBook);
        
        // Act & Assert
        InvalidDataException exception = assertThrows(
            InvalidDataException.class,
            () -> libraryManager.addBook(isbn, "Title", "Author", 100)
        );
        
        assertTrue(exception.getMessage().contains("already exists"));
        
        // Verify che il factory non è stato chiamato
        try (MockedStatic<LibraryItemFactory> mockedFactory = mockStatic(LibraryItemFactory.class)) {
            mockedFactory.verifyNoInteractions();
        }
        
        // Verify che la collection non è stata modificata
        verify(mockCollection, never()).addItem(any());
    }

    @Test
    @DisplayName("Should add magazine successfully with mocked dependencies")
    void testAddMagazineWithMockedDependencies() throws LibraryException {
        // Arrange
        String issn = "1234-5678";
        String title = "Java Magazine";
        int issueNumber = 45;
        
        try (MockedStatic<LibraryItemFactory> mockedFactory = mockStatic(LibraryItemFactory.class)) {
            // Mock del factory per restituire il nostro mock magazine
            mockedFactory.when(() -> LibraryItemFactory.createMagazine(issn, title, issueNumber))
                        .thenReturn(mockMagazine);
            
            // Act
            libraryManager.addMagazine(issn, title, issueNumber);
            
            // Assert & Verify
            mockedFactory.verify(() -> LibraryItemFactory.createMagazine(issn, title, issueNumber));
            verify(mockCollection).addItem(mockMagazine);
            assertTrue(mockItemsById.containsKey(issn));
            assertEquals(mockMagazine, mockItemsById.get(issn));
        }
    }

    @Test
    @DisplayName("Should handle factory exception when adding book")
    void testAddBookWithFactoryException() throws InvalidDataException {
        // Arrange
        String isbn = "invalid-isbn";
        
        try (MockedStatic<LibraryItemFactory> mockedFactory = mockStatic(LibraryItemFactory.class)) {
            // Mock del factory per lanciare eccezione
            mockedFactory.when(() -> LibraryItemFactory.createBook(anyString(), anyString(), anyString(), anyInt()))
                        .thenThrow(new InvalidDataException("Invalid ISBN format"));
            
            // Act & Assert
            InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> libraryManager.addBook(isbn, "Title", "Author", 100)
            );
            
            assertTrue(exception.getMessage().contains("Invalid ISBN format"));
            
            // Verify che la collection non è stata modificata
            verify(mockCollection, never()).addItem(any());
            assertFalse(mockItemsById.containsKey(isbn));
        }
    }

    @Test
    @DisplayName("Should perform search using mocked strategy")
    void testSearchWithMockedStrategy() {
        // Arrange
        String query = "Java";
        List<LibraryItem> expectedResults = Arrays.asList(mockBook, mockMagazine);
        List<LibraryItem> mockItems = Arrays.asList(mockBook, mockMagazine);
        
        // Mock della collection per restituire items
        when(mockCollection.getItems()).thenReturn(mockItems);
        
        // Mock della strategy per restituire risultati filtrati
        when(mockSearchStrategy.search(mockItems, query)).thenReturn(expectedResults);
        
        // Act
        List<LibraryItem> results = libraryManager.search(mockSearchStrategy, query);
        
        // Assert & Verify
        assertEquals(expectedResults, results);
        verify(mockCollection).getItems();
        verify(mockSearchStrategy).search(mockItems, query);
    }

    @Test
    @DisplayName("Should handle empty search results")
    void testSearchWithEmptyResults() {
        // Arrange
        String query = "NonExistent";
        List<LibraryItem> mockItems = Arrays.asList(mockBook, mockMagazine);
        List<LibraryItem> emptyResults = Arrays.asList();
        
        when(mockCollection.getItems()).thenReturn(mockItems);
        when(mockSearchStrategy.search(mockItems, query)).thenReturn(emptyResults);
        
        // Act
        List<LibraryItem> results = libraryManager.search(mockSearchStrategy, query);
        
        // Assert
        assertTrue(results.isEmpty());
        verify(mockSearchStrategy).search(mockItems, query);
    }

    @Test
    @DisplayName("Should verify singleton behavior with mocked dependencies")
    void testSingletonBehaviorWithMocks() {
        // Act
        LibraryManager instance1 = LibraryManager.getInstance();
        LibraryManager instance2 = LibraryManager.getInstance();
        
        // Assert
        assertSame(instance1, instance2);
        assertSame(libraryManager, instance1);
    }

    @Test
    @DisplayName("Should handle getTotalItems with mocked collection")
    void testGetTotalItemsWithMockedCollection() {
        // Arrange
        int expectedSize = 5;
        when(mockCollection.size()).thenReturn(expectedSize);
        
        // Act
        int totalItems = libraryManager.getTotalItems();
        
        // Assert
        assertEquals(expectedSize, totalItems);
        verify(mockCollection).size();
    }

    /**
     * Metodo helper per resettare il Singleton usando reflection
     */
    private void resetSingleton() throws Exception {
        Field instanceField = LibraryManager.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
    }

    /**
     * Metodo helper per iniettare le dipendenze mock usando reflection
     */
    private void injectMockDependencies() throws Exception {
        // Inietta la mock collection
        Field collectionField = LibraryManager.class.getDeclaredField("collection");
        collectionField.setAccessible(true);
        collectionField.set(libraryManager, mockCollection);
        
        // Inietta la mock map
        Field itemsByIdField = LibraryManager.class.getDeclaredField("itemsById");
        itemsByIdField.setAccessible(true);
        itemsByIdField.set(libraryManager, mockItemsById);
    }
}
