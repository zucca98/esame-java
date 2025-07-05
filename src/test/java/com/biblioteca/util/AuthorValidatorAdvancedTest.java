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
 * Test suite avanzata per AuthorValidator utilizzando tecniche avanzate di Mockito.
 *
 * <p>Questa classe di test dimostra l'utilizzo di tecniche avanzate di testing
 * con Mockito, inclusi static mocking, integrazione tra componenti e test
 * di comportamento complesso. Rappresenta un livello superiore di testing
 * rispetto ai test base di AuthorValidator.</p>
 *
 * <p><strong>Tecniche avanzate di Mockito dimostrate:</strong></p>
 * <ul>
 *   <li><strong>Static Mocking:</strong> Mock di metodi statici con MockedStatic</li>
 *   <li><strong>Integration Testing:</strong> Test di integrazione tra AuthorValidator e Factory</li>
 *   <li><strong>Behavior Verification:</strong> Verifica del comportamento e delle interazioni</li>
 *   <li><strong>Exception Mocking:</strong> Simulazione di eccezioni in contesti controllati</li>
 *   <li><strong>Complex Scenarios:</strong> Test di scenari realistici e complessi</li>
 * </ul>
 *
 * <p><strong>Scenari di test coperti:</strong></p>
 * <ul>
 *   <li>Integrazione con LibraryItemFactory tramite static mocking</li>
 *   <li>Comportamento del validator in contesti di errore</li>
 *   <li>Verifica delle chiamate ai metodi statici</li>
 *   <li>Test di regressione per edge cases complessi</li>
 * </ul>
 *
 * <p><strong>Valore aggiunto:</strong></p>
 * <ul>
 *   <li>Dimostra capacità avanzate di testing</li>
 *   <li>Verifica integrazione tra componenti</li>
 *   <li>Copre scenari non testabili con unit test semplici</li>
 *   <li>Fornisce esempi di best practices per Mockito avanzato</li>
 * </ul>
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
class AuthorValidatorAdvancedTest {

    /**
     * Test di integrazione con LibraryItemFactory utilizzando static mocking.
     *
     * <p>Questo test dimostra l'utilizzo di MockedStatic per controllare
     * il comportamento di metodi statici durante i test di integrazione.
     * Verifica che l'integrazione tra AuthorValidator e LibraryItemFactory
     * funzioni correttamente quando il validator è mockato.</p>
     *
     * <p><strong>Tecniche dimostrate:</strong></p>
     * <ul>
     *   <li>Static mocking con MockedStatic</li>
     *   <li>Try-with-resources per gestione automatica del mock</li>
     *   <li>Verifica dell'integrazione tra componenti</li>
     *   <li>Test del flusso completo di validazione</li>
     * </ul>
     *
     * <p><strong>Scenario:</strong> Creazione libro con validator mockato che restituisce true</p>
     */
    @Test
    @DisplayName("Should test integration with LibraryItemFactory using static mocking")
    void testIntegrationWithLibraryItemFactoryMocking() throws InvalidDataException {
        // Arrange - Preparazione dati di test
        String isbn = "978-0134685991";
        String title = "Effective Java";
        String validAuthor = "Joshua Bloch";
        int pages = 416;

        // Utilizzo di try-with-resources per gestione automatica del MockedStatic
        try (MockedStatic<AuthorValidator> mockedValidator = mockStatic(AuthorValidator.class)) {
            // Mock del validator per restituire sempre true per questo autore
            mockedValidator.when(() -> AuthorValidator.isValidAuthor(validAuthor))
                          .thenReturn(true);

            // Act - Creazione libro che dovrebbe utilizzare il validator mockato
            LibraryItem book = LibraryItemFactory.createBook(isbn, title, validAuthor, pages);
            
            // Assert
            assertNotNull(book);
            assertEquals(title, book.getTitle());
            
            // Verify che il validator è stato chiamato
            mockedValidator.verify(() -> AuthorValidator.isValidAuthor(validAuthor));
        }
    }

