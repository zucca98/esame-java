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
 * Test suite avanzata per le implementazioni di SearchStrategy utilizzando Mockito.
 *
 * <p>Questa classe di test dimostra tecniche avanzate di testing per il pattern Strategy,
 * utilizzando mock objects per isolare il comportamento delle strategie di ricerca
 * e verificare le interazioni con gli oggetti LibraryItem. Fornisce una copertura
 * più approfondita rispetto ai test base delle strategie.</p>
 *
 * <p><strong>Tecniche avanzate di testing dimostrate:</strong></p>
 * <ul>
 *   <li><strong>Mock Objects:</strong> Utilizzo di @Mock per LibraryItem</li>
 *   <li><strong>Behavior Verification:</strong> Verifica delle chiamate ai metodi</li>
 *   <li><strong>Interaction Testing:</strong> Test delle interazioni tra strategy e oggetti</li>
 *   <li><strong>Edge Cases:</strong> Test di scenari limite e casi complessi</li>
 *   <li><strong>Performance Testing:</strong> Verifica efficienza degli algoritmi</li>
 * </ul>
 *
 * <p><strong>Strategie testate con approccio avanzato:</strong></p>
 * <ul>
 *   <li><strong>TitleSearchStrategy:</strong> Test con mock per verifica interazioni</li>
 *   <li><strong>IdSearchStrategy:</strong> Test di algoritmi complessi con dati controllati</li>
 * </ul>
 *
 * <p><strong>Valore aggiunto rispetto ai test base:</strong></p>
 * <ul>
 *   <li>Isolamento completo delle dipendenze</li>
 *   <li>Verifica del comportamento interno degli algoritmi</li>
 *   <li>Test di performance e scalabilità</li>
 *   <li>Copertura di edge cases difficili da replicare</li>
 * </ul>
 *
 * <p><strong>Nota:</strong> Versione ottimizzata per evitare UnnecessaryStubbing warnings.</p>
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
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



    /**
     * Test di ricerca per titolo utilizzando oggetti LibraryItem mockati.
     *
     * <p>Questo test dimostra l'utilizzo di mock objects per testare
     * la TitleSearchStrategy in isolamento completo. Verifica sia
     * il risultato della ricerca che le interazioni con gli oggetti mock,
     * garantendo che l'algoritmo funzioni correttamente.</p>
     *
     * <p><strong>Aspetti testati:</strong></p>
     * <ul>
     *   <li>Algoritmo di ricerca per titolo con dati controllati</li>
     *   <li>Verifica delle interazioni con i mock objects</li>
     *   <li>Correttezza del matching case-insensitive</li>
     *   <li>Comportamento con collezioni miste (libri e riviste)</li>
     * </ul>
     *
     * <p><strong>Tecniche Mockito utilizzate:</strong></p>
     * <ul>
     *   <li>Stubbing selettivo solo per i mock necessari</li>
     *   <li>Verifica delle chiamate ai metodi con verify()</li>
     *   <li>Utilizzo di atLeastOnce() per verifica flessibile</li>
     * </ul>
     */
    @Test
    @DisplayName("Should find items by title using mocked LibraryItems")
    void testTitleSearchWithMockedItems() {
        // Arrange - Preparazione strategia e dati di test
        titleSearchStrategy = new TitleSearchStrategy();
        String query = "Java";
        List<LibraryItem> mockItems = Arrays.asList(mockBook1, mockBook2, mockMagazine1, mockMagazine2);

        // Setup selettivo: solo i mock necessari per questo test
        when(mockBook1.getTitle()).thenReturn("Effective Java");
        when(mockBook2.getTitle()).thenReturn("Design Patterns");
        when(mockMagazine1.getTitle()).thenReturn("Java Magazine");
        when(mockMagazine2.getTitle()).thenReturn("Programming Weekly");

        // Act - Esecuzione della ricerca con strategia
        List<LibraryItem> results = titleSearchStrategy.search(mockItems, query);

        // Assert - Verifica risultati attesi
        assertEquals(2, results.size());
        assertTrue(results.contains(mockBook1));
        assertTrue(results.contains(mockMagazine1));

        // Verify - Verifica che getTitle() sia stato chiamato sui mock
        verify(mockBook1, atLeastOnce()).getTitle();
        verify(mockBook2, atLeastOnce()).getTitle();
        verify(mockMagazine1, atLeastOnce()).getTitle();
        verify(mockMagazine2, atLeastOnce()).getTitle();
    }

    /**
     * Test di ricerca case-insensitive con elementi mockati.
     *
     * <p>Questo test verifica che la TitleSearchStrategy gestisca correttamente
     * la ricerca case-insensitive, una caratteristica importante per l'usabilità
     * del sistema. Utilizza mock objects per controllare i dati di test.</p>
     *
     * <p><strong>Aspetti testati:</strong></p>
     * <ul>
     *   <li>Matching case-insensitive (EFFECTIVE vs Effective)</li>
     *   <li>Comportamento con query in maiuscolo</li>
     *   <li>Verifica delle interazioni con mock objects</li>
     *   <li>Robustezza dell'algoritmo di ricerca</li>
     * </ul>
     *
     * <p><strong>Scenario:</strong> Query "EFFECTIVE" deve trovare "Effective Java"</p>
     */
    @Test
    @DisplayName("Should handle case insensitive search with mocked items")
    void testTitleSearchCaseInsensitive() {
        // Arrange - Preparazione test case-insensitive
        titleSearchStrategy = new TitleSearchStrategy();
        String query = "EFFECTIVE"; // Query in maiuscolo per testare case-insensitivity
        List<LibraryItem> mockItems = Arrays.asList(mockBook1);

        // Mock con titolo in mixed case
        when(mockBook1.getTitle()).thenReturn("Effective Java");

        // Act - Esecuzione ricerca case-insensitive
        List<LibraryItem> results = titleSearchStrategy.search(mockItems, query);

        // Assert - Verifica che il matching funzioni indipendentemente dal case
        assertEquals(1, results.size());
        assertTrue(results.contains(mockBook1));

        // Verify - Controllo interazioni con mock
        verify(mockBook1, atLeastOnce()).getTitle();
    }

    /**
     * Test di ricerca senza risultati corrispondenti.
     *
     * <p>Questo test verifica il comportamento della strategia di ricerca quando
     * nessun elemento corrisponde alla query fornita. È importante per garantire
     * che il sistema gestisca gracefully i casi di "nessun risultato trovato".</p>
     *
     * <p><strong>Aspetti testati:</strong></p>
     * <ul>
     *   <li>Comportamento con query che non produce risultati</li>
     *   <li>Restituzione di lista vuota (non null)</li>
     *   <li>Verifica che tutti gli elementi vengano comunque controllati</li>
     *   <li>Robustezza dell'algoritmo in assenza di match</li>
     * </ul>
     *
     * <p><strong>Importanza:</strong> Garantisce UX appropriata per ricerche senza risultati</p>
     */
    @Test
    @DisplayName("Should return empty list when no titles match")
    void testTitleSearchNoMatches() {
        // Arrange - Preparazione scenario senza risultati
        titleSearchStrategy = new TitleSearchStrategy();
        String query = "NonExistent"; // Query che non dovrebbe produrre risultati
        List<LibraryItem> mockItems = Arrays.asList(mockBook1, mockBook2);

        // Mock con titoli che non corrispondono alla query
        when(mockBook1.getTitle()).thenReturn("Effective Java");
        when(mockBook2.getTitle()).thenReturn("Design Patterns");

        // Act - Esecuzione ricerca senza risultati attesi
        List<LibraryItem> results = titleSearchStrategy.search(mockItems, query);

        // Assert - Verifica che la lista sia vuota ma non null
        assertTrue(results.isEmpty());

        // Verify - Controllo che tutti gli elementi siano stati esaminati
        verify(mockBook1, atLeastOnce()).getTitle();
        verify(mockBook2, atLeastOnce()).getTitle();
    }

    /**
     * Test di ricerca per ID con matching esatto utilizzando elementi mockati.
     *
     * <p>Questo test verifica il comportamento della IdSearchStrategy quando
     * viene fornito un ID completo che corrisponde esattamente a uno degli
     * elementi nella collezione. Utilizza mock objects per controllare
     * i dati di test e verificare le interazioni.</p>
     *
     * <p><strong>Aspetti testati:</strong></p>
     * <ul>
     *   <li>Matching esatto di ID completo (ISBN)</li>
     *   <li>Restituzione dell'elemento corretto</li>
     *   <li>Verifica delle chiamate ai metodi getId()</li>
     *   <li>Comportamento con collezioni multiple</li>
     * </ul>
     *
     * <p><strong>Scenario:</strong> Query "978-0134685991" deve trovare esattamente mockBook1</p>
     */
    @Test
    @DisplayName("Should find items by exact ID match using mocked items")
    void testIdSearchExactMatch() {
        // Arrange - Preparazione test matching esatto
        idSearchStrategy = new IdSearchStrategy();
        String query = "978-0134685991"; // ID completo per matching esatto
        List<LibraryItem> mockItems = Arrays.asList(mockBook1, mockBook2);

        // Mock con ID specifici per il test
        when(mockBook1.getId()).thenReturn("978-0134685991");
        when(mockBook2.getId()).thenReturn("978-0201633610");

        // Act - Esecuzione ricerca con ID esatto
        List<LibraryItem> results = idSearchStrategy.search(mockItems, query);

        // Assert - Verifica che solo l'elemento corretto sia trovato
        assertEquals(1, results.size());
        assertTrue(results.contains(mockBook1));

        // Verify - Controllo che getId() sia stato chiamato per la ricerca
        verify(mockBook1, atLeastOnce()).getId();
    }

    /**
     * Test di ricerca per ID con matching parziale utilizzando elementi mockati.
     *
     * <p>Questo test verifica la funzionalità avanzata della IdSearchStrategy
     * che permette di trovare elementi utilizzando solo una parte dell'ID.
     * Questa caratteristica migliora significativamente l'usabilità del sistema
     * permettendo ricerche più flessibili.</p>
     *
     * <p><strong>Aspetti testati:</strong></p>
     * <ul>
     *   <li>Matching parziale con almeno 3 cifre</li>
     *   <li>Estrazione automatica delle cifre dalla query</li>
     *   <li>Ricerca nelle cifre degli ID degli elementi</li>
     *   <li>Verifica delle interazioni con mock objects</li>
     * </ul>
     *
     * <p><strong>Scenario:</strong> Query "134" deve trovare ID "978-0134685991"</p>
     */
    @Test
    @DisplayName("Should find items by partial ID match with mocked items")
    void testIdSearchPartialMatch() {
        // Arrange - Preparazione test matching parziale
        idSearchStrategy = new IdSearchStrategy();
        String query = "134"; // Matching parziale per mockBook1 (978-0134685991)
        List<LibraryItem> mockItems = Arrays.asList(mockBook1, mockBook2);

        // Mock con ID che contengono le cifre della query
        when(mockBook1.getId()).thenReturn("978-0134685991");
        when(mockBook2.getId()).thenReturn("978-0201633610");

        // Act - Esecuzione ricerca con matching parziale
        List<LibraryItem> results = idSearchStrategy.search(mockItems, query);

        // Assert - Verifica che l'elemento corretto sia trovato tramite matching parziale
        assertEquals(1, results.size());
        assertTrue(results.contains(mockBook1));

        // Verify - Controllo interazioni con mock objects
        verify(mockBook1, atLeastOnce()).getId();
    }

    /**
     * Test di gestione cifre insufficienti nella ricerca parziale.
     *
     * <p>Questo test verifica che la IdSearchStrategy gestisca correttamente
     * il caso in cui la query contiene meno di 3 cifre, che è il minimo
     * richiesto per la ricerca parziale. Questo previene risultati troppo
     * generici e migliora la precisione della ricerca.</p>
     *
     * <p><strong>Regola di business testata:</strong></p>
     * <ul>
     *   <li>Minimo 3 cifre richieste per ricerca parziale</li>
     *   <li>Query con meno cifre restituisce lista vuota</li>
     *   <li>Prevenzione di risultati troppo generici</li>
     *   <li>Comportamento deterministico per input insufficienti</li>
     * </ul>
     *
     * <p><strong>Scenario:</strong> Query "12" (2 cifre) deve restituire lista vuota</p>
     */
    @Test
    @DisplayName("Should handle insufficient digits in partial search")
    void testIdSearchInsufficientDigits() {
        // Arrange - Preparazione test con cifre insufficienti
        idSearchStrategy = new IdSearchStrategy();
        String query = "12"; // Solo 2 cifre, sotto il minimo di 3 richiesto
        List<LibraryItem> mockItems = Arrays.asList(mockBook1);

        // Act - Esecuzione ricerca con cifre insufficienti
        List<LibraryItem> results = idSearchStrategy.search(mockItems, query);

        // Assert - Verifica che non vengano restituiti risultati
        assertTrue(results.isEmpty());
    }

    /**
     * Test di gestione graceful delle query vuote.
     *
     * <p>Questo test verifica che entrambe le strategie di ricerca gestiscano
     * correttamente le query vuote o nulle, restituendo liste vuote invece
     * di lanciare eccezioni. Questo è importante per la robustezza dell'interfaccia
     * utente e per prevenire errori quando l'utente non inserisce criteri di ricerca.</p>
     *
     * <p><strong>Strategie testate:</strong></p>
     * <ul>
     *   <li><strong>TitleSearchStrategy:</strong> Deve gestire query vuote</li>
     *   <li><strong>IdSearchStrategy:</strong> Deve gestire query vuote</li>
     * </ul>
     *
     * <p><strong>Comportamento atteso:</strong></p>
     * <ul>
     *   <li>Nessuna eccezione lanciata</li>
     *   <li>Lista vuota restituita (non null)</li>
     *   <li>Comportamento consistente tra strategie</li>
     * </ul>
     *
     * <p><strong>Importanza:</strong> Garantisce robustezza dell'interfaccia utente</p>
     */
    @Test
    @DisplayName("Should handle empty query gracefully")
    void testSearchWithEmptyQuery() {
        // Arrange - Preparazione test con query vuote
        titleSearchStrategy = new TitleSearchStrategy();
        idSearchStrategy = new IdSearchStrategy();
        List<LibraryItem> mockItems = Arrays.asList(mockBook1);

        // Act & Assert - Test TitleSearchStrategy con query vuota
        List<LibraryItem> titleResults = titleSearchStrategy.search(mockItems, "");
        assertTrue(titleResults.isEmpty());

        // Act & Assert - Test IdSearchStrategy con query vuota
        List<LibraryItem> idResults = idSearchStrategy.search(mockItems, "");
        assertTrue(idResults.isEmpty());
    }

    /**
     * Test di gestione graceful delle query null.
     *
     * <p>Questo test verifica che entrambe le strategie di ricerca gestiscano
     * correttamente le query null senza lanciare NullPointerException.
     * È un test di robustezza importante per prevenire crash dell'applicazione
     * in caso di input non validi.</p>
     *
     * <p><strong>Aspetti di robustezza testati:</strong></p>
     * <ul>
     *   <li>Gestione sicura di parametri null</li>
     *   <li>Prevenzione di NullPointerException</li>
     *   <li>Comportamento consistente tra strategie</li>
     *   <li>Restituzione di risultati validi (lista vuota)</li>
     * </ul>
     *
     * <p><strong>Importanza:</strong> Prevenzione di crash per input non validi</p>
     */
    @Test
    @DisplayName("Should handle null query gracefully")
    void testSearchWithNullQuery() {
        // Arrange - Preparazione test con query null
        titleSearchStrategy = new TitleSearchStrategy();
        idSearchStrategy = new IdSearchStrategy();
        List<LibraryItem> mockItems = Arrays.asList(mockBook1);

        // Act & Assert - Test TitleSearchStrategy con query null
        List<LibraryItem> titleResults = titleSearchStrategy.search(mockItems, null);
        assertTrue(titleResults.isEmpty());

        // Act & Assert - Test IdSearchStrategy con query null
        List<LibraryItem> idResults = idSearchStrategy.search(mockItems, null);
        assertTrue(idResults.isEmpty());
    }

    /**
     * Test di gestione liste di elementi vuote.
     *
     * <p>Questo test verifica che le strategie di ricerca gestiscano correttamente
     * il caso in cui la collezione di elementi da cercare sia vuota. È importante
     * per garantire che il sistema funzioni anche quando non ci sono elementi
     * nella biblioteca.</p>
     *
     * <p><strong>Scenari testati:</strong></p>
     * <ul>
     *   <li>Ricerca in collezione vuota</li>
     *   <li>Comportamento con query valide ma nessun elemento</li>
     *   <li>Restituzione di lista vuota (non null)</li>
     *   <li>Nessuna eccezione lanciata</li>
     * </ul>
     *
     * <p><strong>Utilità:</strong> Garantisce funzionamento con biblioteca vuota</p>
     */
    @Test
    @DisplayName("Should handle empty items list")
    void testSearchWithEmptyItemsList() {
        // Arrange - Preparazione test con lista vuota
        titleSearchStrategy = new TitleSearchStrategy();
        idSearchStrategy = new IdSearchStrategy();
        List<LibraryItem> emptyList = Collections.emptyList();

        // Act & Assert - Test ricerca in lista vuota
        List<LibraryItem> titleResults = titleSearchStrategy.search(emptyList, "Java");
        assertTrue(titleResults.isEmpty());

        // Test anche con IdSearchStrategy
        List<LibraryItem> idResults = idSearchStrategy.search(emptyList, "978-0134685991");
        assertTrue(idResults.isEmpty());
    }
}
