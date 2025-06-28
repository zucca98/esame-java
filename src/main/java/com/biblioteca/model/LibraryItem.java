
package com.biblioteca.model;

/**
 * Base interface for all library items.
 * Used with Factory Pattern and Composite Pattern.
 */
public interface LibraryItem {
    String getId();
    String getTitle();
    String getType();
    boolean isAvailable();
    void setAvailable(boolean available);
    void displayInfo();
}