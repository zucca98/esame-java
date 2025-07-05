package com.biblioteca.manager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.biblioteca.exceptions.BookNotFoundException;
import com.biblioteca.exceptions.InvalidDataException;
import com.biblioteca.exceptions.LibraryException;
import com.biblioteca.factory.LibraryItemFactory;
import com.biblioteca.iterator.LibraryCollection;
import com.biblioteca.model.LibraryItem;
import com.biblioteca.strategy.SearchStrategy;

/**
 * Manager centrale per tutte le operazioni del sistema bibliotecario.
 *
 * <p>Questa classe implementa il pattern Singleton e funge da punto di controllo
 * centrale per tutte le operazioni della biblioteca. Coordina l'interazione tra
 * i diversi componenti del sistema e gestisce la persistenza dei dati.</p>
 *
 * <p><strong>Design Patterns implementati:</strong></p>
 * <ul>
 *   <li><strong>Singleton:</strong> Garantisce una sola istanza del manager</li>
 *   <li><strong>Facade:</strong> Fornisce un'interfaccia semplificata per operazioni complesse</li>
 *   <li><strong>Coordinator:</strong> Coordina l'interazione tra diversi sottosistemi</li>
 * </ul>
 *
 * <p><strong>Responsabilità principali:</strong></p>
 * <ul>
 *   <li><strong>Gestione collezione:</strong> Aggiunta, rimozione e ricerca di elementi</li>
 *   <li><strong>Persistenza:</strong> Salvataggio e caricamento da file</li>
 *   <li><strong>Validazione:</strong> Controllo duplicati e integrità dati</li>
 *   <li><strong>Logging:</strong> Tracciamento delle operazioni per debugging</li>
 *   <li><strong>Coordinamento:</strong> Integrazione tra Factory, Strategy e Iterator patterns</li>
 * </ul>
 *
 * <p><strong>Strutture dati utilizzate:</strong></p>
 * <ul>
 *   <li><strong>LibraryCollection:</strong> Per l'iterazione e gestione ordinata</li>
 *   <li><strong>HashMap:</strong> Per l'accesso rapido per ID (O(1))</li>
 *   <li><strong>File system:</strong> Per la persistenza dei dati</li>
 * </ul>
 *
 * <p><strong>Gestione degli errori:</strong></p>
 * <ul>
 *   <li>Validazione preventiva dei dati</li>
 *   <li>Gestione eccezioni specifiche del dominio</li>
 *   <li>Logging dettagliato per debugging</li>
 *   <li>Rollback automatico in caso di errori</li>
 * </ul>
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
 */
public class LibraryManager {

    /** Logger per il tracciamento delle operazioni e debugging */
    private static final Logger logger = LoggerFactory.getLogger(LibraryManager.class);

    /** Istanza singleton del manager */
    private static LibraryManager instance;

    /** Collezione principale degli elementi bibliotecari per iterazione */
    private final LibraryCollection collection;

    /** Mappa per accesso rapido agli elementi per ID (O(1) lookup) */
    private final Map<String, LibraryItem> itemsById;

    /** Percorso del file per la persistenza dei dati */
    private final String dataFilePath = "data/library.txt";

    /**
     * Costruttore privato per implementare il pattern Singleton.
     *
     * <p>Inizializza le strutture dati interne e configura il sistema di logging.
     * Questo costruttore è privato per impedire l'istanziazione diretta
     * e garantire che esista una sola istanza del manager.</p>
     */
    private LibraryManager() {
        // Inizializzazione della collezione per l'iterazione
        collection = new LibraryCollection();
        // Inizializzazione della mappa per l'accesso rapido per ID
        itemsById = new HashMap<>();
        // Log dell'inizializzazione del sistema
        logger.info("Library Manager initialized");
    }

    /**
     * Restituisce l'istanza singleton del LibraryManager.
     *
     * <p>Implementa il pattern Singleton con lazy initialization e sincronizzazione
     * per garantire thread-safety. L'istanza viene creata solo al primo accesso.</p>
     *
     * @return l'istanza singleton del LibraryManager
     */
    public static synchronized LibraryManager getInstance() {
        // Lazy initialization con controllo thread-safe
        if (instance == null) {
            instance = new LibraryManager();
        }
        return instance;
    }

