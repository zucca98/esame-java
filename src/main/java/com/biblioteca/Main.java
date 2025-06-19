package com.biblioteca;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.biblioteca.exceptions.LibraryException;
import com.biblioteca.manager.LibraryManager;
import com.biblioteca.model.LibraryItem;
import com.biblioteca.strategy.IdSearchStrategy;
import com.biblioteca.strategy.SearchStrategy;
import com.biblioteca.strategy.TitleSearchStrategy;

/**
 * Main class for the Library Management System
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    
public static void main(String[] args) {
    // Start the system
    logger.info("Starting Library Management System");
    
    try (Scanner scanner = new Scanner(System.in)) {
        LibraryManager manager = LibraryManager.getInstance();
        
        // Demo the system
        demonstrateSystem(manager);
        
        // Load existing data
        if (manager.getTotalItems() > 0) {
            manager.loadFromFile();
        }
        
        // Interactive menu
        runInteractiveMenu(manager, scanner);
        
    } catch (Exception e) {
        logger.error("System error: {}", e.getMessage());
        System.err.println("System error occurred. Please check logs.");
    } finally {
        logger.info("Library Management System stopped");
    }
}
    /**
     * Demonstrates the functionality of the Library Management System
     * @param manager The LibraryManager instance
     *
     */
    private static void demonstrateSystem(LibraryManager manager) {
        System.out.println("=== Library Management System Demo ===\n");
        
        try {
            // Add some sample data
            manager.addBook("978-0134685991", "Effective Java", "Joshua Bloch", 416);
            manager.addBook("978-0596009205", "Head First Design Patterns", "Eric Freeman", 694);
            manager.addMagazine("1234-5678", "Java Magazine", 45);
            
            System.out.println("Sample data added successfully!\n");
            
            // Demonstrate search strategies
            demonstrateSearchStrategies(manager);
            
            // Display all items
            manager.displayAllItems();
            
            // Save data
            manager.saveToFile();
            
        } catch (LibraryException e) {
            System.err.println("Demo error: " + e.getMessage());
        }
    }
    /**
     * Demonstrates the search capabilities of the Library Management System
     * @param manager
     * 
     */
    private static void demonstrateSearchStrategies(LibraryManager manager) {
        System.out.println("=== Search Demonstration ===");
        
        // Search by title
        var titleResults = manager.search(new TitleSearchStrategy(), "Java");
        System.out.println("Title search for 'Java' found " + titleResults.size() + " items:");
        titleResults.forEach(item -> System.out.println("  - " + item.getTitle()));
        
        // Search by ID
        var idResults = manager.search(new IdSearchStrategy(), "978-0134685991");
        System.out.println("\nID search found " + idResults.size() + " items:");
        idResults.forEach(item -> System.out.println("  - " + item.getTitle()));
        
        System.out.println();
    }
    
    /**
     * Runs the interactive menu for the Library Management System
     * @param manager The LibraryManager instance
     * @param scanner The Scanner instance
     */
    private static void runInteractiveMenu(LibraryManager manager, Scanner scanner) {
        while (true) {
            displayMenu();
            String choice = getValidatedInput("Enter your choice: ", scanner);
            
            try {
                switch (choice) {
                    case "1" -> addNewBook(manager, scanner);
                    case "2" -> addNewMagazine(manager, scanner);
                    case "3" -> searchItems(manager, scanner);
                    case "4" -> manager.displayAllItems();
                    case "5" -> {
                        manager.saveToFile();
                        System.out.println("Data saved successfully!");
                    }
                    case "6" -> {
                        System.out.println("Total items: " + manager.getTotalItems());
                    }
                    case "0" -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (LibraryException e) {
                System.err.println("Error: " + e.getMessage());
            }
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }
    /**
     * Displays the menu for the Library Management System
     */
    private static void displayMenu() {
        System.out.println("\n=== Library Management System ===");
        System.out.println("1. Add Book");
        System.out.println("2. Add Magazine");
        System.out.println("3. Search Items");
        System.out.println("4. Display All Items");
        System.out.println("5. Save Data");
        System.out.println("6. Show Statistics");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }
    
    private static void addNewBook(LibraryManager manager, Scanner scanner) throws LibraryException {
        System.out.println("\n=== Add New Book ===");
        String isbn = getValidatedInput("ISBN: ", scanner);
        String title = getValidatedInput("Title: ", scanner);
        String author = getValidatedInput("Author: ", scanner);
        int pages = getValidatedIntInput("Pages: ", scanner);
        
        manager.addBook(isbn, title, author, pages);
        System.out.println("Book added successfully!");
    }
    
    private static void addNewMagazine(LibraryManager manager, Scanner scanner) throws LibraryException {
        System.out.println("\n=== Add New Magazine ===");
        String issn = getValidatedInput("ISSN: ", scanner);
        String title = getValidatedInput("Title: ", scanner);
        int issueNumber = getValidatedIntInput("Issue Number: ", scanner);
        
        manager.addMagazine(issn, title, issueNumber);
        System.out.println("Magazine added successfully!");
    }
    
    private static void searchItems(LibraryManager manager, Scanner scanner) {
        System.out.println("\n=== Search Items ===");
        System.out.println("1. Search by Title");
        System.out.println("2. Search by ID");
        String choice = getValidatedInput("Search type: ", scanner);
        String query = getValidatedInput("Search query: ", scanner);
        
        SearchStrategy strategy = switch (choice) {
            case "1" -> new TitleSearchStrategy();
            case "2" -> new IdSearchStrategy();
            default -> {
                System.out.println("Invalid choice, using title search");
                yield new TitleSearchStrategy();
            }
        };
        
        var results = manager.search(strategy, query);
        System.out.println("Found " + results.size() + " items:");
        results.forEach(LibraryItem::displayInfo);
    }
    
    private static String getValidatedInput(String prompt, Scanner scanner) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        while (input.isEmpty()) {
            System.out.print("Input cannot be empty. " + prompt);
            input = scanner.nextLine().trim();
        }
        return input;
    }
    
    private static int getValidatedIntInput(String prompt, Scanner scanner) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);
                if (value <= 0) {
                    System.out.println("Please enter a positive number.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}