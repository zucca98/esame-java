package com.biblioteca.iterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.biblioteca.model.LibraryItem;

/**
 * Collection class that provides custom iterator for LibraryItem objects.
 * Demonstrates Iterator Pattern implementation.
 */
public class LibraryCollection implements Iterable<LibraryItem> {
    private final List<LibraryItem> items;
    
    public LibraryCollection() {
        this.items = new ArrayList<>();
    }
    
    public void addItem(LibraryItem item) {
        items.add(item);
    }
    
    public void removeItem(LibraryItem item) {
        items.remove(item);
    }
    
    public int size() {
        return items.size();
    }
    
    public LibraryIterator createIterator() {
        return new LibraryIterator(items);
    }
    
    @Override
    public Iterator<LibraryItem> iterator() {
        return createIterator();
    }
    
    public List<LibraryItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}