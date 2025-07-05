package com.biblioteca.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test suite per la classe di utilità AuthorValidator.
 *
 * <p>Questa classe di test verifica la logica di validazione dei nomi degli autori,
 * garantendo che il sistema accetti solo nomi significativi e rifiuti input
 * puramente numerici o non validi. I test coprono una vasta gamma di casi
 * per garantire robustezza e usabilità.</p>
 *
 * <p><strong>Aree di validazione testate:</strong></p>
 * <ul>
 *   <li><strong>Nomi validi:</strong> Vari formati di nomi reali</li>
 *   <li><strong>Nomi non validi:</strong> Input numerici e non significativi</li>
 *   <li><strong>Casi limite:</strong> Spazi, caratteri speciali, combinazioni</li>
 *   <li><strong>Messaggi di errore:</strong> Appropriatezza e chiarezza</li>
 * </ul>
 *
 * <p><strong>Strategia di testing:</strong></p>
 * <ul>
 *   <li>Test parametrizzati per efficienza</li>
 *   <li>Copertura di edge cases realistici</li>
 *   <li>Verifica messaggi di errore user-friendly</li>
 *   <li>Test di regressione per casi problematici</li>
 * </ul>
 *
 * @author Sistema Biblioteca
 * @version 1.0
 * @since 1.0
 */
class AuthorValidatorTest {

    @Test
    @DisplayName("Should accept valid author names with alphabetic characters")
    void testValidAuthorNames() {
        assertTrue(AuthorValidator.isValidAuthor("John Smith"));
        assertTrue(AuthorValidator.isValidAuthor("Jane Doe"));
        assertTrue(AuthorValidator.isValidAuthor("Mary O'Connor"));
        assertTrue(AuthorValidator.isValidAuthor("Jean-Pierre Dupont"));
        assertTrue(AuthorValidator.isValidAuthor("Dr. Smith"));
        assertTrue(AuthorValidator.isValidAuthor("Smith Jr."));
        assertTrue(AuthorValidator.isValidAuthor("A"));
        assertTrue(AuthorValidator.isValidAuthor("   John Smith   ")); // with spaces
    }

    @Test
    @DisplayName("Should accept author names with numbers as part of legitimate names")
    void testValidAuthorNamesWithNumbers() {
        assertTrue(AuthorValidator.isValidAuthor("John Smith Jr. 3rd"));
        assertTrue(AuthorValidator.isValidAuthor("O'Connor-2"));
        assertTrue(AuthorValidator.isValidAuthor("Smith III"));
        assertTrue(AuthorValidator.isValidAuthor("John 2nd"));
        assertTrue(AuthorValidator.isValidAuthor("Author123"));
        assertTrue(AuthorValidator.isValidAuthor("123Author"));
        assertTrue(AuthorValidator.isValidAuthor("Au123thor"));
        assertTrue(AuthorValidator.isValidAuthor("John Smith 1984"));
    }

    @Test
    @DisplayName("Should reject purely numeric author names")
    void testInvalidNumericOnlyAuthorNames() {
        assertFalse(AuthorValidator.isValidAuthor("123"));
        assertFalse(AuthorValidator.isValidAuthor("456789"));
        assertFalse(AuthorValidator.isValidAuthor("0"));
        assertFalse(AuthorValidator.isValidAuthor("999999999"));
    }

    @Test
    @DisplayName("Should reject numeric names with separators")
    void testInvalidNumericWithSeparators() {
        assertFalse(AuthorValidator.isValidAuthor("123-456"));
        assertFalse(AuthorValidator.isValidAuthor("456 789"));
        assertFalse(AuthorValidator.isValidAuthor("12-34"));
        assertFalse(AuthorValidator.isValidAuthor("123.456"));
        assertFalse(AuthorValidator.isValidAuthor("123,456"));
        assertFalse(AuthorValidator.isValidAuthor("123_456"));
        assertFalse(AuthorValidator.isValidAuthor("123 456 789"));
        assertFalse(AuthorValidator.isValidAuthor("(123) 456-7890"));
        assertFalse(AuthorValidator.isValidAuthor("[123] 456"));
        assertFalse(AuthorValidator.isValidAuthor("123/456"));
        assertFalse(AuthorValidator.isValidAuthor("123@456"));
        assertFalse(AuthorValidator.isValidAuthor("123#456"));
        assertFalse(AuthorValidator.isValidAuthor("123$456"));
        assertFalse(AuthorValidator.isValidAuthor("123%456"));
        assertFalse(AuthorValidator.isValidAuthor("123^456"));
        assertFalse(AuthorValidator.isValidAuthor("123&456"));
        assertFalse(AuthorValidator.isValidAuthor("123*456"));
        assertFalse(AuthorValidator.isValidAuthor("123+456"));
        assertFalse(AuthorValidator.isValidAuthor("123=456"));
        assertFalse(AuthorValidator.isValidAuthor("123|456"));
        assertFalse(AuthorValidator.isValidAuthor("123\\456"));
        assertFalse(AuthorValidator.isValidAuthor("123~456"));
        assertFalse(AuthorValidator.isValidAuthor("123`456"));
    }

