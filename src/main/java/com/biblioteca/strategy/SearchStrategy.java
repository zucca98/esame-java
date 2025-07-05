package com.biblioteca.strategy;

import java.util.List;

import com.biblioteca.model.LibraryItem;

/**
 * Interfaccia del pattern Strategy per diversi algoritmi di ricerca.
 *
 * <p>Questa interfaccia definisce il contratto comune per tutte le strategie
 * di ricerca nel sistema bibliotecario. Implementa il ruolo di Strategy
 * nel pattern Strategy, permettendo di definire una famiglia di algoritmi
 * di ricerca intercambiabili.</p>
 *
 * <p><strong>Vantaggi del pattern Strategy:</strong></p>
 * <ul>
 *   <li><strong>Flessibilità:</strong> Permette di cambiare algoritmo a runtime</li>
 *   <li><strong>Estensibilità:</strong> Facile aggiunta di nuove strategie di ricerca</li>
 *   <li><strong>Separazione delle responsabilità:</strong> Ogni strategia è indipendente</li>
 *   <li><strong>Testabilità:</strong> Ogni strategia può essere testata separatamente</li>
 *   <li><strong>Riusabilità:</strong> Le strategie possono essere riutilizzate in contesti diversi</li>
 * </ul>
 *
 * <p><strong>Strategie implementate:</strong></p>
 * <ul>
 *   <li>{@link IdSearchStrategy} - Ricerca per ID/ISBN/ISSN</li>
 *   <li>{@link TitleSearchStrategy} - Ricerca per titolo</li>
 * </ul>
 *
 * <p><strong>Utilizzo tipico:</strong></p>
 * <pre>{@code
 * SearchStrategy strategy = new TitleSearchStrategy();
 * List<LibraryItem> results = strategy.search(items, "Java Programming");
 *
 * // Cambio di strategia a runtime
 * strategy = new IdSearchStrategy();
 * results = strategy.search(items, "978-0134685991");
 * }</pre>
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
 */
public interface SearchStrategy {

    /**
     * Esegue la ricerca nella collezione di elementi utilizzando la strategia specifica.
     *
     * <p>Questo metodo rappresenta il cuore del pattern Strategy. Ogni implementazione
     * concreta definirà il proprio algoritmo di ricerca specifico, mantenendo
     * la stessa interfaccia per garantire l'intercambiabilità.</p>
     *
     * <p><strong>Comportamento generale:</strong></p>
     * <ul>
     *   <li>Analizza la query fornita secondo la strategia specifica</li>
     *   <li>Attraversa la collezione di elementi</li>
     *   <li>Applica i criteri di matching della strategia</li>
     *   <li>Restituisce tutti gli elementi che soddisfano i criteri</li>
     * </ul>
     *
     * @param items la collezione di LibraryItem in cui cercare (non deve essere nulla)
     * @param query la stringa di ricerca (non deve essere nulla)
     * @return una lista di LibraryItem che corrispondono ai criteri di ricerca;
     *         lista vuota se nessun elemento corrisponde
     */
    List<LibraryItem> search(List<LibraryItem> items, String query);
}
