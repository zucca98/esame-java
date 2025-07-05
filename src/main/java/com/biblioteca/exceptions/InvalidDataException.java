package com.biblioteca.exceptions;

/**
 * Eccezione lanciata quando vengono forniti dati non validi al sistema.
 *
 * <p>Questa eccezione specializzata estende {@link LibraryException} e viene
 * utilizzata per segnalare errori di validazione dei dati di input. È tipicamente
 * lanciata quando i dati forniti non rispettano i criteri di validazione
 * definiti dal sistema (es. campi obbligatori mancanti, formati non validi, ecc.).</p>
 *
 * <p><strong>Casi d'uso tipici:</strong></p>
 * <ul>
 *   <li>Validazione di ISBN/ISSN non validi</li>
 *   <li>Titoli o autori vuoti o nulli</li>
 *   <li>Numeri di pagina o edizione negativi</li>
 *   <li>Formati di data non corretti</li>
 *   <li>Parametri di ricerca non validi</li>
 * </ul>
 *
 * <p><strong>Strategia di gestione:</strong></p>
 * <ul>
 *   <li>Fornisce messaggi di errore specifici per la validazione</li>
 *   <li>Prefissa automaticamente "Invalid data:" ai messaggi</li>
 *   <li>Facilita la distinzione tra errori di validazione e altri errori</li>
 * </ul>
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
 */
public class InvalidDataException extends LibraryException {

    /**
     * Costruisce una nuova InvalidDataException con il messaggio specificato.
     *
     * <p>Il messaggio viene automaticamente prefissato con "Invalid data: "
     * per fornire un contesto chiaro sull'origine dell'errore.</p>
     *
     * @param message il messaggio descrittivo dell'errore di validazione
     */
    public InvalidDataException(String message) {
        // Prefisso automatico per identificare chiaramente errori di validazione
        super("Invalid data: " + message);
    }
}