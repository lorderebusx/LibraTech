package models;

public class Magazine extends LibraryItem {
    private String issueDate;

    public Magazine(String title, String issueDate) {
        super(title);
        this.issueDate = issueDate;
    }

    private Magazine(String id, String title, String issueDate, ItemStatus status) {
        super(id, title, status);
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

    public String toCsvString() {
        final String DELIMITER = ";";
        return String.join(DELIMITER, getItemId(), getTitle(), issueDate, getStatus().toString());
    }

    public static Magazine fromCsvString(String csvLine) {
        final String DELIMITER = ";";
        String[] parts = csvLine.split(DELIMITER);
        return new Magazine(parts[0], parts[1], parts[2], ItemStatus.valueOf(parts[3]));
    }
}

