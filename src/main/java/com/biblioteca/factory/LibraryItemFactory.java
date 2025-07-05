package com.biblioteca.factory;

import com.biblioteca.exceptions.InvalidDataException;
import com.biblioteca.model.Book;
import com.biblioteca.model.LibraryItem;
import com.biblioteca.model.Magazine;
import com.biblioteca.util.AuthorValidator;

/**
 * Implementazione del pattern Factory per la creazione di oggetti LibraryItem.
 *
 * <p>Questa classe implementa il pattern Factory Method per centralizzare
 * la creazione di tutti i tipi di elementi bibliotecari. Fornisce metodi
 * statici per creare istanze di {@link Book} e {@link Magazine} con
 * validazione completa dei dati di input.</p>
 *
 * <p><strong>Vantaggi del pattern Factory:</strong></p>
 * <ul>
 *   <li><strong>Centralizzazione:</strong> Tutta la logica di creazione in un posto</li>
 *   <li><strong>Validazione:</strong> Controlli di validità prima della creazione</li>
 *   <li><strong>Consistenza:</strong> Garantisce oggetti sempre validi</li>
 *   <li><strong>Estensibilità:</strong> Facile aggiunta di nuovi tipi di elementi</li>
 *   <li><strong>Disaccoppiamento:</strong> I client non dipendono dalle classi concrete</li>
 * </ul>
 *
 * <p><strong>Strategia di validazione:</strong></p>
 * <ul>
 *   <li>Validazione di tutti i parametri obbligatori</li>
 *   <li>Controllo dei formati e dei valori ammissibili</li>
 *   <li>Utilizzo di validator specializzati (es. AuthorValidator)</li>
 *   <li>Lancio di eccezioni specifiche per errori di validazione</li>
 * </ul>
 *
 * <p>Tutti i metodi di creazione sono statici per facilitare l'utilizzo
 * senza necessità di istanziare la factory.</p>
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
 */
public class LibraryItemFactory {

    /**
     * Crea una nuova istanza di Book con validazione completa dei dati.
     *
     * <p>Questo metodo factory centralizza la creazione di libri garantendo
     * che tutti i dati forniti siano validi prima della costruzione dell'oggetto.
     * Utilizza il pattern Builder interno alla classe Book per la costruzione.</p>
     *
     * <p><strong>Validazioni eseguite:</strong></p>
     * <ul>
     *   <li>ISBN non nullo e non vuoto</li>
     *   <li>Titolo non nullo e non vuoto</li>
     *   <li>Autore non nullo, non vuoto e formato valido</li>
     *   <li>Numero di pagine positivo</li>
     * </ul>
     *
     * @param isbn l'ISBN del libro (deve essere non nullo e non vuoto)
     * @param title il titolo del libro (deve essere non nullo e non vuoto)
     * @param author l'autore del libro (deve essere valido secondo AuthorValidator)
     * @param pages il numero di pagine (deve essere positivo)
     * @return una nuova istanza di Book completamente validata
     * @throws InvalidDataException se uno qualsiasi dei parametri non è valido
     */
    public static LibraryItem createBook(String isbn, String title, String author, int pages)
            throws InvalidDataException {
        // Validazione completa dei dati prima della creazione
        validateBookData(isbn, title, author, pages);

        // Utilizzo del pattern Builder per la costruzione dell'oggetto
        return new Book.BookBuilder()
                .setIsbn(isbn)
                .setTitle(title)
                .setAuthor(author)
                .setPages(pages)
                .build();
    }

