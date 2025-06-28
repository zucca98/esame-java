package com.biblioteca.util;

import java.util.regex.Pattern;

/**
 * Utility class for validating author names.
 * Ensures author names contain meaningful text rather than just numeric identifiers.
 */
public class AuthorValidator {
    
    // Pattern to match strings that contain only digits, spaces, hyphens, dots, and other non-alphabetic separators
    private static final Pattern NUMERIC_ONLY_PATTERN = Pattern.compile("^[0-9\\s\\-\\.\\,\\(\\)\\[\\]\\{\\}\\_\\+\\=\\|\\\\\\/@#$%^&*~`]+$");
    
    // Pattern to check if string contains at least one alphabetic character
    private static final Pattern CONTAINS_LETTER_PATTERN = Pattern.compile(".*[a-zA-Z].*");
    
    /**
     * Validates that an author name is not purely numeric.
     * 
     * @param author the author name to validate
     * @return true if the author name is valid (contains alphabetic characters), false otherwise
     */
    public static boolean isValidAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            return false;
        }
        
        String trimmedAuthor = author.trim();
        
        // Check if the author name contains at least one alphabetic character
        return CONTAINS_LETTER_PATTERN.matcher(trimmedAuthor).matches();
    }
    
    /**
     * Validates that an author name is not purely numeric and throws an exception if invalid.
     * 
     * @param author the author name to validate
     * @throws IllegalArgumentException if the author name is invalid
     */
    public static void validateAuthor(String author) {
        if (!isValidAuthor(author)) {
            if (author == null || author.trim().isEmpty()) {
                throw new IllegalArgumentException("Author name cannot be empty");
            }
            
            String trimmedAuthor = author.trim();
            
            // Check if it's purely numeric/separators
            if (NUMERIC_ONLY_PATTERN.matcher(trimmedAuthor).matches()) {
                throw new IllegalArgumentException(
                    "Author name cannot consist entirely of numbers and separators. " +
                    "Please enter a valid author name with alphabetic characters."
                );
            }
            
            // Fallback for other cases
            throw new IllegalArgumentException(
                "Author name must contain at least some alphabetic characters"
            );
        }
    }
    
    /**
     * Checks if a string consists entirely of numeric characters and common separators.
     * 
     * @param text the text to check
     * @return true if the text is purely numeric with separators, false otherwise
     */
    public static boolean isPurelyNumeric(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        
        return NUMERIC_ONLY_PATTERN.matcher(text.trim()).matches();
    }
    
    /**
     * Gets a user-friendly error message for invalid author names.
     * 
     * @param author the invalid author name
     * @return a descriptive error message
     */
    public static String getValidationErrorMessage(String author) {
        if (author == null || author.trim().isEmpty()) {
            return "Author name cannot be empty";
        }
        
        if (isPurelyNumeric(author)) {
            return "Author name cannot consist entirely of numbers and separators (like '" + 
                   author.trim() + "'). Please enter a valid author name with alphabetic characters.";
        }
        
        return "Author name must contain at least some alphabetic characters";
    }
}
