
package com.biblioteca.model;

/**
 * Interfaccia base per tutti gli elementi della biblioteca.
 *
 * <p>Questa interfaccia definisce il contratto comune per tutti i tipi di elementi
 * che possono essere gestiti dal sistema bibliotecario (libri, riviste, ecc.).
 * Implementa il ruolo di Component nel pattern Composite e viene utilizzata
 * dal pattern Factory per la creazione di oggetti.</p>
 *
 * <p><strong>Design Patterns implementati:</strong></p>
 * <ul>
 *   <li><strong>Factory Pattern:</strong> Utilizzata da LibraryItemFactory per creare istanze</li>
 *   <li><strong>Composite Pattern:</strong> Funge da Component base per la gerarchia</li>
 *   <li><strong>Iterator Pattern:</strong> Gli elementi implementano questa interfaccia per essere iterabili</li>
 * </ul>
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
 */
public interface LibraryItem {

    /**
     * Restituisce l'identificativo univoco dell'elemento.
     *
     * <p>L'ID è utilizzato come chiave primaria per identificare univocamente
     * ogni elemento nella collezione della biblioteca. Deve essere non nullo
     * e unico all'interno del sistema.</p>
     *
     * @return l'identificativo univoco dell'elemento (non nullo)
     */
    String getId();

    /**
     * Restituisce il titolo dell'elemento.
     *
     * <p>Il titolo rappresenta il nome principale dell'elemento bibliotecario
     * ed è utilizzato per la ricerca e la visualizzazione.</p>
     *
     * @return il titolo dell'elemento (non nullo)
     */
    String getTitle();

    /**
     * Restituisce il tipo dell'elemento.
     *
     * <p>Il tipo identifica la categoria dell'elemento (es. "Book", "Magazine")
     * e viene utilizzato per la classificazione e il filtraggio.</p>
     *
     * @return il tipo dell'elemento (non nullo)
     */
    String getType();

    /**
     * Verifica se l'elemento è disponibile per il prestito.
     *
     * <p>La disponibilità indica se l'elemento può essere prestato agli utenti.
     * Un elemento non disponibile potrebbe essere già in prestito o riservato.</p>
     *
     * @return {@code true} se l'elemento è disponibile, {@code false} altrimenti
     */
    boolean isAvailable();

    /**
     * Imposta lo stato di disponibilità dell'elemento.
     *
     * <p>Questo metodo permette di modificare lo stato di disponibilità
     * dell'elemento, tipicamente quando viene prestato o restituito.</p>
     *
     * @param available {@code true} per rendere l'elemento disponibile,
     *                  {@code false} per renderlo non disponibile
     */
    void setAvailable(boolean available);

    /**
     * Visualizza le informazioni complete dell'elemento.
     *
     * <p>Questo metodo stampa sulla console tutte le informazioni rilevanti
     * dell'elemento in un formato leggibile. L'implementazione specifica
     * dipende dal tipo di elemento.</p>
     */
    void displayInfo();
}