    /**
     * Crea una nuova istanza di Magazine con validazione completa dei dati.
     *
     * <p>Questo metodo factory centralizza la creazione di riviste garantendo
     * che tutti i dati forniti siano validi prima della costruzione dell'oggetto.
     * Utilizza il costruttore diretto della classe Magazine.</p>
     *
     * <p><strong>Validazioni eseguite:</strong></p>
     * <ul>
     *   <li>ISSN non nullo e non vuoto</li>
     *   <li>Titolo non nullo e non vuoto</li>
     *   <li>Numero di edizione positivo</li>
     * </ul>
     *
     * @param issn l'ISSN della rivista (deve essere non nullo e non vuoto)
     * @param title il titolo della rivista (deve essere non nullo e non vuoto)
     * @param issueNumber il numero di edizione (deve essere positivo)
     * @return una nuova istanza di Magazine completamente validata
     * @throws InvalidDataException se uno qualsiasi dei parametri non è valido
     */
    public static LibraryItem createMagazine(String issn, String title, int issueNumber)
            throws InvalidDataException {
        // Validazione completa dei dati prima della creazione
        validateMagazineData(issn, title, issueNumber);

        // Creazione diretta dell'oggetto Magazine
        return new Magazine(issn, title, issueNumber);
    }

    /**
     * Valida tutti i dati necessari per la creazione di un libro.
     *
     * <p>Questo metodo privato esegue una validazione completa di tutti
     * i parametri necessari per creare un libro. Ogni controllo è specifico
     * e fornisce messaggi di errore dettagliati per facilitare il debugging.</p>
     *
     * <p><strong>Controlli eseguiti:</strong></p>
     * <ul>
     *   <li>ISBN: non nullo, non vuoto dopo trim</li>
     *   <li>Titolo: non nullo, non vuoto dopo trim</li>
     *   <li>Autore: non nullo, non vuoto, formato valido</li>
     *   <li>Pagine: valore positivo</li>
     * </ul>
     *
     * @param isbn l'ISBN da validare
     * @param title il titolo da validare
     * @param author l'autore da validare
     * @param pages il numero di pagine da validare
     * @throws InvalidDataException se uno qualsiasi dei parametri non è valido
     */
    private static void validateBookData(String isbn, String title, String author, int pages)
            throws InvalidDataException {
        // Validazione ISBN: deve essere presente e non vuoto
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new InvalidDataException("ISBN cannot be empty");
        }

        // Validazione titolo: deve essere presente e non vuoto
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidDataException("Title cannot be empty");
        }

        // Validazione autore: deve essere presente e non vuoto
        if (author == null || author.trim().isEmpty()) {
            throw new InvalidDataException("Author cannot be empty");
        }

        // Validazione formato autore utilizzando validator specializzato
        if (!AuthorValidator.isValidAuthor(author)) {
            throw new InvalidDataException(AuthorValidator.getValidationErrorMessage(author));
        }

        // Validazione numero di pagine: deve essere positivo
        if (pages <= 0) {
            throw new InvalidDataException("Pages must be positive");
        }
    }

    /**
     * Valida tutti i dati necessari per la creazione di una rivista.
     *
     * <p>Questo metodo privato esegue una validazione completa di tutti
     * i parametri necessari per creare una rivista. I controlli sono
     * specifici per le caratteristiche delle riviste.</p>
     *
     * <p><strong>Controlli eseguiti:</strong></p>
     * <ul>
     *   <li>ISSN: non nullo, non vuoto dopo trim</li>
     *   <li>Titolo: non nullo, non vuoto dopo trim</li>
     *   <li>Numero edizione: valore positivo</li>
     * </ul>
     *
     * @param issn l'ISSN da validare
     * @param title il titolo da validare
     * @param issueNumber il numero di edizione da validare
     * @throws InvalidDataException se uno qualsiasi dei parametri non è valido
     */
    private static void validateMagazineData(String issn, String title, int issueNumber)
            throws InvalidDataException {
        // Validazione ISSN: deve essere presente e non vuoto
        if (issn == null || issn.trim().isEmpty()) {
            throw new InvalidDataException("ISSN cannot be empty");
        }

        // Validazione titolo: deve essere presente e non vuoto
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidDataException("Title cannot be empty");
        }

        // Validazione numero edizione: deve essere positivo
        if (issueNumber <= 0) {
            throw new InvalidDataException("Issue number must be positive");
        }
    }
}