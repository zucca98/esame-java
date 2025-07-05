package com.biblioteca;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.biblioteca.exceptions.LibraryException;
import com.biblioteca.manager.LibraryManager;
import com.biblioteca.model.LibraryItem;
import com.biblioteca.strategy.IdSearchStrategy;
import com.biblioteca.strategy.SearchStrategy;
import com.biblioteca.strategy.TitleSearchStrategy;

/**
 * Classe principale che dimostra il Sistema di Gestione Biblioteca.
 *
 * Questa classe rappresenta il punto di ingresso dell'applicazione e
 * dimostra l'utilizzo di tutti i design pattern e le tecnologie richieste
 * nel sistema bibliotecario. Fornisce sia una dimostrazione automatica
 * che un'interfaccia interattiva per l'utente.
 *
 * Funzionalità principali:
 *   Inizializzazione sistema: Caricamento dati e configurazione
 *   Dimostrazione automatica: Esempi di utilizzo dei pattern
 *   Menu interattivo: Interfaccia utente per operazioni manuali
 *   Gestione errori:Logging e recupero da errori
 *
 *
 * Design Patterns dimostrati:
 *
 * Singleton: LibraryManager instance
 * Factory: Creazione di libri e riviste
 * Strategy: Diverse strategie di ricerca
 * Iterator: Attraversamento collezioni
 * 
 *
 * Tecnologie utilizzate:
 * 
 *   SLF4J: Sistema di logging
 *   Scanner: Input utente
 *   Try-with-resources: Gestione risorse
 *   Exception handling: Gestione robusta degli errori
 * 
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
 */
public class Main {

    /** Logger per il tracciamento delle operazioni dell'applicazione */
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * Metodo principale dell'applicazione.
     *
     *Questo metodo coordina l'avvio dell'intero sistema bibliotecario,
     * gestendo l'inizializzazione, la dimostrazione delle funzionalità
     * e l'interfaccia utente interattiva. Include gestione completa
     * degli errori e logging delle operazioni.
     *
     * Flusso di esecuzione:
     * 
     *     Inizializzazione del sistema di logging
     *     Creazione dell'istanza LibraryManager (Singleton)
     *     Caricamento dati esistenti da file
     *     Esecuzione dimostrazione automatica
     *     Avvio menu interattivo per l'utente
     *     Gestione errori e cleanup finale
     *
     *
     * @param args argomenti della riga di comando (non utilizzati)
     */
    public static void main(String[] args) {
        // Logging dell'avvio del sistema
        logger.info("Starting Library Management System");

        // Utilizzo try-with-resources per gestione automatica dello Scanner
        try (Scanner scanner = new Scanner(System.in)) {
            // Ottenimento dell'istanza singleton del manager
            LibraryManager manager = LibraryManager.getInstance();

            // Caricamento dei dati esistenti dal file di persistenza
            manager.loadFromFile();

            // Esecuzione della dimostrazione automatica del sistema
            demonstrateSystem(manager);

            // Avvio del menu interattivo per l'utente
            runInteractiveMenu(manager, scanner);

        } catch (Exception e) {
            // Gestione errori critici con logging e notifica utente
            logger.error("System error: {}", e.getMessage());
            System.err.println("System error occurred. Please check logs.");
        } finally {
            // Logging della terminazione del sistema
            logger.info("Library Management System stopped");
        }
    }

