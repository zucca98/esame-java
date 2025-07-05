package com.biblioteca.exceptions;

/**
 * Eccezione base per tutte le operazioni del sistema bibliotecario.
 *
 * <p>Questa classe rappresenta l'eccezione radice della gerarchia di eccezioni
 * del sistema biblioteca. Implementa il pattern Exception Shielding per
 * nascondere i dettagli interni degli errori e fornire messaggi di errore
 * appropriati agli utenti finali.</p>
 *
 * <p><strong>Design Patterns implementati:</strong></p>
 * <ul>
 *   <li><strong>Exception Shielding:</strong> Nasconde i dettagli tecnici interni</li>
 *   <li><strong>Exception Hierarchy:</strong> Classe base per eccezioni specifiche</li>
 * </ul>
 *
 * <p><strong>Strategia di gestione degli errori:</strong></p>
 * <ul>
 *   <li>Fornisce messaggi di errore user-friendly</li>
 *   <li>Mantiene la causa originale per il debugging</li>
 *   <li>Permette la gestione centralizzata degli errori</li>
 *   <li>Facilita il logging e il monitoraggio</li>
 * </ul>
 *
 * <p>Tutte le eccezioni specifiche del dominio biblioteca dovrebbero
 * estendere questa classe per mantenere una gerarchia coerente.</p>
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
 */
public class LibraryException extends Exception {

    /**
     * Costruisce una nuova LibraryException con il messaggio specificato.
     *
     * <p>Questo costruttore è utilizzato quando si vuole creare un'eccezione
     * con un messaggio descrittivo senza una causa sottostante specifica.</p>
     *
     * @param message il messaggio di errore descrittivo (può essere nullo)
     */
    public LibraryException(String message) {
        super(message);
    }

    /**
     * Costruisce una nuova LibraryException con messaggio e causa specificati.
     *
     * <p>Questo costruttore è utilizzato quando si vuole wrappare un'altra
     * eccezione mantenendo sia un messaggio descrittivo che la causa originale.
     * È particolarmente utile per implementare l'Exception Shielding.</p>
     *
     * @param message il messaggio di errore descrittivo (può essere nullo)
     * @param cause l'eccezione che ha causato questo errore (può essere nulla)
     */
    public LibraryException(String message, Throwable cause) {
        super(message, cause);
    }
}