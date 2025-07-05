package com.biblioteca.strategy;

import java.util.List;
import java.util.stream.Collectors;

import com.biblioteca.model.LibraryItem;

/**
 * Strategia concreta per la ricerca per ID (ISBN/ISSN).
 *
 * <p>Questa classe implementa il pattern Strategy fornendo un algoritmo
 * specializzato per la ricerca di elementi bibliotecari basata sui loro
 * identificativi (ISBN per i libri, ISSN per le riviste). Supporta sia
 * la corrispondenza esatta che quella parziale con logica intelligente.</p>
 *
 * <p><strong>Algoritmo di ricerca implementato:</strong></p>
 * <ol>
 *   <li><strong>Corrispondenza esatta:</strong> Cerca ID che corrispondono esattamente (case-insensitive)</li>
 *   <li><strong>Corrispondenza parziale:</strong> Se non trova corrispondenze esatte, cerca per cifre</li>
 *   <li><strong>Filtro minimo:</strong> Richiede almeno 3 cifre per la ricerca parziale</li>
 * </ol>
 *
 * <p><strong>Caratteristiche della ricerca:</strong></p>
 * <ul>
 *   <li>Case-insensitive per corrispondenze esatte</li>
 *   <li>Estrazione automatica delle cifre per ricerca parziale</li>
 *   <li>Validazione della lunghezza minima per evitare risultati troppo generici</li>
 *   <li>Gestione sicura di valori nulli e stringhe vuote</li>
 * </ul>
 *
 * <p><strong>Esempi di utilizzo:</strong></p>
 * <ul>
 *   <li>"978-0134685991" → corrispondenza esatta con ISBN</li>
 *   <li>"134685" → corrispondenza parziale con qualsiasi ID contenente queste cifre</li>
 *   <li>"12" → nessun risultato (meno di 3 cifre)</li>
 * </ul>
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
 */
public class IdSearchStrategy implements SearchStrategy {

    /**
     * {@inheritDoc}
     *
     * <p>Implementa la ricerca per ID utilizzando un algoritmo a due fasi:
     * prima cerca corrispondenze esatte, poi corrispondenze parziali basate sulle cifre.</p>
     *
     * <p><strong>Fasi dell'algoritmo:</strong></p>
     * <ol>
     *   <li>Validazione e pulizia della query di input</li>
     *   <li>Tentativo di corrispondenza esatta (case-insensitive)</li>
     *   <li>Se non trova risultati, estrazione delle cifre dalla query</li>
     *   <li>Ricerca parziale solo se ci sono almeno 3 cifre</li>
     * </ol>
     *
     * @param items la collezione di LibraryItem in cui cercare
     * @param query la stringa di ricerca (ID completo o parziale)
     * @return lista di elementi che corrispondono ai criteri di ricerca
     */
    @Override
    public List<LibraryItem> search(List<LibraryItem> items, String query) {
        // Validazione iniziale: query nulla o vuota
        if (query == null || query.trim().isEmpty()) {
            return List.of(); // Restituisce lista vuota per query non valide
        }

        // Pulizia della query rimuovendo spazi iniziali e finali
        String trimmedQuery = query.trim();

        // FASE 1: Tentativo di corrispondenza esatta (case-insensitive)
        List<LibraryItem> exactMatches = items.stream()
                .filter(item -> item.getId() != null && item.getId().equalsIgnoreCase(trimmedQuery))
                .collect(Collectors.toList());

        // Se troviamo corrispondenze esatte, le restituiamo immediatamente
        if (!exactMatches.isEmpty()) {
            return exactMatches;
        }

        // FASE 2: Estrazione delle cifre per ricerca parziale
        String digitsOnly = trimmedQuery.replaceAll("[^0-9]", "");

        // Ricerca parziale solo se abbiamo almeno 3 cifre (per evitare risultati troppo generici)
        if (digitsOnly.length() >= 3) {
            return items.stream()
                    .filter(item -> {
                        String itemId = item.getId();
                        // Controllo di sicurezza per ID nulli
                        if (itemId == null) {
                            return false;
                        }

                        // Estrazione delle cifre dall'ID dell'elemento
                        String itemDigits = itemId.replaceAll("[^0-9]", "");
                        // Verifica se le cifre dell'elemento contengono le cifre della query
                        return itemDigits.contains(digitsOnly);
                    })
                    .collect(Collectors.toList());
        }

        // Se abbiamo meno di 3 cifre, restituiamo lista vuota per evitare risultati troppo generici
        return List.of();
    }
}
