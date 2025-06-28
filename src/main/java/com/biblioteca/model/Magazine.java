package com.biblioteca.model;

/**
 * Magazine class implementing LibraryItem.
 */
public class Magazine implements LibraryItem {
    private final String issn;
    private final String title;
    private final int issueNumber;
    private boolean available;
    
    public Magazine(String issn, String title, int issueNumber) {
        this.issn = issn;
        this.title = title;
        this.issueNumber = issueNumber;
        this.available = true;
    }
    
    @Override
    public String getId() {
        return issn;
    }
    
    @Override
    public String getTitle() {
        return title;
    }
    
    public int getIssueNumber() {
        return issueNumber;
    }
    
    @Override
    public String getType() {
        return "Magazine";
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
        System.out.printf("Magazine: %s Issue #%d (ISSN: %s, Available: %s)%n",
                title, issueNumber, issn, available ? "Yes" : "No");
    }
}