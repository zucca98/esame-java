package com.biblioteca.model;

/**
 * Book class implementing LibraryItem.
 * Supports Builder Pattern for construction.
 */
public class Book implements LibraryItem {
    private final String isbn;
    private final String title;
    private final String author;
    private final int pages;
    private boolean available;
    
    private Book(BookBuilder builder) {
        this.isbn = builder.isbn;
        this.title = builder.title;
        this.author = builder.author;
        this.pages = builder.pages;
        this.available = true;
    }
    
    @Override
    public String getId() {
        return isbn;
    }
    
    @Override
    public String getTitle() {
        return title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public int getPages() {
        return pages;
    }
    
    @Override
    public String getType() {
        return "Book";
    }
    
    @Override
    public boolean isAvailable() {
        return available;
    }
    
    @Override
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    @Override
    public void displayInfo() {
        System.out.printf("Book: %s by %s (ISBN: %s, Pages: %d, Available: %s)%n",
                title, author, isbn, pages, available ? "Yes" : "No");
    }
    
    // Builder Pattern Implementation
    public static class BookBuilder {
        private String isbn;
        private String title;
        private String author;
        private int pages;
        
        public BookBuilder setIsbn(String isbn) {
            this.isbn = isbn;
            return this;
        }
        
        public BookBuilder setTitle(String title) {
            this.title = title;
            return this;
        }
        
        public BookBuilder setAuthor(String author) {
            this.author = author;
            return this;
        }
        
        public BookBuilder setPages(int pages) {
            this.pages = pages;
            return this;
        }
        
        public Book build() {
            if (isbn == null || title == null || author == null) {
                throw new IllegalArgumentException("ISBN, title, and author are required");
            }
            return new Book(this);
        }
    }
}
