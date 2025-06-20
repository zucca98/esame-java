package com.biblioteca.manager;

import com.biblioteca.exceptions.*;
import com.biblioteca.factory.LibraryItemFactory;
import com.biblioteca.iterator.LibraryCollection;
import com.biblioteca.model.LibraryItem;
import com.biblioteca.strategy.SearchStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Singleton Pattern implementation for Library Management.
 * Central manager for all library operations.
 */
public class LibraryManager {
    private static final Logger logger = LoggerFactory.getLogger(LibraryManager.class);
    private static LibraryManager instance;
    
    private final LibraryCollection collection;
    private final Map<String, LibraryItem> itemsById;
    private final String dataFilePath = "data/library.txt";
    
    private LibraryManager() {
        collection = new LibraryCollection();
        itemsById = new HashMap<>();
        logger.info("Library Manager initialized");
    }
    
    public static synchronized LibraryManager getInstance() {
        if (instance == null) {
            instance = new LibraryManager();
        }
        return instance;
    }
    
    public void addBook(String isbn, String title, String author, int pages) 
            throws LibraryException {
        try {
            if (itemsById.containsKey(isbn)) {
                throw new InvalidDataException("Book with ISBN " + isbn + " already exists");
            }
            
            LibraryItem book = LibraryItemFactory.createBook(isbn, title, author, pages);
            collection.addItem(book);
            itemsById.put(isbn, book);
            
            logger.info("Added book: {} by {}", title, author);
        } catch (InvalidDataException e) {
            logger.error("Error adding book: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error adding book: {}", e.getMessage());
            throw new LibraryException("Failed to add book", e);
        }
    }
    
    public void addMagazine(String issn, String title, int issueNumber) 
            throws LibraryException {
        try {
            if (itemsById.containsKey(issn)) {
                throw new InvalidDataException("Magazine with ISSN " + issn + " already exists");
            }
            
            LibraryItem magazine = LibraryItemFactory.createMagazine(issn, title, issueNumber);
            collection.addItem(magazine);
            itemsById.put(issn, magazine);
            
            logger.info("Added magazine: {} Issue #{}", title, issueNumber);
        } catch (InvalidDataException e) {
            logger.error("Error adding magazine: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error adding magazine: {}", e.getMessage());
            throw new LibraryException("Failed to add magazine", e);
        }
    }
    
    public LibraryItem findById(String id) throws BookNotFoundException {
        LibraryItem item = itemsById.get(id);
        if (item == null) {
            throw new BookNotFoundException(id);
        }
        return item;
    }
    
    public List<LibraryItem> search(SearchStrategy strategy, String query) {
        logger.info("Searching with query: {}", query);
        return strategy.search(collection.getItems(), query);
    }
    
    public void displayAllItems() {
        logger.info("Displaying all library items");
        System.out.println("=== Library Collection ===");
        collection.forEach(LibraryItem::displayInfo);
    }
    
    public void saveToFile() throws LibraryException {
        try {
            Files.createDirectories(Paths.get("data"));
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(dataFilePath))) {
                for (LibraryItem item : collection) {
                    writer.println(itemToString(item));
                }
            }
            
            logger.info("Library data saved to file");
        } catch (IOException e) {
            logger.error("Error saving to file: {}", e.getMessage());
            throw new LibraryException("Failed to save data", e);
        }
    }
    
    public void loadFromFile() throws LibraryException {
        try {
            if (!Files.exists(Paths.get(dataFilePath))) {
                logger.info("Data file not found, starting with empty library");
                return;
            }
            
            try (BufferedReader reader = new BufferedReader(new FileReader(dataFilePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    parseItemFromString(line);
                }
            }
            
            logger.info("Library data loaded from file");
        } catch (IOException e) {
            logger.error("Error loading from file: {}", e.getMessage());
            throw new LibraryException("Failed to load data", e);
        }
    }
    
    private String itemToString(LibraryItem item) {
        return String.format("%s|%s|%s|%s", 
                item.getType(), item.getId(), item.getTitle(), item.isAvailable());
    }
    
    private void parseItemFromString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length >= 4) {
            try {
                String type = parts[0];
                String id = parts[1];
                String title = parts[2];
                // Note: availability will be set after item creation
                
                // Simplified parsing - in real implementation would be more complete
                LibraryItem item = null;
                if ("Book".equals(type)) {
                    addBook(id, title, "Unknown Author", 100);
                    item = findById(id);
                } else if ("Magazine".equals(type)) {
                    addMagazine(id, title, 1);
                    item = findById(id);
                }
                
                // Set availability if item was created
                if (item != null && parts.length > 3) {
                    boolean available = Boolean.parseBoolean(parts[3]);
                    item.setAvailable(available);
                }
            } catch (LibraryException e) {
                logger.warn("Failed to parse item: {}", line);
            }
        }
    }
    
    public int getTotalItems() {
        return collection.size();
    }
}