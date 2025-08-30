package models;

public class Librarian extends User {
    private String employeeId;

    public Librarian(String name, String password, String employeeId) {
        super(name, password);
        this.employeeId = employeeId;
    }

    public String getEmployeeId() { return employeeId; }

    public String toCsvString() {
        final String DELIMITER = ";";
        return String.join(DELIMITER, employeeId, getName(), getPassword());
    }

    public static Librarian fromCsvString(String csvLine) {
        final String DELIMITER = ";";
        String[] parts = csvLine.split(DELIMITER);
        return new Librarian(parts[1], parts[2], parts[0]);
    }
}

