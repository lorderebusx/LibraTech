package models;

public class Book extends LibraryItem {
    private String author;
    private String isbn;

    public Book(String title, String author, String isbn) {
        super(title);
        this.author = author;
        this.isbn = isbn;
    }

    private Book(String id, String title, String author, String isbn, ItemStatus status) {
        super(id, title, status);
        this.author = author;
        this.isbn = isbn;
    }

    @Override
    public void display() {
        System.out.println("Type: Book");
        System.out.println("ID: " + getItemId());
        System.out.println("Title: " + getTitle());
        System.out.println("Author: " + author);
        System.out.println("ISBN: " + isbn);
        System.out.println("Status: " + getStatus());
    }
    
    @Override
    public boolean matches(String query) {
        String lcQuery = query.toLowerCase();
        return super.matches(lcQuery) || author.toLowerCase().contains(lcQuery) || isbn.toLowerCase().contains(lcQuery);
    }
    
    public String toCsvString() {
        final String DELIMITER = ";";
        return String.join(DELIMITER, getItemId(), getTitle(), author, isbn, getStatus().toString());
    }

    public static Book fromCsvString(String csvLine) {
        final String DELIMITER = ";";
        String[] parts = csvLine.split(DELIMITER);
        return new Book(parts[0], parts[1], parts[2], parts[3], ItemStatus.valueOf(parts[4]));
    }
}