    @Test
    @DisplayName("Should reject null and empty author names")
    void testNullAndEmptyAuthorNames() {
        assertFalse(AuthorValidator.isValidAuthor(null));
        assertFalse(AuthorValidator.isValidAuthor(""));
        assertFalse(AuthorValidator.isValidAuthor("   "));
        assertFalse(AuthorValidator.isValidAuthor("\t"));
        assertFalse(AuthorValidator.isValidAuthor("\n"));
    }

    @Test
    @DisplayName("Should correctly identify purely numeric strings")
    void testIsPurelyNumeric() {
        assertTrue(AuthorValidator.isPurelyNumeric("123"));
        assertTrue(AuthorValidator.isPurelyNumeric("123-456"));
        assertTrue(AuthorValidator.isPurelyNumeric("456 789"));
        assertTrue(AuthorValidator.isPurelyNumeric("123.456.789"));
        assertTrue(AuthorValidator.isPurelyNumeric("(123) 456-7890"));
        
        assertFalse(AuthorValidator.isPurelyNumeric("John123"));
        assertFalse(AuthorValidator.isPurelyNumeric("123John"));
        assertFalse(AuthorValidator.isPurelyNumeric("John Smith"));
        assertFalse(AuthorValidator.isPurelyNumeric(""));
        assertFalse(AuthorValidator.isPurelyNumeric(null));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for invalid authors in validateAuthor")
    void testValidateAuthorThrowsException() {
        // Test null
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> AuthorValidator.validateAuthor(null)
        );
        assertTrue(exception.getMessage().contains("Author name cannot be empty"));

        // Test empty
        exception = assertThrows(
            IllegalArgumentException.class,
            () -> AuthorValidator.validateAuthor("")
        );
        assertTrue(exception.getMessage().contains("Author name cannot be empty"));

        // Test purely numeric
        exception = assertThrows(
            IllegalArgumentException.class,
            () -> AuthorValidator.validateAuthor("123")
        );
        assertTrue(exception.getMessage().contains("cannot consist entirely of numbers"));

        // Test numeric with separators
        exception = assertThrows(
            IllegalArgumentException.class,
            () -> AuthorValidator.validateAuthor("123-456")
        );
        assertTrue(exception.getMessage().contains("cannot consist entirely of numbers"));
    }

    @Test
    @DisplayName("Should not throw exception for valid authors in validateAuthor")
    void testValidateAuthorDoesNotThrow() {
        // These should not throw any exceptions
        AuthorValidator.validateAuthor("John Smith");
        AuthorValidator.validateAuthor("Jane Doe");
        AuthorValidator.validateAuthor("John Smith Jr. 3rd");
        AuthorValidator.validateAuthor("O'Connor-2");
        AuthorValidator.validateAuthor("Author123");
    }

    @Test
    @DisplayName("Should provide appropriate error messages")
    void testGetValidationErrorMessage() {
        String message = AuthorValidator.getValidationErrorMessage(null);
        assertEquals("Author name cannot be empty", message);

        message = AuthorValidator.getValidationErrorMessage("");
        assertEquals("Author name cannot be empty", message);

        message = AuthorValidator.getValidationErrorMessage("123");
        assertTrue(message.contains("cannot consist entirely of numbers"));
        assertTrue(message.contains("'123'"));

        message = AuthorValidator.getValidationErrorMessage("123-456");
        assertTrue(message.contains("cannot consist entirely of numbers"));
        assertTrue(message.contains("'123-456'"));
    }

    @Test
    @DisplayName("Should handle edge cases with whitespace")
    void testWhitespaceEdgeCases() {
        // Valid cases with whitespace
        assertTrue(AuthorValidator.isValidAuthor("  John Smith  "));
        assertTrue(AuthorValidator.isValidAuthor("\tJane Doe\n"));
        
        // Invalid cases with whitespace
        assertFalse(AuthorValidator.isValidAuthor("  123  "));
        assertFalse(AuthorValidator.isValidAuthor("\t456-789\n"));
    }

    @Test
    @DisplayName("Should handle special characters correctly")
    void testSpecialCharacters() {
        // Valid names with special characters
        assertTrue(AuthorValidator.isValidAuthor("José María"));
        assertTrue(AuthorValidator.isValidAuthor("François"));
        assertTrue(AuthorValidator.isValidAuthor("Müller"));
        assertTrue(AuthorValidator.isValidAuthor("O'Brien"));
        assertTrue(AuthorValidator.isValidAuthor("Smith-Jones"));
        
        // Invalid - only special characters and numbers
        assertFalse(AuthorValidator.isValidAuthor("123-456-789"));
        assertFalse(AuthorValidator.isValidAuthor("!@#$%^&*()"));
    }
}
