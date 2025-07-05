package com.biblioteca.model;

/**
 * Rappresenta un libro nel sistema bibliotecario.
 *
 * <p>Questa classe implementa l'interfaccia {@link LibraryItem} e rappresenta
 * un libro con le sue caratteristiche specifiche come ISBN, autore e numero di pagine.
 * Utilizza il pattern Builder per garantire una costruzione robusta e flessibile
 * degli oggetti Book.</p>
 *
 * <p><strong>Design Patterns implementati:</strong></p>
 * <ul>
 *   <li><strong>Builder Pattern:</strong> Attraverso la classe interna BookBuilder</li>
 *   <li><strong>Immutable Object:</strong> I campi principali sono final e immutabili</li>
 * </ul>
 *
 * <p><strong>Caratteristiche:</strong></p>
 * <ul>
 *   <li>ISBN come identificativo univoco</li>
 *   <li>Informazioni bibliografiche complete (titolo, autore, pagine)</li>
 *   <li>Stato di disponibilità modificabile</li>
 *   <li>Validazione dei dati obbligatori durante la costruzione</li>
 * </ul>
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
 */
public class Book implements LibraryItem {

    /** L'ISBN del libro, utilizzato come identificativo univoco */
    private final String isbn;

    /** Il titolo del libro */
    private final String title;

    /** L'autore del libro */
    private final String author;

    /** Il numero di pagine del libro */
    private final int pages;

    /** Indica se il libro è attualmente disponibile per il prestito */
    private boolean available;

    /**
     * Costruttore privato che utilizza il pattern Builder.
     *
     * <p>Questo costruttore è accessibile solo attraverso il BookBuilder,
     * garantendo che tutti i campi obbligatori siano impostati correttamente
     * prima della creazione dell'oggetto.</p>
     *
     * @param builder l'istanza di BookBuilder contenente i dati del libro
     */
    private Book(BookBuilder builder) {
        // Inizializzazione dei campi immutabili dal builder
        this.isbn = builder.isbn;
        this.title = builder.title;
        this.author = builder.author;
        this.pages = builder.pages;
        // Per default, ogni nuovo libro è disponibile
        this.available = true;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Per i libri, l'ID corrisponde all'ISBN (International Standard Book Number).</p>
     */
    @Override
    public String getId() {
        return isbn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * Restituisce l'autore del libro.
     *
     * @return il nome dell'autore del libro
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Restituisce il numero di pagine del libro.
     *
     * @return il numero di pagine del libro
     */
    public int getPages() {
        return pages;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Per i libri, il tipo è sempre "Book".</p>
     */
    @Override
    public String getType() {
        return "Book";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAvailable() {
        return available;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Visualizza le informazioni complete del libro in formato leggibile,
     * includendo titolo, autore, ISBN, numero di pagine e stato di disponibilità.</p>
     */
    @Override
    public void displayInfo() {
        // Formattazione delle informazioni del libro per la visualizzazione
        System.out.printf("Book: %s by %s (ISBN: %s, Pages: %d, Available: %s)%n",
                title, author, isbn, pages, available ? "Yes" : "No");
    }

    /**
     * Implementazione del pattern Builder per la costruzione di oggetti Book.
     *
     * <p>Questa classe interna statica implementa il pattern Builder per permettere
     * la costruzione flessibile e robusta di oggetti Book. Il pattern Builder è
     * particolarmente utile quando si hanno molti parametri di costruzione e si
     * vuole garantire la validazione dei dati.</p>
     *
     * <p><strong>Vantaggi del pattern Builder:</strong></p>
     * <ul>
     *   <li>Costruzione step-by-step dell'oggetto</li>
     *   <li>Validazione centralizzata nel metodo build()</li>
     *   <li>Codice più leggibile e manutenibile</li>
     *   <li>Immutabilità dell'oggetto finale</li>
     * </ul>
     *
     * @author Sistema Biblioteca
     * @version 1.0
     */
    public static class BookBuilder {

        /** L'ISBN del libro da costruire */
        private String isbn;

        /** Il titolo del libro da costruire */
        private String title;

        /** L'autore del libro da costruire */
        private String author;

        /** Il numero di pagine del libro da costruire */
        private int pages;

        /**
         * Imposta l'ISBN del libro.
         *
         * @param isbn l'ISBN del libro (deve essere non nullo)
         * @return questa istanza di BookBuilder per il method chaining
         */
        public BookBuilder setIsbn(String isbn) {
            this.isbn = isbn;
            return this;
        }

        /**
         * Imposta il titolo del libro.
         *
         * @param title il titolo del libro (deve essere non nullo)
         * @return questa istanza di BookBuilder per il method chaining
         */
        public BookBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Imposta l'autore del libro.
         *
         * @param author l'autore del libro (deve essere non nullo)
         * @return questa istanza di BookBuilder per il method chaining
         */
        public BookBuilder setAuthor(String author) {
            this.author = author;
            return this;
        }

        /**
         * Imposta il numero di pagine del libro.
         *
         * @param pages il numero di pagine del libro (deve essere positivo)
         * @return questa istanza di BookBuilder per il method chaining
         */
        public BookBuilder setPages(int pages) {
            this.pages = pages;
            return this;
        }

        /**
         * Costruisce l'oggetto Book finale con validazione dei dati.
         *
         * <p>Questo metodo esegue la validazione di tutti i campi obbligatori
         * prima di creare l'istanza di Book. Se qualche campo obbligatorio
         * è mancante, viene lanciata un'eccezione.</p>
         *
         * @return una nuova istanza di Book completamente inizializzata
         * @throws IllegalArgumentException se ISBN, titolo o autore sono nulli
         */
        public Book build() {
            // Validazione dei campi obbligatori prima della costruzione
            if (isbn == null || title == null || author == null) {
                throw new IllegalArgumentException("ISBN, title, and author are required");
            }
            // Creazione dell'oggetto Book immutabile
            return new Book(this);
        }
    }
}
