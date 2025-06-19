package com.biblioteca.strategy;

import java.util.List;
import java.util.stream.Collectors;

import com.biblioteca.model.LibraryItem;

/**
 * Concrete strategy for searching by ID (ISBN/ISSN).
 */
public class IdSearchStrategy implements SearchStrategy {
    
    @Override
    public List<LibraryItem> search(List<LibraryItem> items, String query) {
        return items.stream()
                .filter(item -> item.getId().equalsIgnoreCase(query))
                .collect(Collectors.toList());
    }
}
