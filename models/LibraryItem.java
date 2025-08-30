package models;

import interfaces.Searchable;
import java.util.List;

public abstract class LibraryItem implements Searchable {
    private static int nextId = 1;
    private String itemId;
    private String title;
    private ItemStatus status;

    public LibraryItem(String title) {
        this.itemId = "ITEM-" + String.format("%03d", nextId++);
        this.title = title;
        this.status = ItemStatus.AVAILABLE;
    }

    protected LibraryItem(String id, String title, ItemStatus status) {
        this.itemId = id;
        this.title = title;
        this.status = status;
    }

    public abstract void display();
    
    @Override
    public boolean matches(String query) {
        return title.toLowerCase().contains(query.toLowerCase());
    }

    public String getItemId() { return itemId; }
    public String getTitle() { return title; }
    public ItemStatus getStatus() { return status; }
    public void setStatus(ItemStatus status) { this.status = status; }

    public static void syncNextId(List<LibraryItem> items) {
        if (items.isEmpty()) {
            nextId = 1;
            return;
        }
        int maxId = items.stream()
            .map(item -> item.getItemId().replace("ITEM-", ""))
            .mapToInt(Integer::parseInt)
            .max()
            .orElse(0);
        nextId = maxId + 1;
    }
}

