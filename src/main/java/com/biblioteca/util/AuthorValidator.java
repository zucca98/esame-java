package com.biblioteca.util;

import java.util.regex.Pattern;

/**
 * Classe di utilità per la validazione dei nomi degli autori.
 *
 * <p>Questa classe fornisce metodi statici per validare che i nomi degli autori
 * contengano testo significativo piuttosto che solo identificatori numerici.
 * Implementa regole di business specifiche per garantire la qualità dei dati
 * degli autori nel sistema bibliotecario.</p>
 *
 * <p><strong>Regole di validazione implementate:</strong></p>
 * <ul>
 *   <li><strong>Non vuoto:</strong> Il nome non può essere nullo o vuoto</li>
 *   <li><strong>Contenuto alfabetico:</strong> Deve contenere almeno un carattere alfabetico</li>
 *   <li><strong>Non puramente numerico:</strong> Non può consistere solo di numeri e separatori</li>
 *   <li><strong>Significativo:</strong> Deve rappresentare un nome reale, non un codice</li>
 * </ul>
 *
 * <p><strong>Utilizzo tipico:</strong></p>
 * <ul>
 *   <li>Validazione durante l'inserimento di nuovi libri</li>
 *   <li>Controllo qualità dati durante l'importazione</li>
 *   <li>Prevenzione di errori di input dell'utente</li>
 * </ul>
 *
 * <p><strong>Pattern regex utilizzati:</strong></p>
 * <ul>
 *   <li>Rilevamento di stringhe puramente numeriche</li>
 *   <li>Verifica presenza di caratteri alfabetici</li>
 *   <li>Gestione di separatori comuni</li>
 * </ul>
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
 */
public class AuthorValidator {

    /**
     * Pattern per identificare stringhe che contengono solo cifre, spazi, trattini,
     * punti e altri separatori non alfabetici
     */
    private static final Pattern NUMERIC_ONLY_PATTERN = Pattern.compile("^[0-9\\s\\-\\.\\,\\(\\)\\[\\]\\{\\}\\_\\+\\=\\|\\\\\\/@#$%^&*~`]+$");

    /** Pattern per verificare se una stringa contiene almeno un carattere alfabetico */
    private static final Pattern CONTAINS_LETTER_PATTERN = Pattern.compile(".*[a-zA-Z].*");

    /**
     * Valida che un nome di autore non sia puramente numerico.
     *
     * <p>Questo metodo esegue la validazione principale per i nomi degli autori,
     * verificando che contengano almeno un carattere alfabetico. È il metodo
     * di validazione primario utilizzato dal sistema.</p>
     *
     * <p><strong>Criteri di validazione:</strong></p>
     * <ul>
     *   <li>Non nullo e non vuoto dopo trim</li>
     *   <li>Contiene almeno un carattere alfabetico (a-z, A-Z)</li>
     *   <li>Non consiste solo di numeri e separatori</li>
     * </ul>
     *
     * @param author il nome dell'autore da validare
     * @return {@code true} se il nome è valido (contiene caratteri alfabetici),
     *         {@code false} altrimenti
     */
    public static boolean isValidAuthor(String author) {
        // Controllo preliminare: nome nullo o vuoto
        if (author == null || author.trim().isEmpty()) {
            return false;
        }

        // Pulizia del nome rimuovendo spazi iniziali e finali
        String trimmedAuthor = author.trim();

        // Verifica che il nome contenga almeno un carattere alfabetico
        return CONTAINS_LETTER_PATTERN.matcher(trimmedAuthor).matches();
    }

    /**
     * Valida un nome di autore e lancia un'eccezione se non valido.
     *
     * <p>Questo metodo fornisce validazione con eccezioni per integrazione
     * con codice che preferisce gestire errori tramite eccezioni piuttosto
     * che valori di ritorno booleani. Fornisce messaggi di errore dettagliati.</p>
     *
     * <p><strong>Tipi di errori rilevati:</strong></p>
     * <ul>
     *   <li>Nome vuoto o nullo</li>
     *   <li>Nome puramente numerico con separatori</li>
     *   <li>Nome senza caratteri alfabetici</li>
     * </ul>
     *
     * @param author il nome dell'autore da validare
     * @throws IllegalArgumentException se il nome dell'autore non è valido
     */
    public static void validateAuthor(String author) {
        // Utilizzo del metodo di validazione principale
        if (!isValidAuthor(author)) {
            // Gestione specifica per nomi vuoti
            if (author == null || author.trim().isEmpty()) {
                throw new IllegalArgumentException("Author name cannot be empty");
            }

            String trimmedAuthor = author.trim();

            // Controllo specifico per nomi puramente numerici/separatori
            if (NUMERIC_ONLY_PATTERN.matcher(trimmedAuthor).matches()) {
                throw new IllegalArgumentException(
                    "Author name cannot consist entirely of numbers and separators. " +
                    "Please enter a valid author name with alphabetic characters."
                );
            }

            // Fallback per altri casi non specificamente identificati
            throw new IllegalArgumentException(
                "Author name must contain at least some alphabetic characters"
            );
        }
    }

    /**
     * Verifica se una stringa consiste interamente di caratteri numerici e separatori comuni.
     *
     * <p>Questo metodo di utilità identifica stringhe che potrebbero essere
     * erroneamente inserite come nomi di autori ma che in realtà rappresentano
     * codici, numeri di telefono, o altri identificatori numerici.</p>
     *
     * <p><strong>Caratteri considerati "numerici":</strong></p>
     * <ul>
     *   <li>Cifre (0-9)</li>
     *   <li>Spazi e separatori comuni (-, ., ,)</li>
     *   <li>Parentesi e simboli di raggruppamento</li>
     *   <li>Altri simboli non alfabetici comuni</li>
     * </ul>
     *
     * @param text il testo da controllare
     * @return {@code true} se il testo è puramente numerico con separatori,
     *         {@code false} altrimenti
     */
    public static boolean isPurelyNumeric(String text) {
        // Controllo preliminare per testo nullo o vuoto
        if (text == null || text.trim().isEmpty()) {
            return false;
        }

        // Applicazione del pattern per identificare contenuto puramente numerico
        return NUMERIC_ONLY_PATTERN.matcher(text.trim()).matches();
    }

    /**
     * Genera un messaggio di errore user-friendly per nomi di autori non validi.
     *
     * <p>Questo metodo analizza il tipo specifico di errore nel nome dell'autore
     * e restituisce un messaggio di errore appropriato e descrittivo per
     * guidare l'utente nella correzione dell'input.</p>
     *
     * <p><strong>Tipi di messaggi generati:</strong></p>
     * <ul>
     *   <li>Messaggio per nomi vuoti</li>
     *   <li>Messaggio specifico per nomi puramente numerici</li>
     *   <li>Messaggio generico per altri casi</li>
     * </ul>
     *
     * @param author il nome dell'autore non valido
     * @return un messaggio di errore descrittivo e user-friendly
     */
    public static String getValidationErrorMessage(String author) {
        // Gestione caso specifico: nome vuoto
        if (author == null || author.trim().isEmpty()) {
            return "Author name cannot be empty";
        }

        // Gestione caso specifico: nome puramente numerico
        if (isPurelyNumeric(author)) {
            return "Author name cannot consist entirely of numbers and separators (like '" +
                   author.trim() + "'). Please enter a valid author name with alphabetic characters.";
        }

        // Messaggio generico per altri tipi di errori
        return "Author name must contain at least some alphabetic characters";
    }
}
