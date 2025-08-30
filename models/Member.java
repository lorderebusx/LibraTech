package models;

import interfaces.Searchable;
import java.util.List;

public class Member extends User implements Searchable {
    private static int nextId = 1;
    private String memberId;

    public Member(String name, String password) {
        super(name, password);
        this.memberId = "M" + String.format("%03d", nextId++);
    }

    private Member(String id, String name, String password) {
        super(name, password);
        this.memberId = id;
    }
    
    public void display() {
        System.out.println("Member Name: " + getName());
        System.out.println("Member ID: " + memberId);
    }

    @Override
    public boolean matches(String query) {
        String lcQuery = query.toLowerCase();
        return getName().toLowerCase().contains(lcQuery) || memberId.toLowerCase().contains(lcQuery);
    }
    
    public String getMemberId() { return memberId; }

    public String toCsvString() {
        final String DELIMITER = ";";
        return String.join(DELIMITER, memberId, getName(), getPassword());
    }

    public static Member fromCsvString(String csvLine) {
        final String DELIMITER = ";";
        String[] parts = csvLine.split(DELIMITER);
        return new Member(parts[0], parts[1], parts[2]);
    }

    public static void syncNextId(List<User> users) {
        if (users.isEmpty()) {
            nextId = 1;
            return;
        }
        int maxId = users.stream()
            .filter(u -> u instanceof Member)
            .map(u -> ((Member) u).getMemberId().replace("M", ""))
            .mapToInt(Integer::parseInt)
            .max()
            .orElse(0);
        nextId = maxId + 1;
    }
}