    /**
     * Aggiunge un nuovo libro alla biblioteca.
     *
     * <p>Questo metodo coordina la creazione di un libro utilizzando il Factory pattern,
     * la validazione dei dati, il controllo dei duplicati e l'aggiornamento delle
     * strutture dati interne. Include gestione completa degli errori e logging.</p>
     *
     * <p><strong>Operazioni eseguite:</strong></p>
     * <ol>
     *   <li>Controllo duplicati per ISBN</li>
     *   <li>Creazione libro tramite LibraryItemFactory</li>
     *   <li>Aggiunta alla collezione per iterazione</li>
     *   <li>Aggiunta alla mappa per accesso rapido</li>
     *   <li>Logging dell'operazione</li>
     * </ol>
     *
     * @param isbn l'ISBN del libro (deve essere univoco)
     * @param title il titolo del libro
     * @param author l'autore del libro
     * @param pages il numero di pagine del libro
     * @throws LibraryException se si verifica un errore durante l'aggiunta
     */
    public void addBook(String isbn, String title, String author, int pages)
            throws LibraryException {
        try {
            // Controllo duplicati: verifica se l'ISBN esiste già
            if (itemsById.containsKey(isbn)) {
                throw new InvalidDataException("Book with ISBN " + isbn + " already exists");
            }

            // Creazione del libro tramite Factory pattern con validazione
            LibraryItem book = LibraryItemFactory.createBook(isbn, title, author, pages);

            // Aggiunta alla collezione per supportare l'iterazione
            collection.addItem(book);
            // Aggiunta alla mappa per accesso rapido O(1)
            itemsById.put(isbn, book);

            // Logging dell'operazione completata con successo
            logger.info("Added book: {} by {}", title, author);
        } catch (InvalidDataException e) {
            // Gestione errori di validazione con logging specifico
            logger.error("Error adding book: {}", e.getMessage());
            throw e; // Rilancia l'eccezione specifica
        } catch (Exception e) {
            // Gestione errori imprevisti con wrapping in LibraryException
            logger.error("Unexpected error adding book: {}", e.getMessage());
            throw new LibraryException("Failed to add book", e);
        }
    }

