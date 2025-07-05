package com.biblioteca.composite;

import java.util.List;

/**
 * Interfaccia Component per il pattern Composite nel sistema bibliotecario.
 *
 * <p>Questa interfaccia definisce il contratto comune per tutti gli elementi
 * nella gerarchia del pattern Composite, permettendo di trattare uniformemente
 * sia elementi singoli (foglie) che collezioni di elementi (compositi).
 * Nel contesto bibliotecario, rappresenta sia singoli elementi che categorie
 * contenenti altri elementi.</p>
 *
 * <p><strong>Ruoli nel pattern Composite:</strong></p>
 * <ul>
 *   <li><strong>Component:</strong> Definisce l'interfaccia comune per oggetti semplici e composti</li>
 *   <li><strong>Uniformità:</strong> Permette al client di trattare oggetti singoli e composizioni uniformemente</li>
 *   <li><strong>Trasparenza:</strong> Il client non deve distinguere tra foglie e compositi</li>
 * </ul>
 *
 * <p><strong>Implementazioni concrete:</strong></p>
 * <ul>
 *   <li>{@link ItemLeaf} - Rappresenta elementi singoli (libri, riviste)</li>
 *   <li>{@link Category} - Rappresenta categorie che contengono altri componenti</li>
 * </ul>
 *
 * <p><strong>Vantaggi del pattern:</strong></p>
 * <ul>
 *   <li>Strutture ad albero per organizzare elementi bibliotecari</li>
 *   <li>Operazioni uniformi su singoli elementi e gruppi</li>
 *   <li>Facilità di aggiunta di nuovi tipi di componenti</li>
 *   <li>Navigazione ricorsiva della gerarchia</li>
 * </ul>
 *
 * <p><strong>Esempio di utilizzo:</strong></p>
 * <pre>{@code
 * LibraryComponent fiction = new Category("Fiction");
 * LibraryComponent book = new ItemLeaf(someBook);
 * fiction.add(book);
 * fiction.displayInfo(); // Mostra la categoria e tutti i suoi contenuti
 * }</pre>
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
 */
public interface LibraryComponent {

    /**
     * Visualizza le informazioni del componente.
     *
     * <p>Per le foglie (elementi singoli), mostra le informazioni dell'elemento.
     * Per i compositi (categorie), mostra le informazioni della categoria
     * e ricorsivamente di tutti i suoi figli.</p>
     */
    void displayInfo();

    /**
     * Restituisce il numero totale di elementi contenuti nel componente.
     *
     * <p>Per le foglie, restituisce sempre 1.
     * Per i compositi, restituisce la somma ricorsiva di tutti gli elementi
     * contenuti nei suoi figli.</p>
     *
     * @return il numero totale di elementi
     */
    int getItemCount();

    /**
     * Aggiunge un componente figlio a questo componente.
     *
     * <p>Per i compositi, aggiunge il componente alla collezione dei figli.
     * Per le foglie, questa operazione potrebbe non essere supportata
     * e lanciare un'eccezione.</p>
     *
     * @param component il componente da aggiungere
     * @throws UnsupportedOperationException se l'operazione non è supportata
     */
    void add(LibraryComponent component);

    /**
     * Rimuove un componente figlio da questo componente.
     *
     * <p>Per i compositi, rimuove il componente dalla collezione dei figli.
     * Per le foglie, questa operazione potrebbe non essere supportata
     * e lanciare un'eccezione.</p>
     *
     * @param component il componente da rimuovere
     * @throws UnsupportedOperationException se l'operazione non è supportata
     */
    void remove(LibraryComponent component);

    /**
     * Restituisce la lista dei componenti figli.
     *
     * <p>Per i compositi, restituisce la collezione dei figli.
     * Per le foglie, restituisce una lista vuota o lancia un'eccezione.</p>
     *
     * @return la lista dei componenti figli
     * @throws UnsupportedOperationException se l'operazione non è supportata
     */
    List<LibraryComponent> getChildren();
}