    /**
     * Dimostra le funzionalità principali del sistema bibliotecario.
     *
     * Questo metodo esegue una dimostrazione automatica delle capacità
     * del sistema, mostrando l'utilizzo dei design pattern implementati
     * e delle funzionalità principali. Include gestione degli errori
     * per garantire che la dimostrazione non interrompa l'applicazione.
     *
     * Operazioni dimostrate:
     * 
     * Aggiunta di libri e riviste (Factory Pattern)
     * Strategie di ricerca (Strategy Pattern)
     * Visualizzazione collezione (Iterator Pattern)
     * Persistenza dati (File I/O)
     * 
     *
     * @param manager l'istanza del LibraryManager da utilizzare per la demo
     */
    private static void demonstrateSystem(LibraryManager manager) {
        System.out.println("=== Library Management System Demo ===\n");

        try {
            // Aggiunta di dati di esempio per dimostrare il Factory Pattern
            manager.addBook("978-0134685991", "Effective Java", "Joshua Bloch", 416);
            manager.addBook("978-0596009205", "Head First Design Patterns", "Eric Freeman", 694);
            manager.addMagazine("1234-5678", "Java Magazine", 45);

            System.out.println("Sample data added successfully!\n");

            // Dimostrazione delle strategie di ricerca (Strategy Pattern)
            demonstrateSearchStrategies(manager);

            // Visualizzazione di tutti gli elementi (Iterator Pattern)
            manager.displayAllItems();

            // Salvataggio dei dati per dimostrare la persistenza
            manager.saveToFile();

        } catch (LibraryException e) {
            // Gestione errori durante la dimostrazione
            System.err.println("Demo error: " + e.getMessage());
        }
    }

    /**
     * Dimostra l'utilizzo delle diverse strategie di ricerca.
     *
     * Questo metodo mostra il funzionamento del Strategy Pattern
     * implementato nel sistema, utilizzando diverse strategie di ricerca
     * in modo intercambiabile per trovare elementi nella biblioteca.
     *
     *Strategie dimostrate:
     *   TitleSearchStrategy: Ricerca per titolo
     *   IdSearchStrategy: Ricerca per ID/ISBN
     * 
     *
     * @param manager l'istanza del LibraryManager per eseguire le ricerche
     */
    private static void demonstrateSearchStrategies(LibraryManager manager) {
        System.out.println("=== Search Demonstration ===");

        // Dimostrazione ricerca per titolo utilizzando TitleSearchStrategy
        var titleResults = manager.search(new TitleSearchStrategy(), "Java");
        System.out.println("Title search for 'Java' found " + titleResults.size() + " items:");
        titleResults.forEach(item -> System.out.println("  - " + item.getTitle()));

        // Dimostrazione ricerca per ID utilizzando IdSearchStrategy
        var idResults = manager.search(new IdSearchStrategy(), "978-0134685991");
        System.out.println("\nID search found " + idResults.size() + " items:");
        idResults.forEach(item -> System.out.println("  - " + item.getTitle()));

        System.out.println();
    }

