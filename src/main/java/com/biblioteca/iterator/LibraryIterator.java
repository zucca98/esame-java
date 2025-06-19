package com.biblioteca.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.biblioteca.model.LibraryItem;

/**
 * Custom Iterator Pattern implementation for LibraryItem collections.
 */
public class LibraryIterator implements Iterator<LibraryItem> {
    private final List<LibraryItem> items;
    private int position = 0;
    
    public LibraryIterator(List<LibraryItem> items) {
        this.items = new ArrayList<>(items);
    }
    
    @Override
    public boolean hasNext() {
        return position < items.size();
    }
    
    @Override
    public LibraryItem next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more items");
        }
        return items.get(position++);
    }
    
    public void reset() {
        position = 0;
    }
    
    public int getCurrentPosition() {
        return position;
    }
}
