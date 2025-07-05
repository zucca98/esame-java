
package com.biblioteca.exceptions;

/**
 * Eccezione lanciata quando un libro non viene trovato nella biblioteca.
 *
 * <p>Questa eccezione specializzata estende {@link LibraryException} e viene
 * utilizzata specificamente per segnalare che un libro richiesto non è presente
 * nella collezione della biblioteca. È tipicamente lanciata durante operazioni
 * di ricerca, prestito o consultazione quando l'ISBN fornito non corrisponde
 * a nessun libro nel sistema.</p>
 *
 * <p><strong>Scenari di utilizzo:</strong></p>
 * <ul>
 *   <li>Ricerca di un libro per ISBN inesistente</li>
 *   <li>Tentativo di prestito di un libro non catalogato</li>
 *   <li>Operazioni di aggiornamento su libri rimossi</li>
 *   <li>Accesso a libri attraverso riferimenti obsoleti</li>
 * </ul>
 *
 * <p><strong>Gestione dell'errore:</strong></p>
 * <ul>
 *   <li>Fornisce un messaggio di errore specifico con l'ISBN</li>
 *   <li>Facilita la distinzione da altri tipi di errori di ricerca</li>
 *   <li>Permette gestione specifica per elementi non trovati</li>
 * </ul>
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
 */
public class BookNotFoundException extends LibraryException {

    /**
     * Costruisce una nuova BookNotFoundException per l'ISBN specificato.
     *
     * <p>Crea un'eccezione con un messaggio descrittivo che include l'ISBN
     * del libro non trovato, facilitando l'identificazione del problema
     * specifico durante il debugging e la gestione degli errori.</p>
     *
     * @param isbn l'ISBN del libro che non è stato trovato nella biblioteca
     */
    public BookNotFoundException(String isbn) {
        // Messaggio specifico che include l'ISBN per facilitare il debugging
        super("Book with ISBN " + isbn + " not found");
    }
}