    /**
     * Esegue il menu interattivo per l'utente.
     *
     * Questo metodo implementa l'interfaccia utente principale dell'applicazione,
     * fornendo un menu a ciclo continuo che permette all'utente di eseguire
     * tutte le operazioni disponibili nel sistema bibliotecario.
     *
     *Funzionalità del menu:
     * 
     *   Aggiunta di libri e riviste
     *   Ricerca con diverse strategie
     *   Visualizzazione della collezione
     *   Salvataggio dati
     *   Statistiche del sistema
     *
     *
     * Gestione errori:
     * 
     * Cattura eccezioni LibraryException
     * Continua l'esecuzione dopo errori
     * Fornisce feedback all'utente<
     * 
     *
     * @param manager l'istanza del LibraryManager per le operazioni
     * @param scanner lo Scanner per l'input dell'utente
     */
    private static void runInteractiveMenu(LibraryManager manager, Scanner scanner) {
        // Ciclo principale del menu interattivo
        while (true) {
            // Visualizzazione del menu delle opzioni
            displayMenu();
            String choice = getValidatedInput("Enter your choice: ", scanner);

            try {
                // Gestione delle scelte dell'utente con switch expression
                switch (choice) {
                    case "1" -> addNewBook(manager, scanner);
                    case "2" -> addNewMagazine(manager, scanner);
                    case "3" -> searchItems(manager, scanner);
                    case "4" -> manager.displayAllItems();
                    case "5" -> {
                        // Salvataggio dati con feedback all'utente
                        manager.saveToFile();
                        System.out.println("Data saved successfully!");
                    }
                    case "6" -> {
                        // Visualizzazione statistiche del sistema
                        System.out.println("Total items: " + manager.getTotalItems());
                    }
                    case "0" -> {
                        // Uscita dal programma
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (LibraryException e) {
                // Gestione errori con continuazione dell'esecuzione
                System.err.println("Error: " + e.getMessage());
            }

            // Pausa per permettere all'utente di leggere l'output
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    /**
     * Visualizza il menu principale delle opzioni disponibili.
     *
     * Questo metodo mostra all'utente tutte le operazioni disponibili
     * nel sistema bibliotecario in un formato chiaro e numerato.
     */
    private static void displayMenu() {
        System.out.println("\n=== Library Management System ===");
        System.out.println("1. Add Book");
        System.out.println("2. Add Magazine");
        System.out.println("3. Search Items");
        System.out.println("4. Display All Items");
        System.out.println("5. Save Data");
        System.out.println("6. Show Statistics");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }

    /**
     * Gestisce l'aggiunta di un nuovo libro tramite input utente.
     *
     * Questo metodo raccoglie tutti i dati necessari per creare un nuovo libro
     * attraverso l'interfaccia utente, validando ogni input e delegando
     * la creazione al LibraryManager.
     * 
     * Dati raccolti:
     *
     * ISBN (validato per non essere vuoto)
     * Titolo (validato per non essere vuoto)
     * Autore (validato per non essere vuoto)
     * Numero di pagine (validato per essere positivo)
     *
     *
     * @param manager l'istanza del LibraryManager per aggiungere il libro
     * @param scanner lo Scanner per l'input dell'utente
     * @throws LibraryException se si verifica un errore durante l'aggiunta
     */
    private static void addNewBook(LibraryManager manager, Scanner scanner) throws LibraryException {
        System.out.println("\n=== Add New Book ===");

        // Raccolta e validazione di tutti i dati del libro
        String isbn = getValidatedInput("ISBN: ", scanner);
        String title = getValidatedInput("Title: ", scanner);
        String author = getValidatedInput("Author: ", scanner);
        int pages = getValidatedIntInput("Pages: ", scanner);

        // Delega al manager per la creazione (Factory Pattern + validazione)
        manager.addBook(isbn, title, author, pages);
        System.out.println("Book added successfully!");
    }

    /**
     * Gestisce l'aggiunta di una nuova rivista tramite input utente.
     *
     * Simile al metodo per i libri, raccoglie i dati specifici per le riviste
     * e delega la creazione al LibraryManager.
     *
     *Dati raccolti:
     * 
     * ISSN (validato per non essere vuoto)
     * Titolo (validato per non essere vuoto)
     * Numero di edizione (validato per essere positivo)
     *
     *
     * @param manager l'istanza del LibraryManager per aggiungere la rivista
     * @param scanner lo Scanner per l'input dell'utente
     * @throws LibraryException se si verifica un errore durante l'aggiunta
     */
    private static void addNewMagazine(LibraryManager manager, Scanner scanner) throws LibraryException {
        System.out.println("\n=== Add New Magazine ===");

        // Raccolta e validazione di tutti i dati della rivista
        String issn = getValidatedInput("ISSN: ", scanner);
        String title = getValidatedInput("Title: ", scanner);
        int issueNumber = getValidatedIntInput("Issue Number: ", scanner);

        // Delega al manager per la creazione (Factory Pattern + validazione)
        manager.addMagazine(issn, title, issueNumber);
        System.out.println("Magazine added successfully!");
    }

    /**
     * Gestisce la ricerca di elementi nella biblioteca tramite interfaccia utente.
     *
     * Questo metodo implementa l'interfaccia utente per la funzionalità di ricerca,
     * dimostrando l'utilizzo del Strategy Pattern permettendo all'utente di scegliere
     * tra diverse strategie di ricerca. Include help contestuale e gestione
     * intelligente dei risultati.
     *
     * Strategie di ricerca disponibili:
     * 
     * Ricerca per titolo:</strong> Matching parziale case-insensitive
     * Ricerca per ID:</strong> Matching esatto o parziale con cifre
     *
     * Caratteristiche avanzate:
     * 
     * Help contestuale per ricerca ID
     * Feedback intelligente per risultati vuoti
     * Validazione lunghezza minima per ricerca parziale
     *
     *
     * @param manager l'istanza del LibraryManager per eseguire la ricerca
     * @param scanner lo Scanner per l'input dell'utente
     */
    private static void searchItems(LibraryManager manager, Scanner scanner) {
        System.out.println("\n=== Search Items ===");
        System.out.println("1. Search by Title (partial matching supported)");
        System.out.println("2. Search by ID (ISBN/ISSN - partial matching with 3+ digits)");
        String choice = getValidatedInput("Search type: ", scanner);

        // Fornisce help contestuale per la ricerca ID
        if ("2".equals(choice)) {
            System.out.println("\nID Search Info:");
            System.out.println("- Enter complete ISBN/ISSN for exact match");
            System.out.println("- Enter at least 3 digits for partial matching");
            System.out.println("- Example: '134' will find ISBN '978-0134685991'");
        }

        String query = getValidatedInput("Search query: ", scanner);

        // Utilizzo del Strategy Pattern per selezione dinamica dell'algoritmo
        SearchStrategy strategy = switch (choice) {
            case "1" -> new TitleSearchStrategy();
            case "2" -> new IdSearchStrategy();
            default -> {
                System.out.println("Invalid choice, using title search");
                yield new TitleSearchStrategy();
            }
        };

        // Esecuzione della ricerca con la strategia selezionata
        var results = manager.search(strategy, query);

        // Gestione intelligente dei risultati vuoti per ricerca ID
        if ("2".equals(choice) && results.isEmpty() && query.trim().length() > 0) {
            // Estrazione delle cifre per verificare se l'utente ha fornito abbastanza dati
            String digitsOnly = query.replaceAll("[^0-9]", "");
            if (digitsOnly.length() < 3) {
                System.out.println("No items found. For partial ID search, please enter at least 3 digits.");
            } else {
                System.out.println("No items found matching the provided ID or digits.");
            }
        } else {
            System.out.println("Found " + results.size() + " items:");
        }

        // Visualizzazione dei risultati utilizzando method reference
        results.forEach(LibraryItem::displayInfo);
    }

    /**
     * Ottiene input validato dall'utente garantendo che non sia vuoto.
     *
     * Questo metodo di utilità implementa la validazione di base per l'input
     * dell'utente, continuando a richiedere input fino a quando non viene
     * fornita una stringa non vuota. Rimuove automaticamente spazi iniziali
     * e finali.
     *
     * Validazioni eseguite:
     * 
     * Rimozione spazi iniziali e finali (trim)
     * Controllo che l'input non sia vuoto
     * Ripetizione richiesta fino a input valido
     * 
     *
     * @param prompt il messaggio da mostrare all'utente
     * @param scanner lo Scanner per leggere l'input
     * @return la stringa validata (non vuota e trimmed)
     */
    private static String getValidatedInput(String prompt, Scanner scanner) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();

        // Ciclo di validazione: continua fino a input valido
        while (input.isEmpty()) {
            System.out.print("Input cannot be empty. " + prompt);
            input = scanner.nextLine().trim();
        }
        return input;
    }

    /**
     * Ottiene input numerico intero validato dall'utente.
     *
     * Questo metodo di utilità gestisce l'input di numeri interi con
     * validazione completa, includendo controllo del formato e del valore.
     * Continua a richiedere input fino a quando non viene fornito un
     * numero intero positivo valido.
     *
     * Validazioni eseguite:
     * 
     *   Parsing corretto come intero
     *   Valore positivo (maggiore di zero)
     *   Gestione eccezioni di formato
     *   Ripetizione richiesta fino a input valido
     *
     *
     * @param prompt il messaggio da mostrare all'utente
     * @param scanner lo Scanner per leggere l'input
     * @return il numero intero validato (positivo)
     */
    private static int getValidatedIntInput(String prompt, Scanner scanner) {
        // Ciclo di validazione per input numerico
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);

                // Controllo che il valore sia positivo
                if (value <= 0) {
                    System.out.println("Please enter a positive number.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                // Gestione errori di formato numerico
                System.out.println("Please enter a valid number.");
            }
        }
    }
}