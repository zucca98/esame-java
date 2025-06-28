package com.biblioteca.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mockStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.biblioteca.exceptions.InvalidDataException;
import com.biblioteca.factory.LibraryItemFactory;
import com.biblioteca.model.LibraryItem;

/**
 * Test avanzati per AuthorValidator utilizzando Mockito.
 * Dimostra static mocking e integrazione con LibraryItemFactory.
 */
@ExtendWith(MockitoExtension.class)
class AuthorValidatorAdvancedTest {

    @Test
    @DisplayName("Should test integration with LibraryItemFactory using static mocking")
    void testIntegrationWithLibraryItemFactoryMocking() throws InvalidDataException {
        // Arrange
        String isbn = "978-0134685991";
        String title = "Effective Java";
        String validAuthor = "Joshua Bloch";
        int pages = 416;
        
        try (MockedStatic<AuthorValidator> mockedValidator = mockStatic(AuthorValidator.class)) {
            // Mock del validator per restituire true
            mockedValidator.when(() -> AuthorValidator.isValidAuthor(validAuthor))
                          .thenReturn(true);
            
            // Act - questo dovrebbe chiamare il validator mockato
            LibraryItem book = LibraryItemFactory.createBook(isbn, title, validAuthor, pages);
            
            // Assert
            assertNotNull(book);
            assertEquals(title, book.getTitle());
            
            // Verify che il validator è stato chiamato
            mockedValidator.verify(() -> AuthorValidator.isValidAuthor(validAuthor));
        }
    }

