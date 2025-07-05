
package com.biblioteca.strategy;

import java.util.List;
import java.util.stream.Collectors;

import com.biblioteca.model.LibraryItem;

/**
 * Strategia concreta per la ricerca per titolo.
 *
 * <p>Questa classe implementa il pattern Strategy fornendo un algoritmo
 * specializzato per la ricerca di elementi bibliotecari basata sui loro
 * titoli. Utilizza le Stream API di Java 8+ e le espressioni lambda per
 * un'implementazione concisa ed efficiente.</p>
 *
 * <p><strong>Caratteristiche dell'algoritmo di ricerca:</strong></p>
 * <ul>
 *   <li><strong>Case-insensitive:</strong> La ricerca ignora maiuscole/minuscole</li>
 *   <li><strong>Substring matching:</strong> Trova titoli che contengono la query</li>
 *   <li><strong>Gestione sicura dei null:</strong> Controlla titoli nulli</li>
 *   <li><strong>Efficienza:</strong> Utilizza Stream API per elaborazione parallela potenziale</li>
 * </ul>
 *
 * <p><strong>Tecnologie utilizzate:</strong></p>
 * <ul>
 *   <li><strong>Stream API:</strong> Per elaborazione funzionale della collezione</li>
 *   <li><strong>Lambda expressions:</strong> Per predicati di filtro concisi</li>
 *   <li><strong>Method chaining:</strong> Per operazioni fluent</li>
 * </ul>
 *
 * <p><strong>Esempi di utilizzo:</strong></p>
 * <ul>
 *   <li>"Java" → trova "Java Programming", "Advanced Java", ecc.</li>
 *   <li>"programming" → trova "Programming in C++", "Java Programming", ecc.</li>
 *   <li>"DESIGN" → trova "Design Patterns" (case-insensitive)</li>
 * </ul>
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
 */
public class TitleSearchStrategy implements SearchStrategy {

    /**
     * {@inheritDoc}
     *
     * <p>Implementa la ricerca per titolo utilizzando un algoritmo di substring
     * matching case-insensitive. Utilizza le Stream API per un'elaborazione
     * efficiente e funzionale della collezione.</p>
     *
     * <p><strong>Algoritmo implementato:</strong></p>
     * <ol>
     *   <li>Validazione della query di input</li>
     *   <li>Conversione a lowercase per confronto case-insensitive</li>
     *   <li>Filtro degli elementi con titoli che contengono la query</li>
     *   <li>Raccolta dei risultati in una nuova lista</li>
     * </ol>
     *
     * @param items la collezione di LibraryItem in cui cercare
     * @param query la stringa di ricerca per il titolo (può essere parziale)
     * @return lista di elementi i cui titoli contengono la query
     */
    @Override
    public List<LibraryItem> search(List<LibraryItem> items, String query) {
        // Validazione iniziale: query nulla o vuota
        if (query == null || query.trim().isEmpty()) {
            return List.of(); // Restituisce lista vuota per query non valide
        }

        // Utilizzo delle Stream API per elaborazione funzionale
        return items.stream()
                // Filtro per elementi con titoli non nulli che contengono la query (case-insensitive)
                .filter(item -> item.getTitle() != null &&
                        item.getTitle().toLowerCase()
                                .contains(query.toLowerCase()))
                // Raccolta dei risultati in una lista
                .collect(Collectors.toList());
    }
}