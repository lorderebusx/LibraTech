package models;

import java.time.LocalDate;

/**
 * Represents a loan transaction.
 * This class demonstrates COMPOSITION, as a Loan is "composed of"
 * a LibraryItem and a Member.
 */
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

    // --- Getters ---
    public LibraryItem getItem() {
        return item;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
}
