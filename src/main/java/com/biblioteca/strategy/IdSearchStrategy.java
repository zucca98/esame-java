package com.biblioteca.strategy;

import java.util.List;
import java.util.stream.Collectors;

import com.biblioteca.model.LibraryItem;

/**
 * Concrete strategy for searching by ID (ISBN/ISSN).
 * Supports both exact matching and partial matching with at least 3 digits.
 */
public class IdSearchStrategy implements SearchStrategy {

    @Override
    public List<LibraryItem> search(List<LibraryItem> items, String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of(); // Return empty list for null or empty queries
        }

        String trimmedQuery = query.trim();

        // First, try exact match (case-insensitive)
        List<LibraryItem> exactMatches = items.stream()
                .filter(item -> item.getId() != null && item.getId().equalsIgnoreCase(trimmedQuery))
                .collect(Collectors.toList());

        // If we found exact matches, return them
        if (!exactMatches.isEmpty()) {
            return exactMatches;
        }

        // If no exact matches, try partial matching with digits
        String digitsOnly = trimmedQuery.replaceAll("[^0-9]", "");

        // Only do partial matching if we have at least 3 digits
        if (digitsOnly.length() >= 3) {
            return items.stream()
                    .filter(item -> {
                        String itemId = item.getId();
                        if (itemId == null) {
                            return false;
                        }

                        // Extract digits from the item ID
                        String itemDigits = itemId.replaceAll("[^0-9]", "");
                        // Check if item's digits contain the query digits as substring
                        return itemDigits.contains(digitsOnly);
                    })
                    .collect(Collectors.toList());
        }

        // If less than 3 digits, return empty list
        return List.of();
    }
}
