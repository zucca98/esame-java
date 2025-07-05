package com.biblioteca.model;

/**
 * Rappresenta una rivista nel sistema bibliotecario.
 *
 * <p>Questa classe implementa l'interfaccia {@link LibraryItem} e rappresenta
 * una rivista con le sue caratteristiche specifiche come ISSN e numero di edizione.
 * A differenza dei libri, le riviste utilizzano un costruttore tradizionale
 * invece del pattern Builder, data la semplicità dei loro attributi.</p>
 *
 * <p><strong>Caratteristiche specifiche delle riviste:</strong></p>
 * <ul>
 *   <li>ISSN (International Standard Serial Number) come identificativo</li>
 *   <li>Numero di edizione per identificare la specifica pubblicazione</li>
 *   <li>Struttura più semplice rispetto ai libri</li>
 *   <li>Gestione della disponibilità per il prestito</li>
 * </ul>
 *
 * <p><strong>Design Patterns:</strong></p>
 * <ul>
 *   <li><strong>Immutable Object:</strong> I campi principali sono final</li>
 *   <li><strong>Factory Pattern:</strong> Creata tramite LibraryItemFactory</li>
 * </ul>
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
 */
public class Magazine implements LibraryItem {

    /** L'ISSN della rivista, utilizzato come identificativo univoco */
    private final String issn;

    /** Il titolo della rivista */
    private final String title;

    /** Il numero di edizione della rivista */
    private final int issueNumber;

    /** Indica se la rivista è attualmente disponibile per il prestito */
    private boolean available;

    /**
     * Costruisce una nuova istanza di Magazine.
     *
     * <p>Crea una rivista con i parametri specificati. La rivista viene
     * automaticamente impostata come disponibile al momento della creazione.</p>
     *
     * @param issn l'ISSN della rivista (deve essere non nullo)
     * @param title il titolo della rivista (deve essere non nullo)
     * @param issueNumber il numero di edizione della rivista (deve essere positivo)
     */
    public Magazine(String issn, String title, int issueNumber) {
        // Inizializzazione dei campi immutabili
        this.issn = issn;
        this.title = title;
        this.issueNumber = issueNumber;
        // Per default, ogni nuova rivista è disponibile
        this.available = true;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Per le riviste, l'ID corrisponde all'ISSN (International Standard Serial Number).</p>
     */
    @Override
    public String getId() {
        return issn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * Restituisce il numero di edizione della rivista.
     *
     * <p>Il numero di edizione identifica la specifica pubblicazione
     * di una rivista periodica (es. numero 1, numero 2, ecc.).</p>
     *
     * @return il numero di edizione della rivista
     */
    public int getIssueNumber() {
        return issueNumber;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Per le riviste, il tipo è sempre "Magazine".</p>
     */
    @Override
    public String getType() {
        return "Magazine";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAvailable() {
        return available;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Visualizza le informazioni complete della rivista in formato leggibile,
     * includendo titolo, numero di edizione, ISSN e stato di disponibilità.</p>
     */
    @Override
    public void displayInfo() {
        // Formattazione delle informazioni della rivista per la visualizzazione
        System.out.printf("Magazine: %s Issue #%d (ISSN: %s, Available: %s)%n",
                title, issueNumber, issn, available ? "Yes" : "No");
    }
}