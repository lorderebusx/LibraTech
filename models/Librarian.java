package models;

/**
 * Represents a librarian with administrative privileges.
 * Inherits from User.
 */
public class Librarian extends User {
    private String employeeId;

    public Librarian(String name, String employeeId) {
        super(name);
        this.employeeId = employeeId;
    }

    // --- Getters and Setters ---
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
