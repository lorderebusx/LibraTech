package models;

public class Article extends LibraryItem {
    private String author;
    private String publication; // e.g., "Journal of Computer Science"

    public Article(String title, String author, String publication) {
        super(title);
        this.author = author;
        this.publication = publication;
    }
    
    // Private constructor for CSV loading
    private Article(String id, String title, String author, String publication, ItemStatus status) {
        super(id, title, status);
        this.author = author;
        this.publication = publication;
    }
    
    @Override
    public void display() {
        System.out.println("Type: Article");
        System.out.println("ID: " + getItemId());
        System.out.println("Title: " + getTitle());
        System.out.println("Author: " + author);
        System.out.println("Publication: " + publication);
        System.out.println("Status: " + getStatus());
    }

    @Override
    public boolean matches(String query) {
        String lowerCaseQuery = query.toLowerCase();
        return super.matches(lowerCaseQuery) ||
               author.toLowerCase().contains(lowerCaseQuery) ||
               publication.toLowerCase().contains(lowerCaseQuery);
    }
    
    // --- CSV Methods ---
    public String toCsvString() {
        return String.join(",", getItemId(), getTitle(), author, publication, getStatus().toString());
    }

    public static Article fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");
        return new Article(parts[0], parts[1], parts[2], parts[3], ItemStatus.valueOf(parts[4]));
    }
}
