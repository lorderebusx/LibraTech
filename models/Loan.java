package models;

import java.time.LocalDate;
import java.util.List;

public class Loan {
    private LibraryItem item;
    private Member member;
    private LocalDate borrowDate;
    private LocalDate dueDate;

    public Loan(LibraryItem item, Member member, LocalDate borrowDate, LocalDate dueDate) {
        this.item = item;
        this.member = member;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    public LibraryItem getItem() { return item; }
    public Member getMember() { return member; }
    public LocalDate getDueDate() { return dueDate; }
    
    public String toCsvString() {
        final String DELIMITER = ";";
        return String.join(DELIMITER, item.getItemId(), member.getMemberId(), borrowDate.toString(), dueDate.toString());
    }

    public static Loan fromCsvString(String csvLine, List<LibraryItem> allItems, List<User> allUsers) {
        final String DELIMITER = ";";
        String[] parts = csvLine.split(DELIMITER);
        String itemId = parts[0];
        String memberId = parts[1];

        LibraryItem item = allItems.stream().filter(i -> i.getItemId().equals(itemId)).findFirst().orElse(null);
        Member member = allUsers.stream()
            .filter(u -> u instanceof Member && ((Member) u).getMemberId().equals(memberId))
            .map(u -> (Member) u).findFirst().orElse(null);

        if (item == null || member == null) return null;
        
        return new Loan(item, member, LocalDate.parse(parts[2]), LocalDate.parse(parts[3]));
    }
}

