package models;

/**
 * Abstract base class for all users of the library system.
 * Demonstrates ABSTRACTION.
 */
public abstract class User {
    private String name;

    public User(String name) {
        this.name = name;
    }

    // --- Getters and Setters ---
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
