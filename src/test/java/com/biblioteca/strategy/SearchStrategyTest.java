package com.biblioteca.strategy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.biblioteca.exceptions.InvalidDataException;
import com.biblioteca.factory.LibraryItemFactory;
import com.biblioteca.model.LibraryItem;

/**
 * Test suite per le implementazioni del pattern Strategy per la ricerca.
 *
 * <p>Questa classe di test verifica il corretto funzionamento delle diverse
 * strategie di ricerca implementate nel sistema bibliotecario. I test coprono
 * sia {@link IdSearchStrategy} che {@link TitleSearchStrategy}, verificando
 * l'intercambiabilità e la correttezza degli algoritmi.</p>
 *
 * <p><strong>Strategie testate:</strong></p>
 * <ul>
 *   <li><strong>IdSearchStrategy:</strong> Ricerca per ISBN/ISSN con matching esatto e parziale</li>
 *   <li><strong>TitleSearchStrategy:</strong> Ricerca per titolo con matching parziale</li>
 * </ul>
 *
 * <p><strong>Scenari di test coperti:</strong></p>
 * <ul>
 *   <li>Ricerca con risultati multipli</li>
 *   <li>Ricerca senza risultati</li>
 *   <li>Ricerca con query edge cases</li>
 *   <li>Comportamento con dati null/vuoti</li>
 *   <li>Case sensitivity e matching parziale</li>
 * </ul>
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
 */
class SearchStrategyTest {
    
    private List<LibraryItem> testItems;
    private IdSearchStrategy idSearchStrategy;
    private TitleSearchStrategy titleSearchStrategy;
    
    @BeforeEach
    public void setUp() throws InvalidDataException {
        testItems = new ArrayList<>();
        
        // Add test books with various ISBN formats
        testItems.add(LibraryItemFactory.createBook("978-0134685991", "Effective Java", "Joshua Bloch", 416));
        testItems.add(LibraryItemFactory.createBook("978-0596009205", "Head First Design Patterns", "Eric Freeman", 694));
        testItems.add(LibraryItemFactory.createBook("123-4567890123", "Java Programming", "John Doe", 300));
        testItems.add(LibraryItemFactory.createBook("987-6543210987", "Advanced Java", "Jane Smith", 500));
        
        // Add test magazines with various ISSN formats
        testItems.add(LibraryItemFactory.createMagazine("1234-5678", "Java Magazine", 45));
        testItems.add(LibraryItemFactory.createMagazine("9876-5432", "Programming Weekly", 12));
        testItems.add(LibraryItemFactory.createMagazine("1111-2222", "Tech Today", 8));
        
        idSearchStrategy = new IdSearchStrategy();
        titleSearchStrategy = new TitleSearchStrategy();
    }
    
    @Test
    @DisplayName("Should find exact match by complete ISBN")
    void testExactMatchByCompleteISBN() {
        List<LibraryItem> results = idSearchStrategy.search(testItems, "978-0134685991");
        
        assertEquals(1, results.size());
        assertEquals("Effective Java", results.get(0).getTitle());
    }
    
    @Test
    @DisplayName("Should find exact match by complete ISSN")
    void testExactMatchByCompleteISSN() {
        List<LibraryItem> results = idSearchStrategy.search(testItems, "1234-5678");
        
        assertEquals(1, results.size());
        assertEquals("Java Magazine", results.get(0).getTitle());
    }
    
    @Test
    @DisplayName("Should find partial match with 3 digits")
    void testPartialMatchWith3Digits() {
        List<LibraryItem> results = idSearchStrategy.search(testItems, "134");
        
        assertEquals(1, results.size());
        assertEquals("Effective Java", results.get(0).getTitle());
    }
    
