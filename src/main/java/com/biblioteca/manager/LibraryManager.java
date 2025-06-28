package com.biblioteca.manager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.biblioteca.exceptions.BookNotFoundException;
import com.biblioteca.exceptions.InvalidDataException;
import com.biblioteca.exceptions.LibraryException;
import com.biblioteca.factory.LibraryItemFactory;
import com.biblioteca.iterator.LibraryCollection;
import com.biblioteca.model.LibraryItem;
import com.biblioteca.strategy.SearchStrategy;

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
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        return switch (item) {
            case com.biblioteca.model.Book book -> String.format("%s|%s|%s|%s|%d|%s",
                    item.getType(), item.getId(), item.getTitle(),
                    book.getAuthor(), book.getPages(), item.isAvailable());
            case com.biblioteca.model.Magazine magazine -> String.format("%s|%s|%s|%d|%s",
                    item.getType(), item.getId(), item.getTitle(),
                    magazine.getIssueNumber(), item.isAvailable());
            default -> String.format("%s|%s|%s|%s",
                    item.getType(), item.getId(), item.getTitle(), item.isAvailable());
        };
    }
    
    private void parseItemFromString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length >= 3) {
            try {
                String type = parts[0];
                String id = parts[1];
                String title = parts[2];

                if ("Book".equals(type)) {
                    // New format: Book|ID|Title|Author|Pages|Available
                    if (parts.length >= 6) {
                        String author = parts[3];
                        int pages = Integer.parseInt(parts[4]);
                        boolean available = Boolean.parseBoolean(parts[5]);

                        addBook(id, title, author, pages);
                        LibraryItem item = findById(id);
                        item.setAvailable(available);
                    } else {
                        // Legacy format: Book|ID|Title|Available - use defaults
                        logger.warn("Loading book with legacy format, using default values: {}", line);
                        addBook(id, title, "Unknown Author", 100);
                        LibraryItem item = findById(id);
                        if (parts.length > 3) {
                            boolean available = Boolean.parseBoolean(parts[3]);
                            item.setAvailable(available);
                        }
                    }
                } else if ("Magazine".equals(type)) {
                    // New format: Magazine|ID|Title|IssueNumber|Available
                    if (parts.length >= 5) {
                        int issueNumber = Integer.parseInt(parts[3]);
                        boolean available = Boolean.parseBoolean(parts[4]);

                        addMagazine(id, title, issueNumber);
                        LibraryItem item = findById(id);
                        item.setAvailable(available);
                    } else {
                        // Legacy format: Magazine|ID|Title|Available - use default
                        logger.warn("Loading magazine with legacy format, using default values: {}", line);
                        addMagazine(id, title, 1);
                        LibraryItem item = findById(id);
                        if (parts.length > 3) {
                            boolean available = Boolean.parseBoolean(parts[3]);
                            item.setAvailable(available);
                        }
                    }
                }
            } catch (LibraryException | NumberFormatException e) {
                logger.warn("Failed to parse item: {} - Error: {}", line, e.getMessage());
            }
        }
    }
    
    public int getTotalItems() {
        return collection.size();
    }
}
