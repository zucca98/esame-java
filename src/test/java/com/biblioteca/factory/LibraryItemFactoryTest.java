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
 * Test suite per la classe LibraryItemFactory.
 *
 * <p>Questa classe di test verifica il corretto funzionamento del pattern Factory
 * implementato nella classe {@link LibraryItemFactory}. I test coprono sia i
 * casi di successo che i casi di errore, garantendo la robustezza della
 * validazione e della creazione degli oggetti.</p>
 *
 * <p><strong>Strategia di testing:</strong></p>
 * <ul>
 *   <li><strong>Test positivi:</strong> Verifica creazione corretta con dati validi</li>
 *   <li><strong>Test negativi:</strong> Verifica gestione errori con dati non validi</li>
 *   <li><strong>Test di validazione:</strong> Verifica regole di business specifiche</li>
 *   <li><strong>Test edge cases:</strong> Verifica comportamento ai limiti</li>
 * </ul>
 *
 * <p><strong>Aree di test coperte:</strong></p>
 * <ul>
 *   <li>Creazione di libri con dati validi e non validi</li>
 *   <li>Creazione di riviste con dati validi e non validi</li>
 *   <li>Validazione degli autori (inclusi casi numerici)</li>
 *   <li>Validazione di ISBN, ISSN, titoli e parametri numerici</li>
 * </ul>
 *
 * <p><strong>Tecnologie di testing utilizzate:</strong></p>
 * <ul>
 *   <li><strong>JUnit 5:</strong> Framework di testing principale</li>
 *   <li><strong>Assertions:</strong> Verifica dei risultati attesi</li>
 *   <li><strong>Exception testing:</strong> Verifica gestione errori</li>
 *   <li><strong>DisplayName:</strong> Descrizioni leggibili dei test</li>
 * </ul>
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
 */
class LibraryItemFactoryTest {

    /**
     * Test di creazione libro con dati validi.
     *
     * <p>Verifica che il Factory Pattern crei correttamente un libro quando
     * tutti i dati forniti sono validi. Questo test rappresenta il caso
     * di successo principale per la creazione di libri.</p>
     *
     * <p><strong>Aspetti testati:</strong></p>
     * <ul>
     *   <li>Creazione oggetto non nullo</li>
     *   <li>Tipo corretto dell'oggetto (Book)</li>
     *   <li>Corretta assegnazione di tutti i campi</li>
     *   <li>Stato iniziale di disponibilità</li>
     * </ul>
     */
    @Test
    @DisplayName("Should create book with valid data")
    void testCreateBookValid() throws InvalidDataException {
        // Creazione libro con dati validi tramite Factory
        LibraryItem book = LibraryItemFactory.createBook(
                "978-0134685991", "Effective Java", "Joshua Bloch", 416);

        // Verifica che l'oggetto sia stato creato correttamente
        assertNotNull(book);
        assertTrue(book instanceof Book);
        assertEquals("978-0134685991", book.getId());
        assertEquals("Effective Java", book.getTitle());
        assertEquals("Book", book.getType());
        assertTrue(book.isAvailable()); // Verifica stato iniziale
    }

    /**
     * Test di creazione rivista con dati validi.
     *
     * <p>Verifica che il Factory Pattern crei correttamente una rivista quando
     * tutti i dati forniti sono validi. Complementa il test per i libri
     * verificando il secondo tipo di elemento supportato.</p>
     *
     * <p><strong>Aspetti testati:</strong></p>
     * <ul>
     *   <li>Creazione oggetto Magazine</li>
     *   <li>Corretta assegnazione ISSN e titolo</li>
     *   <li>Tipo corretto dell'elemento</li>
     *   <li>Stato iniziale di disponibilità</li>
     * </ul>
     */
    @Test
    @DisplayName("Should create magazine with valid data")
    void testCreateMagazineValid() throws InvalidDataException {
        // Creazione rivista con dati validi tramite Factory
        LibraryItem magazine = LibraryItemFactory.createMagazine(
                "1234-5678", "Java Magazine", 45);

        // Verifica che l'oggetto sia stato creato correttamente
        assertNotNull(magazine);
        assertTrue(magazine instanceof Magazine);
        assertEquals("1234-5678", magazine.getId());
        assertEquals("Java Magazine", magazine.getTitle());
        assertEquals("Magazine", magazine.getType());
        assertTrue(magazine.isAvailable()); // Verifica stato iniziale
    }