    @Test
    @DisplayName("Should find partial match with 4 digits")
    void testPartialMatchWith4Digits() {
        List<LibraryItem> results = idSearchStrategy.search(testItems, "1234");
        
        // Should find both the book with ISBN containing "1234" and magazine with ISSN "1234-5678"
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(item -> item.getTitle().equals("Java Programming")));
        assertTrue(results.stream().anyMatch(item -> item.getTitle().equals("Java Magazine")));
    }
    
    @Test
    @DisplayName("Should find multiple items with partial match")
    void testMultipleItemsPartialMatch() {
        List<LibraryItem> results = idSearchStrategy.search(testItems, "987");
        
        // Should find book with ISBN "987-6543210987" and magazine with ISSN "9876-5432"
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(item -> item.getTitle().equals("Advanced Java")));
        assertTrue(results.stream().anyMatch(item -> item.getTitle().equals("Programming Weekly")));
    }
    
    @Test
    @DisplayName("Should not find match with less than 3 digits")
    void testNoMatchWithLessThan3Digits() {
        List<LibraryItem> results = idSearchStrategy.search(testItems, "12");
        
        assertEquals(0, results.size());
    }
    
    @Test
    @DisplayName("Should not find match with only 2 digits")
    void testNoMatchWithOnly2Digits() {
        List<LibraryItem> results = idSearchStrategy.search(testItems, "98");
        
        assertEquals(0, results.size());
    }
    
    @Test
    @DisplayName("Should handle query with non-digit characters")
    void testQueryWithNonDigitCharacters() {
        List<LibraryItem> results = idSearchStrategy.search(testItems, "978-013");
        
        // Should extract "978013" and find partial match
        assertEquals(1, results.size());
        assertEquals("Effective Java", results.get(0).getTitle());
    }
    
    @Test
    @DisplayName("Should handle empty query")
    void testEmptyQuery() {
        List<LibraryItem> results = idSearchStrategy.search(testItems, "");
        
        assertEquals(0, results.size());
    }
    
    @Test
    @DisplayName("Should handle null query")
    void testNullQuery() {
        List<LibraryItem> results = idSearchStrategy.search(testItems, null);
        
        assertEquals(0, results.size());
    }
    
    @Test
    @DisplayName("Should handle whitespace-only query")
    void testWhitespaceOnlyQuery() {
        List<LibraryItem> results = idSearchStrategy.search(testItems, "   ");
        
        assertEquals(0, results.size());
    }
    
    @Test
    @DisplayName("Should find case-insensitive exact match")
    void testCaseInsensitiveExactMatch() {
        List<LibraryItem> results = idSearchStrategy.search(testItems, "978-0134685991".toUpperCase());
        
        assertEquals(1, results.size());
        assertEquals("Effective Java", results.get(0).getTitle());
    }
    
    @Test
    @DisplayName("Should find partial match with longer digit sequence")
    void testPartialMatchWithLongerSequence() {
        List<LibraryItem> results = idSearchStrategy.search(testItems, "0134685991");
        
        assertEquals(1, results.size());
        assertEquals("Effective Java", results.get(0).getTitle());
    }
    
    @Test
    @DisplayName("Title search should work as before")
    void testTitleSearchStillWorks() {
        List<LibraryItem> results = titleSearchStrategy.search(testItems, "Java");

        // Should find all items with "Java" in title
        assertEquals(4, results.size());
        assertTrue(results.stream().anyMatch(item -> item.getTitle().equals("Effective Java")));
        assertTrue(results.stream().anyMatch(item -> item.getTitle().equals("Java Programming")));
        assertTrue(results.stream().anyMatch(item -> item.getTitle().equals("Advanced Java")));
        assertTrue(results.stream().anyMatch(item -> item.getTitle().equals("Java Magazine")));
    }
    
    @Test
    @DisplayName("Should handle query with mixed characters and sufficient digits")
    void testMixedCharactersWithSufficientDigits() {
        List<LibraryItem> results = idSearchStrategy.search(testItems, "abc123def456");

        // Should extract "123456" and find partial matches
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(item -> item.getTitle().equals("Java Programming")));
        assertTrue(results.stream().anyMatch(item -> item.getTitle().equals("Java Magazine")));
    }
    
    @Test
    @DisplayName("Should not match when digits are insufficient after extraction")
    void testInsufficientDigitsAfterExtraction() {
        List<LibraryItem> results = idSearchStrategy.search(testItems, "ab12cd");
        
        // Should extract "12" which is less than 3 digits
        assertEquals(0, results.size());
    }
}