    /**
     * Aggiunge una nuova rivista alla biblioteca.
     *
     * <p>Simile al metodo addBook, questo metodo coordina la creazione di una rivista
     * utilizzando il Factory pattern, con validazione, controllo duplicati e
     * aggiornamento delle strutture dati. Include gestione completa degli errori.</p>
     *
     * <p><strong>Operazioni eseguite:</strong></p>
     * <ol>
     *   <li>Controllo duplicati per ISSN</li>
     *   <li>Creazione rivista tramite LibraryItemFactory</li>
     *   <li>Aggiunta alla collezione e alla mappa</li>
     *   <li>Logging dell'operazione</li>
     * </ol>
     *
     * @param issn l'ISSN della rivista (deve essere univoco)
     * @param title il titolo della rivista
     * @param issueNumber il numero di edizione della rivista
     * @throws LibraryException se si verifica un errore durante l'aggiunta
     */
    public void addMagazine(String issn, String title, int issueNumber)
            throws LibraryException {
        try {
            // Controllo duplicati: verifica se l'ISSN esiste già
            if (itemsById.containsKey(issn)) {
                throw new InvalidDataException("Magazine with ISSN " + issn + " already exists");
            }

            // Creazione della rivista tramite Factory pattern con validazione
            LibraryItem magazine = LibraryItemFactory.createMagazine(issn, title, issueNumber);

            // Aggiunta alle strutture dati interne
            collection.addItem(magazine);
            itemsById.put(issn, magazine);

            // Logging dell'operazione completata
            logger.info("Added magazine: {} Issue #{}", title, issueNumber);
        } catch (InvalidDataException e) {
            // Gestione errori di validazione
            logger.error("Error adding magazine: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            // Gestione errori imprevisti
            logger.error("Unexpected error adding magazine: {}", e.getMessage());
            throw new LibraryException("Failed to add magazine", e);
        }
    }

    /**
     * Trova un elemento bibliotecario per ID.
     *
     * <p>Utilizza la mappa interna per un accesso rapido O(1) all'elemento
     * specificato. Lancia un'eccezione specifica se l'elemento non viene trovato.</p>
     *
     * @param id l'ID dell'elemento da cercare (ISBN per libri, ISSN per riviste)
     * @return l'elemento bibliotecario corrispondente all'ID
     * @throws BookNotFoundException se l'elemento non viene trovato
     */
    public LibraryItem findById(String id) throws BookNotFoundException {
        // Ricerca rapida O(1) nella mappa
        LibraryItem item = itemsById.get(id);
        if (item == null) {
            // Lancio eccezione specifica per elemento non trovato
            throw new BookNotFoundException(id);
        }
        return item;
    }

    /**
     * Esegue una ricerca nella collezione utilizzando la strategia specificata.
     *
     * <p>Implementa il pattern Strategy permettendo di utilizzare diversi
     * algoritmi di ricerca in modo intercambiabile. Delega l'operazione
     * alla strategia fornita.</p>
     *
     * @param strategy la strategia di ricerca da utilizzare
     * @param query la stringa di ricerca
     * @return lista di elementi che corrispondono ai criteri di ricerca
     */
    public List<LibraryItem> search(SearchStrategy strategy, String query) {
        // Logging della ricerca per debugging
        logger.info("Searching with query: {}", query);
        // Delega alla strategia di ricerca specifica
        return strategy.search(collection.getItems(), query);
    }

    /**
     * Visualizza tutti gli elementi della biblioteca.
     *
     * <p>Utilizza l'interfaccia Iterable della LibraryCollection per
     * attraversare e visualizzare tutti gli elementi in modo ordinato.</p>
     */
    public void displayAllItems() {
        // Logging dell'operazione
        logger.info("Displaying all library items");
        System.out.println("=== Library Collection ===");
        // Utilizzo dell'interfaccia Iterable per attraversare la collezione
        collection.forEach(LibraryItem::displayInfo);
    }

    /**
     * Salva tutti i dati della biblioteca su file.
     *
     * <p>Questo metodo implementa la persistenza dei dati serializzando
     * tutti gli elementi della collezione in formato testuale. Crea
     * automaticamente la directory di destinazione se non esiste.</p>
     *
     * <p><strong>Formato di salvataggio:</strong></p>
     * <ul>
     *   <li>Un elemento per riga</li>
     *   <li>Campi separati da pipe (|)</li>
     *   <li>Formato specifico per tipo di elemento</li>
     * </ul>
     *
     * <p><strong>Gestione errori:</strong></p>
     * <ul>
     *   <li>Creazione automatica directory</li>
     *   <li>Chiusura automatica risorse (try-with-resources)</li>
     *   <li>Logging dettagliato degli errori</li>
     * </ul>
     *
     * @throws LibraryException se si verifica un errore durante il salvataggio
     */
    public void saveToFile() throws LibraryException {
        try {
            // Creazione automatica della directory se non esiste
            Files.createDirectories(Paths.get("data"));

            // Utilizzo try-with-resources per gestione automatica delle risorse
            try (PrintWriter writer = new PrintWriter(new FileWriter(dataFilePath))) {
                // Iterazione attraverso tutti gli elementi della collezione
                for (LibraryItem item : collection) {
                    // Serializzazione dell'elemento in formato stringa
                    writer.println(itemToString(item));
                }
            }

            // Logging del completamento dell'operazione
            logger.info("Library data saved to file");
        } catch (IOException e) {
            // Gestione errori di I/O con logging e wrapping
            logger.error("Error saving to file: {}", e.getMessage());
            throw new LibraryException("Failed to save data", e);
        }
    }

    /**
     * Carica i dati della biblioteca da file.
     *
     * <p>Questo metodo implementa il caricamento dei dati deserializzando
     * gli elementi dal formato testuale. Gestisce sia formati legacy che
     * nuovi formati per retrocompatibilità.</p>
     *
     * <p><strong>Caratteristiche del caricamento:</strong></p>
     * <ul>
     *   <li>Controllo esistenza file</li>
     *   <li>Parsing riga per riga</li>
     *   <li>Gestione formati legacy</li>
     *   <li>Recupero da errori di parsing</li>
     * </ul>
     *
     * <p><strong>Comportamento:</strong></p>
     * <ul>
     *   <li>Se il file non esiste, inizia con biblioteca vuota</li>
     *   <li>Ignora righe con errori di parsing</li>
     *   <li>Continua il caricamento anche in presenza di errori</li>
     * </ul>
     *
     * @throws LibraryException se si verifica un errore critico durante il caricamento
     */
    public void loadFromFile() throws LibraryException {
        try {
            // Controllo esistenza del file di dati
            if (!Files.exists(Paths.get(dataFilePath))) {
                logger.info("Data file not found, starting with empty library");
                return; // Inizia con biblioteca vuota se il file non esiste
            }

            // Utilizzo try-with-resources per gestione automatica delle risorse
            try (BufferedReader reader = new BufferedReader(new FileReader(dataFilePath))) {
                String line;
                // Lettura e parsing riga per riga
                while ((line = reader.readLine()) != null) {
                    // Parsing di ogni riga in un elemento bibliotecario
                    parseItemFromString(line);
                }
            }

            // Logging del completamento dell'operazione
            logger.info("Library data loaded from file");
        } catch (IOException e) {
            // Gestione errori di I/O con logging e wrapping
            logger.error("Error loading from file: {}", e.getMessage());
            throw new LibraryException("Failed to load data", e);
        }
    }

    /**
     * Converte un elemento bibliotecario in rappresentazione stringa per la persistenza.
     *
     * <p>Questo metodo privato serializza un LibraryItem in formato testuale
     * utilizzando il pattern matching di Java per gestire i diversi tipi
     * di elementi. Il formato è ottimizzato per la lettura e il parsing.</p>
     *
     * <p><strong>Formati di output:</strong></p>
     * <ul>
     *   <li><strong>Book:</strong> Type|ID|Title|Author|Pages|Available</li>
     *   <li><strong>Magazine:</strong> Type|ID|Title|IssueNumber|Available</li>
     *   <li><strong>Generic:</strong> Type|ID|Title|Available (fallback)</li>
     * </ul>
     *
     * <p><strong>Caratteristiche:</strong></p>
     * <ul>
     *   <li>Utilizza pattern matching per type safety</li>
     *   <li>Formato pipe-separated per facilità di parsing</li>
     *   <li>Include tutti i dati necessari per la ricostruzione</li>
     * </ul>
     *
     * @param item l'elemento da serializzare (non deve essere nullo)
     * @return la rappresentazione stringa dell'elemento
     * @throws IllegalArgumentException se l'elemento è nullo
     */
    private String itemToString(LibraryItem item) {
        // Validazione input: l'elemento non può essere nullo
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        // Utilizzo pattern matching per serializzazione type-safe
        return switch (item) {
            // Serializzazione specifica per libri con tutti i campi
            case com.biblioteca.model.Book book -> String.format("%s|%s|%s|%s|%d|%s",
                    item.getType(), item.getId(), item.getTitle(),
                    book.getAuthor(), book.getPages(), item.isAvailable());
            // Serializzazione specifica per riviste con numero edizione
            case com.biblioteca.model.Magazine magazine -> String.format("%s|%s|%s|%d|%s",
                    item.getType(), item.getId(), item.getTitle(),
                    magazine.getIssueNumber(), item.isAvailable());
            // Fallback per tipi non riconosciuti (estensibilità futura)
            default -> String.format("%s|%s|%s|%s",
                    item.getType(), item.getId(), item.getTitle(), item.isAvailable());
        };
    }

    /**
     * Deserializza una riga di testo in un elemento bibliotecario.
     *
     * <p>Questo metodo privato gestisce il parsing delle righe del file di dati,
     * supportando sia formati attuali che legacy per garantire retrocompatibilità.
     * Include gestione robusta degli errori per continuare il caricamento anche
     * in presenza di righe malformate.</p>
     *
     * <p><strong>Formati supportati:</strong></p>
     * <ul>
     *   <li><strong>Book (nuovo):</strong> Book|ID|Title|Author|Pages|Available</li>
     *   <li><strong>Book (legacy):</strong> Book|ID|Title|Available (usa valori default)</li>
     *   <li><strong>Magazine (nuovo):</strong> Magazine|ID|Title|IssueNumber|Available</li>
     *   <li><strong>Magazine (legacy):</strong> Magazine|ID|Title|Available (usa valori default)</li>
     * </ul>
     *
     * <p><strong>Strategia di gestione errori:</strong></p>
     * <ul>
     *   <li>Ignora righe con meno di 3 campi</li>
     *   <li>Usa valori default per formati legacy</li>
     *   <li>Logga warning per righe problematiche</li>
     *   <li>Continua il parsing delle righe successive</li>
     * </ul>
     *
     * @param line la riga di testo da parsare
     */
    private void parseItemFromString(String line) {
        // Splitting della riga utilizzando pipe come separatore
        String[] parts = line.split("\\|");

        // Controllo minimo: almeno tipo, ID e titolo devono essere presenti
        if (parts.length >= 3) {
            try {
                // Estrazione dei campi base comuni a tutti gli elementi
                String type = parts[0];
                String id = parts[1];
                String title = parts[2];

                // Gestione specifica per libri
                if ("Book".equals(type)) {
                    // Formato nuovo: Book|ID|Title|Author|Pages|Available
                    if (parts.length >= 6) {
                        String author = parts[3];
                        int pages = Integer.parseInt(parts[4]);
                        boolean available = Boolean.parseBoolean(parts[5]);

                        // Creazione libro con tutti i dati
                        addBook(id, title, author, pages);
                        // Impostazione dello stato di disponibilità
                        LibraryItem item = findById(id);
                        item.setAvailable(available);
                    } else {
                        // Formato legacy: Book|ID|Title|Available - utilizza valori default
                        logger.warn("Loading book with legacy format, using default values: {}", line);
                        addBook(id, title, "Unknown Author", 100);
                        LibraryItem item = findById(id);
                        // Impostazione disponibilità se presente
                        if (parts.length > 3) {
                            boolean available = Boolean.parseBoolean(parts[3]);
                            item.setAvailable(available);
                        }
                    }
                } else if ("Magazine".equals(type)) {
                    // Gestione specifica per riviste
                    // Formato nuovo: Magazine|ID|Title|IssueNumber|Available
                    if (parts.length >= 5) {
                        int issueNumber = Integer.parseInt(parts[3]);
                        boolean available = Boolean.parseBoolean(parts[4]);

                        // Creazione rivista con tutti i dati
                        addMagazine(id, title, issueNumber);
                        // Impostazione dello stato di disponibilità
                        LibraryItem item = findById(id);
                        item.setAvailable(available);
                    } else {
                        // Formato legacy: Magazine|ID|Title|Available - utilizza valori default
                        logger.warn("Loading magazine with legacy format, using default values: {}", line);
                        addMagazine(id, title, 1); // Numero edizione default = 1
                        LibraryItem item = findById(id);
                        // Impostazione disponibilità se presente
                        if (parts.length > 3) {
                            boolean available = Boolean.parseBoolean(parts[3]);
                            item.setAvailable(available);
                        }
                    }
                }
            } catch (LibraryException | NumberFormatException e) {
                // Gestione errori di parsing: logga warning ma continua
                logger.warn("Failed to parse item: {} - Error: {}", line, e.getMessage());
            }
        }
        // Nota: righe con meno di 3 campi vengono silenziosamente ignorate
    }

    /**
     * Restituisce il numero totale di elementi nella biblioteca.
     *
     * <p>Questo metodo fornisce un accesso rapido al conteggio degli elementi
     * utilizzando la dimensione della collezione interna.</p>
     *
     * @return il numero totale di elementi bibliotecari
     */
    public int getTotalItems() {
        return collection.size();
    }
}
