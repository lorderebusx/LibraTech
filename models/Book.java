package models;

/**
 * Represents a Book, a specific type of LibraryItem.
 * This demonstrates INHERITANCE.
 */
public class Book extends LibraryItem {
    private String author;
    private String isbn;

    public Book(String title, String author, String isbn) {
        super(title); // Call the constructor of the parent class
        this.author = author;
        this.isbn = isbn;
    }

    // --- Implementation of the abstract method ---
    @Override
    public void display() {
        System.out.println("Type: Book");
        System.out.println("ID: " + getItemId());
        System.out.println("Title: " + getTitle());
        System.out.println("Author: " + author);
        System.out.println("ISBN: " + isbn);
        System.out.println("Status: " + getStatus());
    }
    
    // --- Overriding for more specific search ---
    @Override
    public boolean matches(String query) {
        String lowerCaseQuery = query.toLowerCase();
        // Check parent's matches() method first, then check specific fields
        return super.matches(lowerCaseQuery) || 
               author.toLowerCase().contains(lowerCaseQuery) ||
               isbn.toLowerCase().contains(lowerCaseQuery);
    }

    // --- Getters and Setters ---
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}

