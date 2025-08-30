package models;

import interfaces.Searchable;

/**
 * Abstract base class for all items in the library.
 * It defines common properties and behaviors, like ID, title, and status.
 * It also implements the Searchable interface.
 * This demonstrates ABSTRACTION.
 */
public abstract class LibraryItem implements Searchable {
    private static int nextId = 1;
    private String itemId;
    private String title;
    private ItemStatus status;

    public LibraryItem(String title) {
        this.itemId = "ITEM-" + String.format("%03d", nextId++);
        this.title = title;
        this.status = ItemStatus.AVAILABLE; // Default status
    }

    // --- Abstract method ---
    // Each subclass must provide its own way of displaying details.
    // This demonstrates POLYMORPHISM.
    public abstract void display();
    
    // --- Common method for Searchable interface ---
    @Override
    public boolean matches(String query) {
        return title.toLowerCase().contains(query.toLowerCase());
    }

    // --- Getters and Setters (Encapsulation) ---
    public String getItemId() {
        return itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }
}