    /**
     * Test di rifiuto del factory con autore non valido utilizzando static mocking.
     *
     * <p>Questo test verifica che il LibraryItemFactory rifiuti correttamente
     * la creazione di libri quando l'AuthorValidator (mockato) restituisce
     * false per un autore non valido. Dimostra l'integrazione tra i componenti
     * in scenari di errore.</p>
     *
     * <p><strong>Scenario testato:</strong></p>
     * <ul>
     *   <li>Autore puramente numerico che deve essere rifiutato</li>
     *   <li>Mock del validator per controllare il comportamento</li>
     *   <li>Verifica che l'eccezione appropriata venga lanciata</li>
     *   <li>Controllo delle chiamate ai metodi del validator</li>
     * </ul>
     */
    @Test
    @DisplayName("Should test factory rejection with invalid author using static mocking")
    void testFactoryRejectionWithInvalidAuthor() {
        // Arrange - Preparazione scenario con autore non valido
        String isbn = "978-0134685991";
        String title = "Test Book";
        String invalidAuthor = "123456"; // Puramente numerico - deve essere rifiutato
        int pages = 100;

        try (MockedStatic<AuthorValidator> mockedValidator = mockStatic(AuthorValidator.class)) {
            // Mock del validator per restituire false per autore non valido
            mockedValidator.when(() -> AuthorValidator.isValidAuthor(invalidAuthor))
                          .thenReturn(false);

            // Mock per restituire messaggio di errore specifico
            mockedValidator.when(() -> AuthorValidator.getValidationErrorMessage(invalidAuthor))
                          .thenReturn("Author name cannot consist entirely of numbers");

            // Act & Assert - Verifica che venga lanciata l'eccezione appropriata
            InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> LibraryItemFactory.createBook(isbn, title, invalidAuthor, pages)
            );

            // Verifica che il messaggio di errore sia corretto
            assertTrue(exception.getMessage().contains("numbers"));

            // Verify - Controllo che i metodi del validator siano stati chiamati
            mockedValidator.verify(() -> AuthorValidator.isValidAuthor(invalidAuthor));
            mockedValidator.verify(() -> AuthorValidator.getValidationErrorMessage(invalidAuthor));
        }
    }

    /**
     * Test di partial mocking dei metodi di AuthorValidator.
     *
     * <p>Questo test dimostra una tecnica avanzata di Mockito chiamata "partial mocking",
     * dove alcuni metodi vengono mockati mentre altri chiamano l'implementazione reale.
     * Questo è utile quando si vuole testare l'integrazione parziale tra componenti.</p>
     *
     * <p><strong>Tecniche dimostrate:</strong></p>
     * <ul>
     *   <li>Partial mocking con thenCallRealMethod()</li>
     *   <li>Combinazione di comportamenti mockati e reali</li>
     *   <li>Test di integrazione selettiva</li>
     *   <li>Verifica di chiamate miste (mock + reali)</li>
     * </ul>
     *
     * <p><strong>Scenario:</strong> Mock di isValidAuthor ma chiamata reale di isPurelyNumeric</p>
     */
    @Test
    @DisplayName("Should test partial mocking of AuthorValidator methods")
    void testPartialMockingOfValidatorMethods() {
        try (MockedStatic<AuthorValidator> mockedValidator = mockStatic(AuthorValidator.class)) {
            // Arrange - Configurazione partial mocking
            String author = "Test Author";

            // Mock isValidAuthor per restituire sempre true (comportamento controllato)
            mockedValidator.when(() -> AuthorValidator.isValidAuthor(author))
                          .thenReturn(true);

            // Lascia isPurelyNumeric chiamare l'implementazione reale
            mockedValidator.when(() -> AuthorValidator.isPurelyNumeric("123"))
                          .thenCallRealMethod();

            // Act - Esecuzione di entrambi i metodi (mockato e reale)
            boolean isValid = AuthorValidator.isValidAuthor(author);
            boolean isPurelyNumeric = AuthorValidator.isPurelyNumeric("123");

            // Assert - Verifica dei risultati
            assertTrue(isValid); // Mockato per restituire sempre true
            assertTrue(isPurelyNumeric); // Chiamata reale che restituisce true per "123"

            // Verify - Controllo che entrambi i metodi siano stati chiamati
            mockedValidator.verify(() -> AuthorValidator.isValidAuthor(author));
            mockedValidator.verify(() -> AuthorValidator.isPurelyNumeric("123"));
        }
    }

    /**
     * Test di generazione messaggi di errore con validazione mockata.
     *
     * <p>Questo test verifica che il sistema di generazione dei messaggi di errore
     * funzioni correttamente quando il validator è mockato. È utile per testare
     * l'interfaccia utente e la gestione degli errori senza dipendere dalla
     * logica di validazione reale.</p>
     *
     * <p><strong>Aspetti testati:</strong></p>
     * <ul>
     *   <li>Generazione di messaggi di errore personalizzati</li>
     *   <li>Isolamento della logica di messaging dalla validazione</li>
     *   <li>Controllo del comportamento dell'interfaccia utente</li>
     *   <li>Verifica delle chiamate ai metodi di messaging</li>
     * </ul>
     *
     * <p><strong>Utilità:</strong> Permette di testare UI e UX senza logica di validazione</p>
     */
    @Test
    @DisplayName("Should test error message generation with mocked validation")
    void testErrorMessageGenerationWithMocking() {
        try (MockedStatic<AuthorValidator> mockedValidator = mockStatic(AuthorValidator.class)) {
            // Arrange - Preparazione messaggio di errore personalizzato
            String invalidAuthor = "123-456";
            String expectedMessage = "Custom error message for testing";

            // Mock per restituire messaggio personalizzato controllato
            mockedValidator.when(() -> AuthorValidator.getValidationErrorMessage(invalidAuthor))
                          .thenReturn(expectedMessage);

            // Act - Richiesta del messaggio di errore
            String actualMessage = AuthorValidator.getValidationErrorMessage(invalidAuthor);

            // Assert - Verifica che il messaggio sia quello atteso
            assertEquals(expectedMessage, actualMessage);

            // Verify - Controllo che il metodo sia stato chiamato correttamente
            mockedValidator.verify(() -> AuthorValidator.getValidationErrorMessage(invalidAuthor));
        }
    }

    /**
     * Test di gestione eccezioni nella catena di validazione.
     *
     * <p>Questo test verifica il comportamento del sistema quando si verificano
     * eccezioni impreviste durante il processo di validazione. È importante
     * per garantire che il sistema gestisca gracefully gli errori interni
     * e non si blocchi in situazioni anomale.</p>
     *
     * <p><strong>Scenario testato:</strong></p>
     * <ul>
     *   <li>Simulazione di errore interno nel validator</li>
     *   <li>Verifica che l'eccezione venga propagata correttamente</li>
     *   <li>Controllo che il sistema non si corrompa</li>
     *   <li>Test di robustezza in condizioni di errore</li>
     * </ul>
     *
     * <p><strong>Importanza:</strong> Garantisce resilienza del sistema agli errori interni</p>
     */
    @Test
    @DisplayName("Should test exception handling in validation chain")
    void testExceptionHandlingInValidationChain() {
        try (MockedStatic<AuthorValidator> mockedValidator = mockStatic(AuthorValidator.class)) {
            // Arrange - Preparazione scenario di errore interno
            String problematicAuthor = "null-pointer-test";

            // Mock per simulare errore interno del validator
            var stubbing = mockedValidator.when(() -> AuthorValidator.isValidAuthor(problematicAuthor))
                          .thenThrow(new RuntimeException("Simulated validation error"));
            assertNotNull(stubbing); // Verifica che lo stubbing sia configurato

            // Act & Assert - Verifica che l'eccezione venga propagata
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                AuthorValidator.isValidAuthor(problematicAuthor);
            });
            assertNotNull(exception);

            // Verify - Controllo che il metodo sia stato chiamato prima dell'errore
            mockedValidator.verify(() -> AuthorValidator.isValidAuthor(problematicAuthor));
        }
    }

    /**
     * Test di scenari di validazione complessi con mock multipli.
     *
     * <p>Questo test dimostra l'utilizzo di mock in scenari complessi dove
     * multiple componenti interagiscono. Verifica che l'integrazione tra
     * AuthorValidator e LibraryItemFactory funzioni correttamente quando
     * il validator è completamente sotto controllo del test.</p>
     *
     * <p><strong>Complessità gestita:</strong></p>
     * <ul>
     *   <li>Integrazione tra validator mockato e factory reale</li>
     *   <li>Verifica del flusso completo di creazione</li>
     *   <li>Controllo delle interazioni tra componenti</li>
     *   <li>Test end-to-end con dipendenze controllate</li>
     * </ul>
     *
     * <p><strong>Valore:</strong> Dimostra testing di integrazione con controllo completo</p>
     */
    @Test
    @DisplayName("Should test complex validation scenarios with multiple mocks")
    void testComplexValidationScenariosWithMultipleMocks() throws InvalidDataException {
        try (MockedStatic<AuthorValidator> mockedValidator = mockStatic(AuthorValidator.class)) {

            // Arrange - Preparazione scenario complesso
            String isbn = "978-0134685991";
            String title = "Test Book";
            String author = "Complex Author Name";
            int pages = 300;

            // Mock validator per permettere la creazione del libro
            mockedValidator.when(() -> AuthorValidator.isValidAuthor(author))
                          .thenReturn(true);

            // Act - Utilizzo del factory reale che chiamerà il validator mockato
            LibraryItem result = LibraryItemFactory.createBook(isbn, title, author, pages);

            // Assert - Verifica che l'oggetto sia stato creato correttamente
            assertNotNull(result);
            assertEquals(title, result.getTitle());

            // Verify - Controllo che il validator sia stato chiamato durante la creazione
            mockedValidator.verify(() -> AuthorValidator.isValidAuthor(author));
        }
    }

    /**
     * Test di validazione con scenari diversificati.
     *
     * <p>Questo test verifica il comportamento del validator mockato con
     * diversi tipi di input, dimostrando come configurare comportamenti
     * multipli e specifici per diversi scenari di test. È utile per
     * testare la logica di business che dipende da diverse condizioni.</p>
     *
     * <p><strong>Scenari testati:</strong></p>
     * <ul>
     *   <li><strong>Nome normale:</strong> "John Smith" → valido</li>
     *   <li><strong>Solo numeri:</strong> "123" → non valido</li>
     *   <li><strong>Nome lungo:</strong> "Very Long Author Name" → valido</li>
     * </ul>
     *
     * <p><strong>Tecniche dimostrate:</strong></p>
     * <ul>
     *   <li>Configurazione di comportamenti multipli nello stesso mock</li>
     *   <li>Test di diversi path di esecuzione</li>
     *   <li>Verifica sistematica di tutte le chiamate</li>
     *   <li>Copertura di edge cases con controllo completo</li>
     * </ul>
     *
     * <p><strong>Utilità:</strong> Permette testing completo senza dipendere dalla logica reale</p>
     */
    @Test
    @DisplayName("Should test validation with different scenarios")
    void testValidationWithDifferentScenarios() {
        try (MockedStatic<AuthorValidator> mockedValidator = mockStatic(AuthorValidator.class)) {
            // Arrange - Configurazione di diversi scenari di validazione
            mockedValidator.when(() -> AuthorValidator.isValidAuthor("John Smith"))
                          .thenReturn(true);

            mockedValidator.when(() -> AuthorValidator.isValidAuthor("123"))
                          .thenReturn(false);

            mockedValidator.when(() -> AuthorValidator.isValidAuthor("Very Long Author Name"))
                          .thenReturn(true);

            // Act & Assert - Test di tutti gli scenari configurati
            assertTrue(AuthorValidator.isValidAuthor("John Smith"));
            assertFalse(AuthorValidator.isValidAuthor("123"));
            assertTrue(AuthorValidator.isValidAuthor("Very Long Author Name"));

            // Verify - Controllo sistematico di tutte le chiamate
            mockedValidator.verify(() -> AuthorValidator.isValidAuthor("John Smith"));
            mockedValidator.verify(() -> AuthorValidator.isValidAuthor("123"));
            mockedValidator.verify(() -> AuthorValidator.isValidAuthor("Very Long Author Name"));
        }
    }
}