    @Test
    @DisplayName("Should test factory rejection with invalid author using static mocking")
    void testFactoryRejectionWithInvalidAuthor() {
        // Arrange
        String isbn = "978-0134685991";
        String title = "Test Book";
        String invalidAuthor = "123456"; // Puramente numerico
        int pages = 100;
        
        try (MockedStatic<AuthorValidator> mockedValidator = mockStatic(AuthorValidator.class)) {
            // Mock del validator per restituire false per autore invalido
            mockedValidator.when(() -> AuthorValidator.isValidAuthor(invalidAuthor))
                          .thenReturn(false);
            
            // Mock per restituire messaggio di errore
            mockedValidator.when(() -> AuthorValidator.getValidationErrorMessage(invalidAuthor))
                          .thenReturn("Author name cannot consist entirely of numbers");
            
            // Act & Assert
            InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> LibraryItemFactory.createBook(isbn, title, invalidAuthor, pages)
            );
            
            assertTrue(exception.getMessage().contains("numbers"));
            
            // Verify le chiamate al validator
            mockedValidator.verify(() -> AuthorValidator.isValidAuthor(invalidAuthor));
            mockedValidator.verify(() -> AuthorValidator.getValidationErrorMessage(invalidAuthor));
        }
    }

    @Test
    @DisplayName("Should test partial mocking of AuthorValidator methods")
    void testPartialMockingOfValidatorMethods() {
        try (MockedStatic<AuthorValidator> mockedValidator = mockStatic(AuthorValidator.class)) {
            // Arrange - mock solo alcuni metodi, altri chiamano implementazione reale
            String author = "Test Author";
            
            // Mock isValidAuthor per restituire sempre true
            mockedValidator.when(() -> AuthorValidator.isValidAuthor(author))
                          .thenReturn(true);
            
            // Lascia isPurelyNumeric chiamare l'implementazione reale
            mockedValidator.when(() -> AuthorValidator.isPurelyNumeric("123"))
                          .thenCallRealMethod();
            
            // Act
            boolean isValid = AuthorValidator.isValidAuthor(author);
            boolean isPurelyNumeric = AuthorValidator.isPurelyNumeric("123");
            
            // Assert
            assertTrue(isValid); // Mockato per restituire true
            assertTrue(isPurelyNumeric); // Chiamata reale
            
            // Verify
            mockedValidator.verify(() -> AuthorValidator.isValidAuthor(author));
            mockedValidator.verify(() -> AuthorValidator.isPurelyNumeric("123"));
        }
    }

    @Test
    @DisplayName("Should test error message generation with mocked validation")
    void testErrorMessageGenerationWithMocking() {
        try (MockedStatic<AuthorValidator> mockedValidator = mockStatic(AuthorValidator.class)) {
            // Arrange
            String invalidAuthor = "123-456";
            String expectedMessage = "Custom error message for testing";
            
            // Mock per restituire messaggio personalizzato
            mockedValidator.when(() -> AuthorValidator.getValidationErrorMessage(invalidAuthor))
                          .thenReturn(expectedMessage);
            
            // Act
            String actualMessage = AuthorValidator.getValidationErrorMessage(invalidAuthor);
            
            // Assert
            assertEquals(expectedMessage, actualMessage);
            
            // Verify
            mockedValidator.verify(() -> AuthorValidator.getValidationErrorMessage(invalidAuthor));
        }
    }

    @Test
    @DisplayName("Should test exception handling in validation chain")
    void testExceptionHandlingInValidationChain() {
        try (MockedStatic<AuthorValidator> mockedValidator = mockStatic(AuthorValidator.class)) {
            // Arrange
            String problematicAuthor = "null-pointer-test";
            
            // Mock per lanciare eccezione
            var stubbing = mockedValidator.when(() -> AuthorValidator.isValidAuthor(problematicAuthor))
                          .thenThrow(new RuntimeException("Simulated validation error"));
            assertNotNull(stubbing); // Usa il valore di ritorno
            
            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                AuthorValidator.isValidAuthor(problematicAuthor);
            });
            assertNotNull(exception);
            
            // Verify che il metodo è stato chiamato
            mockedValidator.verify(() -> AuthorValidator.isValidAuthor(problematicAuthor));
        }
    }

    @Test
    @DisplayName("Should test complex validation scenarios with multiple mocks")
    void testComplexValidationScenariosWithMultipleMocks() throws InvalidDataException {
        try (MockedStatic<AuthorValidator> mockedValidator = mockStatic(AuthorValidator.class)) {

            // Arrange
            String isbn = "978-0134685991";
            String title = "Test Book";
            String author = "Complex Author Name";
            int pages = 300;

            // Mock validator per permettere la creazione del libro
            mockedValidator.when(() -> AuthorValidator.isValidAuthor(author))
                          .thenReturn(true);

            // Act - usa il factory reale che chiamerà il validator mockato
            LibraryItem result = LibraryItemFactory.createBook(isbn, title, author, pages);

            // Assert
            assertNotNull(result);
            assertEquals(title, result.getTitle());

            // Verify che il validator è stato chiamato
            mockedValidator.verify(() -> AuthorValidator.isValidAuthor(author));
        }
    }

    @Test
    @DisplayName("Should test validation with different scenarios")
    void testValidationWithDifferentScenarios() {
        try (MockedStatic<AuthorValidator> mockedValidator = mockStatic(AuthorValidator.class)) {
            // Arrange - diversi scenari di validazione
            mockedValidator.when(() -> AuthorValidator.isValidAuthor("John Smith"))
                          .thenReturn(true);
            
            mockedValidator.when(() -> AuthorValidator.isValidAuthor("123"))
                          .thenReturn(false);
            
            mockedValidator.when(() -> AuthorValidator.isValidAuthor("Very Long Author Name"))
                          .thenReturn(true);
            
            // Act & Assert
            assertTrue(AuthorValidator.isValidAuthor("John Smith"));
            assertFalse(AuthorValidator.isValidAuthor("123"));
            assertTrue(AuthorValidator.isValidAuthor("Very Long Author Name"));
            
            // Verify tutte le chiamate
            mockedValidator.verify(() -> AuthorValidator.isValidAuthor("John Smith"));
            mockedValidator.verify(() -> AuthorValidator.isValidAuthor("123"));
            mockedValidator.verify(() -> AuthorValidator.isValidAuthor("Very Long Author Name"));
        }
    }
}
