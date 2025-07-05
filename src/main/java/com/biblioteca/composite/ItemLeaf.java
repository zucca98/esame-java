package com.biblioteca.composite;

import java.util.Collections;
import java.util.List;

import com.biblioteca.model.LibraryItem;

/**
 * Classe Leaf per elementi bibliotecari individuali nel pattern Composite.
 *
 * <p>Questa classe implementa il ruolo di Leaf nel pattern Composite,
 * rappresentando gli elementi terminali della gerarchia che non possono
 * contenere altri componenti. Nel contesto bibliotecario, rappresenta
 * singoli elementi come libri o riviste all'interno della struttura
 * organizzativa della biblioteca.</p>
 *
 * <p><strong>Ruolo nel pattern Composite:</strong></p>
 * <ul>
 *   <li><strong>Leaf:</strong> Rappresenta oggetti foglia nella composizione</li>
 *   <li><strong>Terminal:</strong> Non ha figli e definisce comportamenti per oggetti primitivi</li>
 *   <li><strong>Wrapper:</strong> Incapsula un LibraryItem per integrarlo nella gerarchia</li>
 * </ul>
 *
 * <p><strong>Caratteristiche principali:</strong></p>
 * <ul>
 *   <li>Incapsula un singolo LibraryItem (Book o Magazine)</li>
 *   <li>Non supporta operazioni di aggiunta/rimozione figli</li>
 *   <li>Delega le operazioni di visualizzazione all'elemento incapsulato</li>
 *   <li>Restituisce sempre 1 come conteggio elementi</li>
 * </ul>
 *
 * <p><strong>Integrazione con il sistema:</strong></p>
 * <ul>
 *   <li>Permette di inserire LibraryItem in strutture gerarchiche</li>
 *   <li>Mantiene la compatibilità con l'interfaccia LibraryComponent</li>
 *   <li>Fornisce accesso all'elemento bibliotecario sottostante</li>
 * </ul>
 *
 * <p><strong>Esempio di utilizzo:</strong></p>
 * <pre>{@code
 * LibraryItem book = LibraryItemFactory.createBook(...);
 * LibraryComponent leaf = new ItemLeaf(book);
 * category.add(leaf); // Aggiunge il libro alla categoria
 * }</pre>
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
 */
public class ItemLeaf implements LibraryComponent {

    /** L'elemento bibliotecario incapsulato da questa foglia */
    private final LibraryItem item;

    /**
     * Costruisce una nuova foglia che incapsula l'elemento bibliotecario specificato.
     *
     * <p>Crea un wrapper per l'elemento LibraryItem che permette di integrarlo
     * nella struttura gerarchica del pattern Composite.</p>
     *
     * @param item l'elemento bibliotecario da incapsulare (non deve essere nullo)
     */
    public ItemLeaf(LibraryItem item) {
        this.item = item;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Delega la visualizzazione delle informazioni all'elemento
     * bibliotecario incapsulato, mantenendo la coerenza con il
     * comportamento originale dell'elemento.</p>
     */
    @Override
    public void displayInfo() {
        // Delega la visualizzazione all'elemento incapsulato
        item.displayInfo();
    }

    /**
     * {@inheritDoc}
     *
     * <p>Le foglie rappresentano sempre un singolo elemento,
     * quindi restituiscono sempre 1 come conteggio.</p>
     *
     * @return sempre 1, poiché una foglia rappresenta un singolo elemento
     */
    @Override
    public int getItemCount() {
        return 1; // Una foglia rappresenta sempre un singolo elemento
    }

    /**
     * {@inheritDoc}
     *
     * <p>Le foglie non possono contenere altri componenti, quindi
     * questa operazione non è supportata e lancia un'eccezione.</p>
     *
     * @param component il componente da aggiungere (ignorato)
     * @throws UnsupportedOperationException sempre, poiché le foglie non supportano figli
     */
    @Override
    public void add(LibraryComponent component) {
        // Le foglie non possono contenere altri componenti
        throw new UnsupportedOperationException("Cannot add to a leaf");
    }

    /**
     * {@inheritDoc}
     *
     * <p>Le foglie non possono contenere altri componenti, quindi
     * questa operazione non è supportata e lancia un'eccezione.</p>
     *
     * @param component il componente da rimuovere (ignorato)
     * @throws UnsupportedOperationException sempre, poiché le foglie non supportano figli
     */
    @Override
    public void remove(LibraryComponent component) {
        // Le foglie non possono contenere altri componenti
        throw new UnsupportedOperationException("Cannot remove from a leaf");
    }

    /**
     * {@inheritDoc}
     *
     * <p>Le foglie non hanno figli, quindi restituiscono sempre
     * una lista vuota.</p>
     *
     * @return una lista vuota, poiché le foglie non hanno figli
     */
    @Override
    public List<LibraryComponent> getChildren() {
        // Le foglie non hanno figli
        return Collections.emptyList();
    }

    /**
     * Restituisce l'elemento bibliotecario incapsulato.
     *
     * <p>Questo metodo fornisce accesso diretto all'elemento LibraryItem
     * sottostante, utile quando è necessario accedere alle funzionalità
     * specifiche dell'elemento al di fuori del pattern Composite.</p>
     *
     * @return l'elemento bibliotecario incapsulato
     */
    public LibraryItem getItem() {
        return item;
    }
}
