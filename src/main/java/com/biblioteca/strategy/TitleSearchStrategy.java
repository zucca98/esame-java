
package com.biblioteca.strategy;

import java.util.List;
import java.util.stream.Collectors;

import com.biblioteca.model.LibraryItem;

/**
 * Concrete strategy for searching by title.
 * Uses Stream API and Lambdas (optional feature).
 */
public class TitleSearchStrategy implements SearchStrategy {
    
    @Override
    public List<LibraryItem> search(List<LibraryItem> items, String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }

        return items.stream()
                .filter(item -> item.getTitle() != null &&
                        item.getTitle().toLowerCase()
                                .contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}