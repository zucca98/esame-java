package com.biblioteca.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe Composite per la gestione di categorie e sottocategorie nel sistema bibliotecario.
 *
 * <p>Questa classe implementa il ruolo di Composite nel pattern Composite,
 * rappresentando un contenitore che può contenere altri componenti
 * (sia foglie che altri compositi). Nel contesto bibliotecario, rappresenta
 * categorie che possono contenere libri, riviste o altre sottocategorie.</p>
 *
 * <p><strong>Ruolo nel pattern Composite:</strong></p>
 * <ul>
 *   <li><strong>Composite:</strong> Definisce comportamenti per componenti con figli</li>
 *   <li><strong>Container:</strong> Memorizza componenti figli e implementa operazioni correlate</li>
 *   <li><strong>Delegator:</strong> Delega operazioni ai figli quando appropriato</li>
 * </ul>
 *
 * <p><strong>Caratteristiche principali:</strong></p>
 * <ul>
 *   <li>Gestione di una collezione di componenti figli</li>
 *   <li>Operazioni ricorsive su tutta la gerarchia</li>
 *   <li>Calcolo aggregato delle informazioni (es. conteggio elementi)</li>
 *   <li>Visualizzazione gerarchica con indentazione</li>
 * </ul>
 *
 * <p><strong>Esempi di utilizzo:</strong></p>
 * <ul>
 *   <li>Categoria "Fiction" contenente libri di narrativa</li>
 *   <li>Categoria "Science" con sottocategorie "Physics", "Chemistry"</li>
 *   <li>Organizzazione gerarchica della biblioteca</li>
 * </ul>
 *
 * <p><strong>Operazioni supportate:</strong></p>
 * <ul>
 *   <li>Aggiunta e rimozione di componenti figli</li>
 *   <li>Navigazione della gerarchia</li>
 *   <li>Calcolo ricorsivo del numero di elementi</li>
 *   <li>Visualizzazione strutturata</li>
 * </ul>
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
 */
public class Category implements LibraryComponent {

    /** Il nome della categoria */
    private final String name;

    /** Lista dei componenti figli (possono essere altre categorie o elementi) */
    private final List<LibraryComponent> children;

    /**
     * Costruisce una nuova categoria con il nome specificato.
     *
     * <p>Inizializza una categoria vuota pronta per ricevere componenti figli.
     * La lista dei figli viene inizializzata come ArrayList vuoto.</p>
     *
     * @param name il nome della categoria (non deve essere nullo)
     */
    public Category(String name) {
        this.name = name;
        // Inizializzazione della lista dei componenti figli
        this.children = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     *
     * <p>Aggiunge un componente alla collezione dei figli di questa categoria.
     * Il componente può essere un elemento singolo (ItemLeaf) o un'altra
     * categoria (Category), permettendo la creazione di gerarchie complesse.</p>
     *
     * @param component il componente da aggiungere alla categoria
     */
    @Override
    public void add(LibraryComponent component) {
        // Aggiunta del componente alla lista dei figli
        children.add(component);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Rimuove un componente dalla collezione dei figli di questa categoria.
     * Utilizza il metodo equals() per identificare il componente da rimuovere.</p>
     *
     * @param component il componente da rimuovere dalla categoria
     */
    @Override
    public void remove(LibraryComponent component) {
        // Rimozione del componente dalla lista dei figli
        children.remove(component);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Restituisce una vista read-only della lista dei componenti figli
     * per prevenire modifiche accidentali alla struttura interna.</p>
     *
     * @return una lista non modificabile dei componenti figli
     */
    @Override
    public List<LibraryComponent> getChildren() {
        // Vista read-only per proteggere la struttura interna
        return Collections.unmodifiableList(children);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Visualizza le informazioni della categoria e di tutti i suoi figli
     * in modo ricorsivo. Utilizza l'indentazione per mostrare la gerarchia
     * e include il conteggio totale degli elementi.</p>
     */
    @Override
    public void displayInfo() {
        // Visualizzazione delle informazioni della categoria con conteggio
        System.out.println("Category: " + name + " (" + getItemCount() + " items)");

        // Visualizzazione ricorsiva di tutti i figli con indentazione
        for (LibraryComponent child : children) {
            System.out.print("  "); // Indentazione per mostrare la gerarchia
            child.displayInfo();
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Calcola ricorsivamente il numero totale di elementi contenuti
     * in questa categoria e in tutte le sue sottocategorie. Utilizza
     * le Stream API per un calcolo efficiente e funzionale.</p>
     *
     * @return il numero totale di elementi nella categoria e nelle sottocategorie
     */
    @Override
    public int getItemCount() {
        // Calcolo ricorsivo del numero di elementi utilizzando Stream API
        return children.stream()
                .mapToInt(LibraryComponent::getItemCount)
                .sum();
    }

    /**
     * Restituisce il nome della categoria.
     *
     * @return il nome della categoria
     */
    public String getName() {
        return name;
    }
}
