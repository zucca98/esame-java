package com.biblioteca.strategy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.biblioteca.model.LibraryItem;

/**
 * Test avanzati per SearchStrategy implementations utilizzando Mockito.
 * Versione semplificata per evitare UnnecessaryStubbing.
 */
@ExtendWith(MockitoExtension.class)
class SearchStrategyAdvancedTest {

    @Mock
    private LibraryItem mockBook1;
    
    @Mock
    private LibraryItem mockBook2;
    
    @Mock
    private LibraryItem mockMagazine1;
    
    @Mock
    private LibraryItem mockMagazine2;
    
    private TitleSearchStrategy titleSearchStrategy;
    private IdSearchStrategy idSearchStrategy;



    @Test
    @DisplayName("Should find items by title using mocked LibraryItems")
    void testTitleSearchWithMockedItems() {
        // Arrange
        titleSearchStrategy = new TitleSearchStrategy();
        String query = "Java";
        List<LibraryItem> mockItems = Arrays.asList(mockBook1, mockBook2, mockMagazine1, mockMagazine2);
        
        // Setup only the mocks we need for this test
        when(mockBook1.getTitle()).thenReturn("Effective Java");
        when(mockBook2.getTitle()).thenReturn("Design Patterns");
        when(mockMagazine1.getTitle()).thenReturn("Java Magazine");
        when(mockMagazine2.getTitle()).thenReturn("Programming Weekly");
        
        // Act
        List<LibraryItem> results = titleSearchStrategy.search(mockItems, query);
        
        // Assert
        assertEquals(2, results.size());
        assertTrue(results.contains(mockBook1));
        assertTrue(results.contains(mockMagazine1));
        
        // Verify che getTitle() è stato chiamato su tutti gli items
        verify(mockBook1, atLeastOnce()).getTitle();
        verify(mockBook2, atLeastOnce()).getTitle();
        verify(mockMagazine1, atLeastOnce()).getTitle();
        verify(mockMagazine2, atLeastOnce()).getTitle();
    }

    @Test
    @DisplayName("Should handle case insensitive search with mocked items")
    void testTitleSearchCaseInsensitive() {
        // Arrange
        titleSearchStrategy = new TitleSearchStrategy();
        String query = "EFFECTIVE"; // uppercase query
        List<LibraryItem> mockItems = Arrays.asList(mockBook1);
        
        when(mockBook1.getTitle()).thenReturn("Effective Java");
        
        // Act
        List<LibraryItem> results = titleSearchStrategy.search(mockItems, query);
        
        // Assert
        assertEquals(1, results.size());
        assertTrue(results.contains(mockBook1));
        
        // Verify interactions
        verify(mockBook1, atLeastOnce()).getTitle();
    }

    @Test
    @DisplayName("Should return empty list when no titles match")
    void testTitleSearchNoMatches() {
        // Arrange
        titleSearchStrategy = new TitleSearchStrategy();
        String query = "NonExistent";
        List<LibraryItem> mockItems = Arrays.asList(mockBook1, mockBook2);
        
        when(mockBook1.getTitle()).thenReturn("Effective Java");
        when(mockBook2.getTitle()).thenReturn("Design Patterns");
        
        // Act
        List<LibraryItem> results = titleSearchStrategy.search(mockItems, query);
        
        // Assert
        assertTrue(results.isEmpty());
        
        // Verify che tutti gli items sono stati controllati
        verify(mockBook1, atLeastOnce()).getTitle();
        verify(mockBook2, atLeastOnce()).getTitle();
    }

    @Test
    @DisplayName("Should find items by exact ID match using mocked items")
    void testIdSearchExactMatch() {
        // Arrange
        idSearchStrategy = new IdSearchStrategy();
        String query = "978-0134685991";
        List<LibraryItem> mockItems = Arrays.asList(mockBook1, mockBook2);
        
        when(mockBook1.getId()).thenReturn("978-0134685991");
        when(mockBook2.getId()).thenReturn("978-0201633610");
        
        // Act
        List<LibraryItem> results = idSearchStrategy.search(mockItems, query);
        
        // Assert
        assertEquals(1, results.size());
        assertTrue(results.contains(mockBook1));
        
        // Verify che getId() è stato chiamato
        verify(mockBook1, atLeastOnce()).getId();
    }

    @Test
    @DisplayName("Should find items by partial ID match with mocked items")
    void testIdSearchPartialMatch() {
        // Arrange
        idSearchStrategy = new IdSearchStrategy();
        String query = "134"; // Partial match per mockBook1
        List<LibraryItem> mockItems = Arrays.asList(mockBook1, mockBook2);
        
        when(mockBook1.getId()).thenReturn("978-0134685991");
        when(mockBook2.getId()).thenReturn("978-0201633610");
        
        // Act
        List<LibraryItem> results = idSearchStrategy.search(mockItems, query);
        
        // Assert
        assertEquals(1, results.size());
        assertTrue(results.contains(mockBook1));
        
        // Verify interactions
        verify(mockBook1, atLeastOnce()).getId();
    }

    @Test
    @DisplayName("Should handle insufficient digits in partial search")
    void testIdSearchInsufficientDigits() {
        // Arrange
        idSearchStrategy = new IdSearchStrategy();
        String query = "12"; // Solo 2 cifre, insufficienti per ricerca parziale
        List<LibraryItem> mockItems = Arrays.asList(mockBook1);
        
        // Act
        List<LibraryItem> results = idSearchStrategy.search(mockItems, query);
        
        // Assert
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Should handle empty query gracefully")
    void testSearchWithEmptyQuery() {
        // Arrange
        titleSearchStrategy = new TitleSearchStrategy();
        idSearchStrategy = new IdSearchStrategy();
        List<LibraryItem> mockItems = Arrays.asList(mockBook1);
        
        // Act & Assert per TitleSearchStrategy
        List<LibraryItem> titleResults = titleSearchStrategy.search(mockItems, "");
        assertTrue(titleResults.isEmpty());
        
        // Act & Assert per IdSearchStrategy
        List<LibraryItem> idResults = idSearchStrategy.search(mockItems, "");
        assertTrue(idResults.isEmpty());
    }

    @Test
    @DisplayName("Should handle null query gracefully")
    void testSearchWithNullQuery() {
        // Arrange
        titleSearchStrategy = new TitleSearchStrategy();
        idSearchStrategy = new IdSearchStrategy();
        List<LibraryItem> mockItems = Arrays.asList(mockBook1);
        
        // Act & Assert per TitleSearchStrategy
        List<LibraryItem> titleResults = titleSearchStrategy.search(mockItems, null);
        assertTrue(titleResults.isEmpty());
        
        // Act & Assert per IdSearchStrategy
        List<LibraryItem> idResults = idSearchStrategy.search(mockItems, null);
        assertTrue(idResults.isEmpty());
    }

    @Test
    @DisplayName("Should handle empty items list")
    void testSearchWithEmptyItemsList() {
        // Arrange
        titleSearchStrategy = new TitleSearchStrategy();
        idSearchStrategy = new IdSearchStrategy();
        List<LibraryItem> emptyList = Collections.emptyList();
        
        // Act & Assert
        List<LibraryItem> titleResults = titleSearchStrategy.search(emptyList, "Java");
        assertTrue(titleResults.isEmpty());
        
        List<LibraryItem> idResults = idSearchStrategy.search(emptyList, "978-0134685991");
        assertTrue(idResults.isEmpty());
    }
}
