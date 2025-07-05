package com.biblioteca.iterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.biblioteca.model.LibraryItem;

/**
 * Collezione personalizzata che fornisce iteratori custom per oggetti LibraryItem.
 *
 * <p>Questa classe rappresenta il ruolo di ConcreteAggregate nel pattern Iterator,
 * fornendo una collezione specializzata per elementi bibliotecari con capacità
 * di iterazione personalizzata. Implementa l'interfaccia {@link Iterable} per
 * supportare i cicli for-each di Java.</p>
 *
 * <p><strong>Ruoli nel pattern Iterator:</strong></p>
 * <ul>
 *   <li><strong>ConcreteAggregate:</strong> Implementa la creazione dell'iteratore</li>
 *   <li><strong>Iterable:</strong> Supporta l'iterazione standard di Java</li>
 *   <li><strong>Collection:</strong> Gestisce la collezione di LibraryItem</li>
 * </ul>
 *
 * <p><strong>Caratteristiche principali:</strong></p>
 * <ul>
 *   <li>Gestione sicura della collezione interna</li>
 *   <li>Supporto per iteratori personalizzati e standard</li>
 *   <li>Operazioni di base per aggiunta/rimozione elementi</li>
 *   <li>Accesso read-only alla collezione interna</li>
 *   <li>Compatibilità con for-each loops</li>
 * </ul>
 *
 * <p><strong>Utilizzo tipico:</strong></p>
 * <pre>{@code
 * LibraryCollection collection = new LibraryCollection();
 * collection.addItem(book);
 *
 * // Iterazione con iteratore personalizzato
 * LibraryIterator iterator = collection.createIterator();
 *
 * // Iterazione con for-each
 * for (LibraryItem item : collection) {
 *     item.displayInfo();
 * }
 * }</pre>
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
 */
public class LibraryCollection implements Iterable<LibraryItem> {

    /** Lista interna che mantiene gli elementi della collezione */
    private final List<LibraryItem> items;

    /**
     * Costruisce una nuova collezione vuota di elementi bibliotecari.
     *
     * <p>Inizializza la collezione con una lista vuota pronta per
     * ricevere elementi LibraryItem.</p>
     */
    public LibraryCollection() {
        // Inizializzazione della lista interna
        this.items = new ArrayList<>();
    }

    /**
     * Aggiunge un elemento alla collezione.
     *
     * <p>Inserisce un nuovo LibraryItem nella collezione. L'elemento
     * viene aggiunto alla fine della lista interna.</p>
     *
     * @param item l'elemento da aggiungere alla collezione (non deve essere nullo)
     */
    public void addItem(LibraryItem item) {
        // Aggiunta dell'elemento alla lista interna
        items.add(item);
    }

    /**
     * Rimuove un elemento dalla collezione.
     *
     * <p>Rimuove la prima occorrenza dell'elemento specificato dalla collezione,
     * se presente. Utilizza il metodo equals() per il confronto.</p>
     *
     * @param item l'elemento da rimuovere dalla collezione
     * @return {@code true} se l'elemento è stato rimosso, {@code false} altrimenti
     */
    public boolean removeItem(LibraryItem item) {
        // Rimozione dell'elemento dalla lista interna
        return items.remove(item);
    }

    /**
     * Restituisce il numero di elementi nella collezione.
     *
     * @return il numero di elementi attualmente presenti nella collezione
     */
    public int size() {
        return items.size();
    }

    /**
     * Crea un nuovo iteratore personalizzato per questa collezione.
     *
     * <p>Questo metodo implementa il pattern Iterator creando un'istanza
     * di {@link LibraryIterator} che fornisce funzionalità aggiuntive
     * rispetto all'iteratore standard di Java.</p>
     *
     * @return un nuovo LibraryIterator per attraversare la collezione
     */
    public LibraryIterator createIterator() {
        // Creazione dell'iteratore personalizzato
        return new LibraryIterator(items);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Implementa l'interfaccia Iterable per supportare i cicli for-each
     * di Java. Utilizza internamente il nostro iteratore personalizzato.</p>
     *
     * @return un iteratore per attraversare la collezione
     */
    @Override
    public Iterator<LibraryItem> iterator() {
        // Delega al nostro iteratore personalizzato
        return createIterator();
    }

    /**
     * Restituisce una vista read-only della collezione interna.
     *
     * <p>Questo metodo fornisce accesso sicuro alla lista interna
     * restituendo una vista non modificabile. Utile quando si vuole
     * accedere agli elementi senza rischiare modifiche accidentali.</p>
     *
     * @return una lista non modificabile contenente tutti gli elementi
     */
    public List<LibraryItem> getItems() {
        // Vista read-only per accesso sicuro alla collezione
        return Collections.unmodifiableList(items);
    }
}