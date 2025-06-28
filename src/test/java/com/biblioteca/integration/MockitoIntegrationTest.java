package com.biblioteca.integration;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.biblioteca.iterator.LibraryCollection;
import com.biblioteca.model.LibraryItem;
import com.biblioteca.strategy.TitleSearchStrategy;

/**
 * Test di integrazione che dimostrano le capacità di Mockito.
 * Versione semplificata per evitare UnnecessaryStubbing.
 */
@ExtendWith(MockitoExtension.class)
class MockitoIntegrationTest {

    @Mock
    private LibraryItem mockBook;
    
    @Mock
    private LibraryItem mockMagazine;
    
    @Mock
    private LibraryCollection mockCollection;

    @Test
    @DisplayName("Should demonstrate advanced stubbing with thenReturn, thenThrow")
    void testAdvancedStubbing() {
        // Arrange - Stubbing avanzato con comportamenti multipli
        when(mockBook.getTitle())
            .thenReturn("First Call")
            .thenReturn("Second Call")
            .thenThrow(new RuntimeException("Third Call Exception"));
        
        // Act & Assert
        assertEquals("First Call", mockBook.getTitle());
        assertEquals("Second Call", mockBook.getTitle());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> mockBook.getTitle());
        assertEquals("Third Call Exception", exception.getMessage());
        
        // Verify numero di chiamate
        verify(mockBook, times(3)).getTitle();
    }

    @Test
    @DisplayName("Should demonstrate thenAnswer for dynamic responses")
    void testThenAnswerForDynamicResponses() {
        // Arrange - Usa thenAnswer per risposte dinamiche
        var stubbing = when(mockCollection.getItems()).thenAnswer(invocation -> {
            // Simula logica dinamica basata sullo stato
            return Arrays.asList(mockBook, mockMagazine);
        });
        assertNotNull(stubbing); // Usa il valore di ritorno

        // Act
        List<LibraryItem> items = mockCollection.getItems();

        // Assert
        assertEquals(2, items.size());
        assertTrue(items.contains(mockBook));
        assertTrue(items.contains(mockMagazine));

        verify(mockCollection).getItems();
    }

    @Test
    @DisplayName("Should demonstrate verification with times and never")
    void testAdvancedVerification() {
        // Arrange
        when(mockCollection.size()).thenReturn(5);
        
        // Act - simula chiamate multiple
        for (int i = 0; i < 3; i++) {
            mockCollection.size();
        }
        
        // Assert con verification avanzata
        verify(mockCollection, times(3)).size();
        verify(mockCollection, atLeast(2)).size();
        verify(mockCollection, atMost(5)).size();
        
        // Verifica che altri metodi non sono stati chiamati
        verify(mockCollection, never()).addItem(any());
        verifyNoMoreInteractions(mockCollection);
    }

    @Test
    @DisplayName("Should demonstrate InOrder verification for method call sequence")
    void testInOrderVerification() {
        // Arrange
        when(mockBook.getTitle()).thenReturn("Book Title");
        when(mockBook.getId()).thenReturn("123");
        when(mockMagazine.getTitle()).thenReturn("Magazine Title");
        when(mockMagazine.getId()).thenReturn("456");
        
        // Act - chiamate in ordine specifico
        mockBook.getTitle();
        mockBook.getId();
        mockMagazine.getTitle();
        mockMagazine.getId();
        
        // Assert con InOrder
        var inOrder = inOrder(mockBook, mockMagazine);
        inOrder.verify(mockBook).getTitle();
        inOrder.verify(mockBook).getId();
        inOrder.verify(mockMagazine).getTitle();
        inOrder.verify(mockMagazine).getId();
    }

    @Test
    @DisplayName("Should demonstrate spy behavior with partial mocking")
    void testSpyBehaviorWithPartialMocking() {
        // Arrange - spy chiama metodi reali a meno che non siano mockati
        TitleSearchStrategy realStrategy = spy(new TitleSearchStrategy());
        
        // Setup mock items
        when(mockBook.getTitle()).thenReturn("Effective Java");
        when(mockMagazine.getTitle()).thenReturn("Java Magazine");
        
        List<LibraryItem> items = Arrays.asList(mockBook, mockMagazine);
        
        // Mock solo comportamenti specifici
        doReturn(Arrays.asList(mockBook)).when(realStrategy).search(eq(items), eq("Effective"));
        
        // Act
        List<LibraryItem> mockedResult = realStrategy.search(items, "Effective");
        List<LibraryItem> realResult = realStrategy.search(items, "Java");
        
        // Assert
        assertEquals(1, mockedResult.size()); // Comportamento mockato
        assertEquals(2, realResult.size()); // Comportamento reale
        
        // Verify chiamate
        verify(realStrategy, times(2)).search(any(), anyString());
    }

    @Test
    @DisplayName("Should demonstrate exception handling with doThrow")
    void testExceptionHandlingWithDoThrow() {
        // Arrange
        var stubbing = doThrow(new RuntimeException("Simulated error"))
            .when(mockCollection);
        stubbing.addItem(any());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            mockCollection.addItem(mockBook);
        });
        assertEquals("Simulated error", exception.getMessage());
        
        // Verify che il metodo è stato chiamato nonostante l'eccezione
        verify(mockCollection).addItem(mockBook);
    }

    @Test
    @DisplayName("Should demonstrate reset and clearInvocations")
    void testResetAndClearInvocations() {
        // Arrange & Act
        when(mockBook.getTitle()).thenReturn("Test Title");
        when(mockBook.getId()).thenReturn("Test ID");
        
        mockBook.getTitle();
        mockBook.getId();
        
        // Verify chiamate iniziali
        verify(mockBook).getTitle();
        verify(mockBook).getId();
        
        // Reset delle invocazioni
        clearInvocations(mockBook);
        
        // Nuove chiamate
        when(mockBook.getType()).thenReturn("Book");
        mockBook.getType();
        
        // Assert - solo le chiamate dopo clearInvocations sono visibili
        verify(mockBook).getType();
        verify(mockBook, never()).getTitle(); // Non più visibile dopo clear
    }

    @Test
    @DisplayName("Should demonstrate testing with mocked collection operations")
    void testMockedCollectionOperations() {
        // Arrange
        when(mockCollection.size()).thenReturn(2);
        
        // Act
        int size = mockCollection.size();
        
        // Assert
        assertEquals(2, size);
        verify(mockCollection).size();
    }
}
