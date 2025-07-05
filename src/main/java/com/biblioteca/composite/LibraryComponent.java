package com.biblioteca.composite;

import java.util.List;

/**
 * Interfaccia Component per il pattern Composite nel sistema bibliotecario.
 *
 * Questa interfaccia definisce il contratto comune per tutti gli elementi
 * nella gerarchia del pattern Composite, permettendo di trattare uniformemente
 * sia elementi singoli (foglie) che collezioni di elementi (compositi).
 * Nel contesto bibliotecario, rappresenta sia singoli elementi che categorie
 * contenenti altri elementi.
 *
 * Ruoli nel pattern Composite:
 *   Component: Definisce l'interfaccia comune per oggetti semplici e composti
 *   Uniformità: Permette al client di trattare oggetti singoli e composizioni uniformemente
 *   Trasparenza: Il client non deve distinguere tra foglie e compositi
 * 
 *
 * Implementazioni concrete:
 * 
 *   {@link ItemLeaf} - Rappresenta elementi singoli (libri, riviste)
 *   {@link Category} - Rappresenta categorie che contengono altri componenti
 * 
 *
 * Vantaggi del pattern:
 * 
 *   Strutture ad albero per organizzare elementi bibliotecari
 *   Operazioni uniformi su singoli elementi e gruppi
 *   Facilità di aggiunta di nuovi tipi di componenti
 *   Navigazione ricorsiva della gerarchia
 * 
 *
 * Esempio di utilizzo:
 * >{@code
 * LibraryComponent fiction = new Category("Fiction");
 * LibraryComponent book = new ItemLeaf(someBook);
 * fiction.add(book);
 * fiction.displayInfo(); // Mostra la categoria e tutti i suoi contenuti
 * }
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
 */
public interface LibraryComponent {

    /**
     * Visualizza le informazioni del componente.
     *
     * Per le foglie (elementi singoli), mostra le informazioni dell'elemento.
     * Per i compositi (categorie), mostra le informazioni della categoria
     * e ricorsivamente di tutti i suoi figli.
     */
    void displayInfo();

    /**
     * Restituisce il numero totale di elementi contenuti nel componente.
     *
     * Per le foglie, restituisce sempre 1.
     * Per i compositi, restituisce la somma ricorsiva di tutti gli elementi
     * contenuti nei suoi figli.
     *
     * @return il numero totale di elementi
     */
    int getItemCount();

    /**
     * Aggiunge un componente figlio a questo componente.
     *
     * Per i compositi, aggiunge il componente alla collezione dei figli.
     * Per le foglie, questa operazione potrebbe non essere supportata
     * e lanciare un'eccezione.
     *
     * @param component il componente da aggiungere
     * @throws UnsupportedOperationException se l'operazione non è supportata
     */
    void add(LibraryComponent component);

    /**
     * Rimuove un componente figlio da questo componente.
     *
     * Per i compositi, rimuove il componente dalla collezione dei figli.
     * Per le foglie, questa operazione potrebbe non essere supportata
     * e lanciare un'eccezione.
     *
     * @param component il componente da rimuovere
     * @throws UnsupportedOperationException se l'operazione non è supportata
     */
    void remove(LibraryComponent component);

    /**
     * Restituisce la lista dei componenti figli.
     *
     * Per i compositi, restituisce la collezione dei figli.
     * Per le foglie, restituisce una lista vuota o lancia un'eccezione.
     *
     * @return la lista dei componenti figli
     * @throws UnsupportedOperationException se l'operazione non è supportata
     */
    List<LibraryComponent> getChildren();
}