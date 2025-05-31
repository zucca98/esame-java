package com.biblioteca.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Composite class for managing categories and subcategories.
 * Implements Composite Pattern.
 */
public class Category implements LibraryComponent {
    private final String name;
    private final List<LibraryComponent> children;
    
    public Category(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }
    
    @Override
    public void add(LibraryComponent component) {
        children.add(component);
    }
    
    @Override
    public void remove(LibraryComponent component) {
        children.remove(component);
    }
    
    @Override
    public List<LibraryComponent> getChildren() {
        return Collections.unmodifiableList(children);
    }
    
    @Override
    public void displayInfo() {
        System.out.println("Category: " + name + " (" + getItemCount() + " items)");
        for (LibraryComponent child : children) {
            System.out.print("  ");
            child.displayInfo();
        }
    }
    
    @Override
    public int getItemCount() {
        return children.stream()
                .mapToInt(LibraryComponent::getItemCount)
                .sum();
    }
    
    public String getName() {
        return name;
    }
}
