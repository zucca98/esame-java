package com.biblioteca.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.biblioteca.model.LibraryItem;

/**
 * Implementazione personalizzata del pattern Iterator per collezioni di LibraryItem.
 *
 * <p>Questa classe implementa il pattern Iterator per fornire un modo uniforme
 * e sicuro di attraversare una collezione di elementi bibliotecari. Estende
 * l'interfaccia standard {@link Iterator} di Java aggiungendo funzionalità
 * specifiche per il dominio biblioteca.</p>
 *
 * <p><strong>Caratteristiche del pattern Iterator:</strong></p>
 * <ul>
 *   <li><strong>Accesso sequenziale:</strong> Permette di attraversare gli elementi uno alla volta</li>
 *   <li><strong>Encapsulation:</strong> Nasconde la struttura interna della collezione</li>
 *   <li><strong>Sicurezza:</strong> Copia difensiva per evitare modifiche concorrenti</li>
 *   <li><strong>Flessibilità:</strong> Supporta reset e tracking della posizione</li>
 * </ul>
 *
 * <p><strong>Funzionalità aggiuntive:</strong></p>
 * <ul>
 *   <li>Reset dell'iteratore per ripartire dall'inizio</li>
 *   <li>Tracking della posizione corrente</li>
 *   <li>Copia difensiva della collezione per thread-safety</li>
 * </ul>
 *
 * <p><strong>Utilizzo tipico:</strong></p>
 * <pre>{@code
 * LibraryIterator iterator = collection.createIterator();
 * while (iterator.hasNext()) {
 *     LibraryItem item = iterator.next();
 *     item.displayInfo();
 * }
 * }</pre>
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
 */
public class LibraryIterator implements Iterator<LibraryItem> {

    /** Copia difensiva della lista di elementi da iterare */
    private final List<LibraryItem> items;

    /** Posizione corrente nell'iterazione (indice del prossimo elemento) */
    private int position = 0;

    /**
     * Costruisce un nuovo LibraryIterator per la lista specificata.
     *
     * <p>Il costruttore crea una copia difensiva della lista fornita per
     * garantire che l'iteratore non sia influenzato da modifiche successive
     * alla collezione originale. Questo approccio garantisce la consistenza
     * dell'iterazione anche in presenza di modifiche concorrenti.</p>
     *
     * @param items la lista di LibraryItem da iterare (non deve essere nulla)
     */
    public LibraryIterator(List<LibraryItem> items) {
        // Copia difensiva per evitare modifiche concorrenti alla collezione
        this.items = new ArrayList<>(items);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Verifica se ci sono ancora elementi da iterare confrontando
     * la posizione corrente con la dimensione della collezione.</p>
     *
     * @return {@code true} se ci sono ancora elementi, {@code false} altrimenti
     */
    @Override
    public boolean hasNext() {
        // Controllo se la posizione corrente è ancora valida
        return position < items.size();
    }

    /**
     * {@inheritDoc}
     *
     * <p>Restituisce il prossimo elemento nella sequenza e avanza la posizione.
     * Se non ci sono più elementi disponibili, lancia un'eccezione.</p>
     *
     * @return il prossimo LibraryItem nella sequenza
     * @throws NoSuchElementException se non ci sono più elementi da iterare
     */
    @Override
    public LibraryItem next() {
        // Controllo di sicurezza prima di accedere all'elemento
        if (!hasNext()) {
            throw new NoSuchElementException("No more items");
        }
        // Restituisce l'elemento corrente e incrementa la posizione
        return items.get(position++);
    }

    /**
     * Reimposta l'iteratore alla posizione iniziale.
     *
     * <p>Questo metodo permette di riutilizzare lo stesso iteratore
     * per attraversare nuovamente la collezione dall'inizio, senza
     * dover creare una nuova istanza.</p>
     */
    public void reset() {
        // Riporta la posizione all'inizio della collezione
        position = 0;
    }

    /**
     * Restituisce la posizione corrente dell'iteratore.
     *
     * <p>La posizione rappresenta l'indice del prossimo elemento che
     * sarà restituito da una chiamata a {@link #next()}. Utile per
     * debugging e monitoraggio del progresso dell'iterazione.</p>
     *
     * @return la posizione corrente (0-based) nell'iterazione
     */
    public int getCurrentPosition() {
        return position;
    }
}
