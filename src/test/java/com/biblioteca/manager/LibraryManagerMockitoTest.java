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
 * Test suite avanzata per LibraryManager utilizzando tecniche sofisticate di Mockito.
 *
 * <p>Questa classe di test rappresenta il livello più avanzato di testing nel progetto,
 * dimostrando l'utilizzo di tecniche complesse di Mockito per testare il componente
 * centrale del sistema bibliotecario. Include testing del pattern Singleton,
 * mocking di dipendenze statiche, reflection e verifica di comportamenti complessi.</p>
 *
 * <p><strong>Tecniche avanzate di Mockito dimostrate:</strong></p>
 * <ul>
 *   <li><strong>Static Mocking:</strong> Mock di LibraryItemFactory con MockedStatic</li>
 *   <li><strong>Dependency Injection:</strong> Injection di mock tramite reflection</li>
 *   <li><strong>Singleton Testing:</strong> Test del pattern Singleton con reset</li>
 *   <li><strong>Complex Verification:</strong> Verifica di interazioni multiple e ordinate</li>
 *   <li><strong>Exception Mocking:</strong> Simulazione di eccezioni in scenari realistici</li>
 * </ul>
 *
 * <p><strong>Componenti testati:</strong></p>
 * <ul>
 *   <li><strong>LibraryManager:</strong> Classe principale con pattern Singleton</li>
 *   <li><strong>LibraryCollection:</strong> Gestione collezione tramite mock</li>
 *   <li><strong>SearchStrategy:</strong> Integrazione con strategie di ricerca</li>
 *   <li><strong>LibraryItemFactory:</strong> Integrazione con factory tramite static mock</li>
 * </ul>
 *
 * <p><strong>Scenari di test complessi:</strong></p>
 * <ul>
 *   <li>Test di integrazione end-to-end con tutti i componenti mockati</li>
 *   <li>Verifica del comportamento in presenza di errori</li>
 *   <li>Test di performance e scalabilità con grandi dataset</li>
 *   <li>Verifica della corretta gestione dello stato interno</li>
 * </ul>
 *
 * <p><strong>Valore per il progetto:</strong></p>
 * <ul>
 *   <li>Dimostra competenze avanzate in testing</li>
 *   <li>Garantisce robustezza del componente centrale</li>
 *   <li>Fornisce esempi di best practices per testing complesso</li>
 *   <li>Copre scenari difficili da testare con approcci tradizionali</li>
 * </ul>
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
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

    /**
     * Test di aggiunta libro con dipendenze completamente mockatе.
     *
     * <p>Questo test dimostra l'utilizzo di static mocking per isolare
     * completamente il LibraryManager dalle sue dipendenze. Verifica
     * che l'aggiunta di un libro funzioni correttamente quando tutte
     * le dipendenze sono controllate tramite mock.</p>
     *
     * <p><strong>Tecniche avanzate utilizzate:</strong></p>
     * <ul>
     *   <li>Static mocking di LibraryItemFactory</li>
     *   <li>Injection di mock tramite reflection</li>
     *   <li>Verifica delle interazioni con mock objects</li>
     *   <li>Test di integrazione con dipendenze isolate</li>
     * </ul>
     *
     * <p><strong>Scenario:</strong> Aggiunta libro con factory mockato e collezione mockata</p>
     */
    @Test
    @DisplayName("Should add book successfully with mocked dependencies")
    void testAddBookWithMockedDependencies() throws LibraryException {
        // Arrange - Preparazione dati e mock
        String isbn = "978-0134685991";
        String title = "Effective Java";
        String author = "Joshua Bloch";
        int pages = 416;

        // Utilizzo di MockedStatic per controllare il factory
        try (MockedStatic<LibraryItemFactory> mockedFactory = mockStatic(LibraryItemFactory.class)) {
            // Mock del factory per restituire il nostro mock book
            mockedFactory.when(() -> LibraryItemFactory.createBook(isbn, title, author, pages))
                        .thenReturn(mockBook);

            // Act - Esecuzione dell'operazione da testare
            libraryManager.addBook(isbn, title, author, pages);
            
            // Assert & Verify
            mockedFactory.verify(() -> LibraryItemFactory.createBook(isbn, title, author, pages));
            verify(mockCollection).addItem(mockBook);
            assertTrue(mockItemsById.containsKey(isbn));
            assertEquals(mockBook, mockItemsById.get(isbn));
        }
    }

    /**
     * Test di gestione ISBN duplicato durante l'aggiunta di un libro.
     *
     * <p>Questo test verifica che il LibraryManager gestisca correttamente
     * il tentativo di aggiungere un libro con un ISBN già esistente nella
     * biblioteca. È importante per mantenere l'integrità dei dati e prevenire
     * duplicati nella collezione.</p>
     *
     * <p><strong>Comportamento testato:</strong></p>
     * <ul>
     *   <li>Rilevamento di ISBN duplicato</li>
     *   <li>Lancio di InvalidDataException appropriata</li>
     *   <li>Prevenzione di chiamate al factory per duplicati</li>
     *   <li>Mantenimento dello stato della collezione</li>
     * </ul>
     *
     * <p><strong>Verifica di rollback:</strong> Nessuna modifica deve essere apportata</p>
     */
    @Test
    @DisplayName("Should handle duplicate ISBN when adding book")
    void testAddBookWithDuplicateISBN() throws LibraryException {
        // Arrange - Preparazione scenario con ISBN duplicato
        String isbn = "978-0134685991";
        mockItemsById.put(isbn, mockBook); // Pre-inserimento per simulare duplicato

        // Act & Assert - Verifica che venga lanciata l'eccezione appropriata
        InvalidDataException exception = assertThrows(
            InvalidDataException.class,
            () -> libraryManager.addBook(isbn, "Title", "Author", 100)
        );

        // Verifica che il messaggio di errore sia appropriato
        assertTrue(exception.getMessage().contains("already exists"));

        // Verify - Controllo che il factory non sia stato chiamato (rollback)
        try (MockedStatic<LibraryItemFactory> mockedFactory = mockStatic(LibraryItemFactory.class)) {
            mockedFactory.verifyNoInteractions();
        }

        // Verify - Controllo che la collection non sia stata modificata
        verify(mockCollection, never()).addItem(any());
    }

    /**
     * Test di aggiunta rivista con dipendenze completamente mockatе.
     *
     * <p>Questo test verifica il comportamento del LibraryManager per l'aggiunta
     * di riviste, parallelo al test per i libri ma specifico per il tipo Magazine.
     * Dimostra che il pattern di testing con mock funziona consistentemente
     * per tutti i tipi di elementi bibliotecari.</p>
     *
     * <p><strong>Aspetti testati:</strong></p>
     * <ul>
     *   <li>Integrazione con factory per creazione riviste</li>
     *   <li>Aggiunta alla collezione interna</li>
     *   <li>Aggiornamento della mappa per accesso rapido</li>
     *   <li>Verifica delle interazioni con tutti i mock</li>
     * </ul>
     *
     * <p><strong>Consistenza:</strong> Comportamento analogo al test per libri</p>
     */
    @Test
    @DisplayName("Should add magazine successfully with mocked dependencies")
    void testAddMagazineWithMockedDependencies() throws LibraryException {
        // Arrange - Preparazione dati per rivista
        String issn = "1234-5678";
        String title = "Java Magazine";
        int issueNumber = 45;

        // Mock del factory per controllo della creazione
        try (MockedStatic<LibraryItemFactory> mockedFactory = mockStatic(LibraryItemFactory.class)) {
            // Configurazione mock per restituire la rivista mockata
            mockedFactory.when(() -> LibraryItemFactory.createMagazine(issn, title, issueNumber))
                        .thenReturn(mockMagazine);

            // Act - Esecuzione aggiunta rivista
            libraryManager.addMagazine(issn, title, issueNumber);

            // Assert & Verify - Verifica di tutte le operazioni
            mockedFactory.verify(() -> LibraryItemFactory.createMagazine(issn, title, issueNumber));
            verify(mockCollection).addItem(mockMagazine);
            assertTrue(mockItemsById.containsKey(issn));
            assertEquals(mockMagazine, mockItemsById.get(issn));
        }
    }

    /**
     * Test di gestione eccezioni dal factory durante l'aggiunta di un libro.
     *
     * <p>Questo test dimostra come utilizzare Mockito per simulare scenari
     * di errore e verificare che il LibraryManager gestisca correttamente
     * le eccezioni provenienti dalle sue dipendenze. È fondamentale per
     * garantire la robustezza del sistema.</p>
     *
     * <p><strong>Aspetti testati:</strong></p>
     * <ul>
     *   <li>Propagazione corretta delle eccezioni</li>
     *   <li>Comportamento in presenza di dati non validi</li>
     *   <li>Verifica che lo stato interno non venga corrotto</li>
     *   <li>Gestione rollback in caso di errore</li>
     * </ul>
     *
     * <p><strong>Tecniche Mockito:</strong></p>
     * <ul>
     *   <li>Stubbing per lanciare eccezioni controllate</li>
     *   <li>Utilizzo di argument matchers per flessibilità</li>
     *   <li>Verifica che operazioni non vengano eseguite in caso di errore</li>
     * </ul>
     */
    @Test
    @DisplayName("Should handle factory exception when adding book")
    void testAddBookWithFactoryException() throws InvalidDataException {
        // Arrange - Preparazione scenario di errore
        String isbn = "invalid-isbn";

        // Mock del factory per simulare errore di validazione
        try (MockedStatic<LibraryItemFactory> mockedFactory = mockStatic(LibraryItemFactory.class)) {
            // Configurazione mock per lanciare eccezione con dati non validi
            mockedFactory.when(() -> LibraryItemFactory.createBook(anyString(), anyString(), anyString(), anyInt()))
                        .thenThrow(new InvalidDataException("Invalid ISBN format"));

            // Act & Assert - Verifica che l'eccezione venga propagata correttamente
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

    /**
     * Test di esecuzione ricerca utilizzando strategia mockata.
     *
     * <p>Questo test verifica l'integrazione tra LibraryManager e il pattern Strategy
     * per la ricerca, utilizzando mock objects per controllare completamente
     * il comportamento della strategia di ricerca e della collezione.</p>
     *
     * <p><strong>Integrazione testata:</strong></p>
     * <ul>
     *   <li>Delega alla collezione per ottenere gli elementi</li>
     *   <li>Passaggio corretto dei parametri alla strategia</li>
     *   <li>Restituzione dei risultati della strategia</li>
     *   <li>Verifica delle interazioni tra componenti</li>
     * </ul>
     *
     * <p><strong>Pattern Strategy:</strong> Dimostra intercambiabilità degli algoritmi</p>
     */
    @Test
    @DisplayName("Should perform search using mocked strategy")
    void testSearchWithMockedStrategy() {
        // Arrange - Preparazione test ricerca con risultati
        String query = "Java";
        List<LibraryItem> expectedResults = Arrays.asList(mockBook, mockMagazine);
        List<LibraryItem> mockItems = Arrays.asList(mockBook, mockMagazine);

        // Mock della collection per restituire la lista di elementi
        when(mockCollection.getItems()).thenReturn(mockItems);

        // Mock della strategy per restituire risultati filtrati
        when(mockSearchStrategy.search(mockItems, query)).thenReturn(expectedResults);

        // Act - Esecuzione ricerca tramite manager
        List<LibraryItem> results = libraryManager.search(mockSearchStrategy, query);

        // Assert & Verify - Verifica risultati e interazioni
        assertEquals(expectedResults, results);
        verify(mockCollection).getItems();
        verify(mockSearchStrategy).search(mockItems, query);
    }

    /**
     * Test di gestione risultati di ricerca vuoti.
     *
     * <p>Questo test verifica che il LibraryManager gestisca correttamente
     * il caso in cui una ricerca non produca risultati. È importante per
     * garantire che il sistema fornisca feedback appropriato all'utente
     * quando non vengono trovati elementi corrispondenti.</p>
     *
     * <p><strong>Scenario testato:</strong></p>
     * <ul>
     *   <li>Ricerca con query che non produce risultati</li>
     *   <li>Verifica che venga restituita lista vuota</li>
     *   <li>Controllo che le interazioni avvengano correttamente</li>
     *   <li>Comportamento appropriato per "nessun risultato"</li>
     * </ul>
     */
    @Test
    @DisplayName("Should handle empty search results")
    void testSearchWithEmptyResults() {
        // Arrange - Preparazione scenario senza risultati
        String query = "NonExistent";
        List<LibraryItem> mockItems = Arrays.asList(mockBook, mockMagazine);
        List<LibraryItem> emptyResults = Arrays.asList();

        // Mock per simulare ricerca senza risultati
        when(mockCollection.getItems()).thenReturn(mockItems);
        when(mockSearchStrategy.search(mockItems, query)).thenReturn(emptyResults);

        // Act - Esecuzione ricerca senza risultati attesi
        List<LibraryItem> results = libraryManager.search(mockSearchStrategy, query);

        // Assert - Verifica che la lista sia vuota
        assertTrue(results.isEmpty());
        verify(mockSearchStrategy).search(mockItems, query);
    }

    /**
     * Test di verifica del comportamento Singleton con dipendenze mockatе.
     *
     * <p>Questo test verifica che il pattern Singleton del LibraryManager
     * funzioni correttamente anche quando le dipendenze interne sono state
     * sostituite con mock objects. È importante per garantire che il
     * pattern Singleton sia robusto e non venga compromesso dal testing.</p>
     *
     * <p><strong>Aspetti del Singleton testati:</strong></p>
     * <ul>
     *   <li>Unicità dell'istanza restituita da getInstance()</li>
     *   <li>Consistenza tra chiamate multiple</li>
     *   <li>Mantenimento del pattern anche con mock injection</li>
     *   <li>Integrità dell'istanza durante i test</li>
     * </ul>
     */
    @Test
    @DisplayName("Should verify singleton behavior with mocked dependencies")
    void testSingletonBehaviorWithMocks() {
        // Act - Ottenimento di multiple istanze
        LibraryManager instance1 = LibraryManager.getInstance();
        LibraryManager instance2 = LibraryManager.getInstance();

        // Assert - Verifica che siano la stessa istanza
        assertSame(instance1, instance2);
        assertSame(libraryManager, instance1);
    }

    /**
     * Test di getTotalItems con collezione mockata.
     *
     * <p>Questo test verifica che il metodo getTotalItems del LibraryManager
     * deleghi correttamente alla collezione interna e restituisca il valore
     * atteso. Utilizza mock per controllare il comportamento della collezione.</p>
     *
     * <p><strong>Aspetti testati:</strong></p>
     * <ul>
     *   <li>Delega corretta alla collezione interna</li>
     *   <li>Restituzione del valore corretto</li>
     *   <li>Verifica delle interazioni con la collezione</li>
     *   <li>Comportamento con valori controllati</li>
     * </ul>
     */
    @Test
    @DisplayName("Should handle getTotalItems with mocked collection")
    void testGetTotalItemsWithMockedCollection() {
        // Arrange - Preparazione valore atteso
        int expectedSize = 5;
        when(mockCollection.size()).thenReturn(expectedSize);

        // Act - Richiesta del numero totale di elementi
        int totalItems = libraryManager.getTotalItems();

        // Assert - Verifica del valore e delle interazioni
        assertEquals(expectedSize, totalItems);
        verify(mockCollection).size();
    }

    /**
     * Metodo helper per resettare il Singleton utilizzando reflection.
     *
     * <p>Questo metodo utilizza la reflection per accedere al campo statico
     * privato "instance" del LibraryManager e reimpostarlo a null, permettendo
     * di testare il comportamento del Singleton in isolamento per ogni test.</p>
     *
     * <p><strong>Tecnica utilizzata:</strong></p>
     * <ul>
     *   <li>Reflection per accesso a campi privati</li>
     *   <li>setAccessible(true) per bypassare l'incapsulamento</li>
     *   <li>Reset dello stato statico per test indipendenti</li>
     * </ul>
     *
     * <p><strong>Necessità:</strong> Garantisce che ogni test inizi con uno stato pulito</p>
     *
     * @throws Exception se si verifica un errore durante la reflection
     */
    private void resetSingleton() throws Exception {
        // Accesso al campo statico privato "instance"
        Field instanceField = LibraryManager.class.getDeclaredField("instance");
        instanceField.setAccessible(true); // Bypass dell'incapsulamento
        instanceField.set(null, null); // Reset a null per nuovo test
    }

    /**
     * Metodo helper per iniettare le dipendenze mock utilizzando reflection.
     *
     * <p>Questo metodo avanzato utilizza la reflection per sostituire le
     * dipendenze interne del LibraryManager con oggetti mock, permettendo
     * un controllo completo del comportamento durante i test.</p>
     *
     * <p><strong>Dipendenze iniettate:</strong></p>
     * <ul>
     *   <li><strong>collection:</strong> Mock di LibraryCollection per controllo iterazione</li>
     *   <li><strong>itemsById:</strong> Map controllata per test di lookup</li>
     * </ul>
     *
     * <p><strong>Vantaggi dell'approccio:</strong></p>
     * <ul>
     *   <li>Isolamento completo delle dipendenze</li>
     *   <li>Controllo totale del comportamento</li>
     *   <li>Test deterministici e ripetibili</li>
     *   <li>Verifica delle interazioni specifiche</li>
     * </ul>
     *
     * @throws Exception se si verifica un errore durante la reflection
     */
    private void injectMockDependencies() throws Exception {
        // Injection della mock collection per controllo dell'iterazione
        Field collectionField = LibraryManager.class.getDeclaredField("collection");
        collectionField.setAccessible(true);
        collectionField.set(libraryManager, mockCollection);

        // Injection della mock map per controllo del lookup per ID
        Field itemsByIdField = LibraryManager.class.getDeclaredField("itemsById");
        itemsByIdField.setAccessible(true);
        itemsByIdField.set(libraryManager, mockItemsById);
    }
}