    /**
     * Test di validazione per ISBN vuoto.
     *
     * <p>Verifica che il Factory lanci un'eccezione appropriata quando
     * viene fornito un ISBN vuoto. Questo test garantisce che la
     * validazione dei dati obbligatori funzioni correttamente.</p>
     *
     * <p><strong>Strategia di test:</strong></p>
     * <ul>
     *   <li>Utilizza assertThrows per verificare l'eccezione</li>
     *   <li>Controlla il tipo specifico di eccezione</li>
     *   <li>Verifica il messaggio di errore appropriato</li>
     * </ul>
     */
    @Test
    @DisplayName("Should throw exception for empty ISBN")
    void testCreateBookEmptyIsbn() {
        // Test di validazione: ISBN vuoto deve lanciare eccezione
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> LibraryItemFactory.createBook("", "Title", "Author", 100)
        );
        // Verifica che il messaggio di errore sia appropriato
        assertTrue(exception.getMessage().contains("ISBN cannot be empty"));
    }

    /**
     * Test di validazione per numero di pagine negativo.
     *
     * <p>Verifica che il Factory rifiuti valori negativi per il numero
     * di pagine, garantendo l'integrità dei dati numerici.</p>
     */
    @Test
    @DisplayName("Should throw exception for negative pages")
    void testCreateBookNegativePages() {
        // Test di validazione: pagine negative devono lanciare eccezione
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> LibraryItemFactory.createBook("123", "Title", "Author", -1)
        );
        // Verifica messaggio di errore specifico per pagine
        assertTrue(exception.getMessage().contains("Pages must be positive"));
    }

    /**
     * Test di validazione per titolo nullo.
     *
     * <p>Verifica la gestione di valori null per i campi obbligatori,
     * garantendo che il sistema non accetti dati incompleti.</p>
     */
    @Test
    @DisplayName("Should throw exception for null title")
    void testCreateBookNullTitle() {
        // Test di validazione: titolo null deve lanciare eccezione
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> LibraryItemFactory.createBook("123", null, "Author", 100)
        );
        // Verifica messaggio di errore per titolo mancante
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

    /**
     * Test di validazione per autore puramente numerico.
     *
     * <p>Verifica che il sistema rifiuti nomi di autori che consistono
     * solo di numeri, implementando una regola di business importante
     * per la qualità dei dati.</p>
     *
     * <p><strong>Caso di test:</strong> Autore "123" deve essere rifiutato</p>
     */
    @Test
    @DisplayName("Should throw exception for purely numeric author")
    void testCreateBookNumericOnlyAuthor() {
        // Test validazione autore: solo numeri non è valido
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> LibraryItemFactory.createBook("123", "Title", "123", 100)
        );
        // Verifica messaggio specifico per autori numerici
        assertTrue(exception.getMessage().contains("cannot consist entirely of numbers"));
    }

    /**
     * Test di validazione per autore numerico con separatori.
     *
     * <p>Verifica che anche numeri con separatori comuni (come trattini)
     * vengano rifiutati, prevenendo l'inserimento di codici al posto
     * di nomi reali.</p>
     *
     * <p><strong>Caso di test:</strong> Autore "123-456" deve essere rifiutato</p>
     */
    @Test
    @DisplayName("Should throw exception for numeric author with separators")
    void testCreateBookNumericAuthorWithSeparators() {
        // Test validazione autore: numeri con separatori non è valido
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> LibraryItemFactory.createBook("123", "Title", "123-456", 100)
        );
        // Verifica messaggio di errore appropriato
        assertTrue(exception.getMessage().contains("cannot consist entirely of numbers"));
    }

    /**
     * Test di accettazione per autore valido con numeri.
     *
     * <p>Verifica che nomi di autori legittimi che contengono numeri
     * (come "Jr. 3rd") vengano accettati correttamente, dimostrando
     * che la validazione è intelligente e non troppo restrittiva.</p>
     *
     * <p><strong>Caso di test:</strong> "John Smith Jr. 3rd" deve essere accettato</p>
     */
    @Test
    @DisplayName("Should accept valid author with numbers as part of name")
    void testCreateBookValidAuthorWithNumbers() throws InvalidDataException {
        // Test caso positivo: autore con numeri come parte del nome
        LibraryItem book = LibraryItemFactory.createBook(
                "978-0134685991", "Effective Java", "John Smith Jr. 3rd", 416);

        // Verifica che l'autore sia stato accettato e memorizzato correttamente
        assertNotNull(book);
        assertTrue(book instanceof Book);
        assertEquals("John Smith Jr. 3rd", ((Book) book).getAuthor());
    }

    /**
     * Test di accettazione per autore alfanumerico misto.
     *
     * <p>Verifica che autori con caratteri misti alfanumerici vengano
     * accettati, coprendo casi come pseudonimi o nomi moderni.</p>
     */
    @Test
    @DisplayName("Should accept author with mixed alphanumeric characters")
    void testCreateBookMixedAlphanumericAuthor() throws InvalidDataException {
        // Test caso positivo: autore con caratteri alfanumerici misti
        LibraryItem book = LibraryItemFactory.createBook(
                "978-0134685991", "Programming Book", "Author123", 300);

        // Verifica accettazione dell'autore alfanumerico
        assertNotNull(book);
        assertTrue(book instanceof Book);
        assertEquals("Author123", ((Book) book).getAuthor());
    }

    /**
     * Test di validazione per caratteri speciali e numeri.
     *
     * <p>Verifica che combinazioni di caratteri speciali e numeri
     * senza lettere vengano rifiutate, mantenendo la qualità dei dati.</p>
     */
    @Test
    @DisplayName("Should throw exception for author with only special characters and numbers")
    void testCreateBookSpecialCharactersAndNumbers() {
        // Test validazione: caratteri speciali + numeri senza lettere
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> LibraryItemFactory.createBook("123", "Title", "123@456#789", 100)
        );
        // Verifica rifiuto di caratteri non alfabetici
        assertTrue(exception.getMessage().contains("cannot consist entirely of numbers"));
    }
}
