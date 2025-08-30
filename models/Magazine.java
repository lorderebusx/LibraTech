package models;

public class Magazine extends LibraryItem {
    private String issueDate;

    public Magazine(String title, String issueDate) {
        super(title);
        this.issueDate = issueDate;
    }

    @Override
    public void display() {
        System.out.println("Type: Magazine");
        System.out.println("ID: " + getItemId());
        System.out.println("Title: " + getTitle());
        System.out.println("Issue Date: " + issueDate);
        System.out.println("Status: " + getStatus());
    }

    // --- Overriding for more specific search ---
    @Override
    public boolean matches(String query) {
        String lowerCaseQuery = query.toLowerCase();
        return super.matches(lowerCaseQuery) || 
               issueDate.toLowerCase().contains(lowerCaseQuery);
    }

    // --- Getters and Setters ---
    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }
}
