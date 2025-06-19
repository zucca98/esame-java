package com.biblioteca.strategy;

import java.util.List;

import com.biblioteca.model.LibraryItem;

/**
 * Strategy Pattern interface for different search algorithms.
 */
public interface SearchStrategy {
    List<LibraryItem> search(List<LibraryItem> items, String query);
}
