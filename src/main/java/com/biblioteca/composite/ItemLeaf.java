package com.biblioteca.composite;

import java.util.Collections;
import java.util.List;

import com.biblioteca.model.LibraryItem;

/**
 * Leaf class for individual library items in Composite Pattern.
 */
public class ItemLeaf implements LibraryComponent {
    private final LibraryItem item;
    
    public ItemLeaf(LibraryItem item) {
        this.item = item;
    }
    
    @Override
    public void displayInfo() {
        item.displayInfo();
    }
    
    @Override
    public int getItemCount() {
        return 1;
    }
    
    @Override
    public void add(LibraryComponent component) {
        throw new UnsupportedOperationException("Cannot add to a leaf");
    }
    
    @Override
    public void remove(LibraryComponent component) {
        throw new UnsupportedOperationException("Cannot remove from a leaf");
    }
    
    @Override
    public List<LibraryComponent> getChildren() {
        return Collections.emptyList();
    }
    
    public LibraryItem getItem() {
        return item;
    }
}
