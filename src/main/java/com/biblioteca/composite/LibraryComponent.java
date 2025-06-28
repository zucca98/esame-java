package com.biblioteca.composite;

import java.util.List;

/**
 * Component interface for Composite Pattern.
 * Represents both individual items and collections of items.
 */
public interface LibraryComponent {
    void displayInfo();
    int getItemCount();
    void add(LibraryComponent component);
    void remove(LibraryComponent component);
    List<LibraryComponent> getChildren();
}