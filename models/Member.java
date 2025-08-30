package models;

import java.util.ArrayList;
import java.util.List;
import interfaces.Searchable;

/**
 * Represents a library member who can borrow items.
 * Inherits from User and implements Searchable.
 */
public class Member extends User implements Searchable {
    private String memberId;
    private List<Loan> loans;

    public Member(String name, String memberId) {
        super(name);
        this.memberId = memberId;
        this.loans = new ArrayList<>();
    }
    
    public void display() {
        System.out.println("Member Name: " + getName());
        System.out.println("Member ID: " + memberId);
    }

    @Override
    public boolean matches(String query) {
        String lowerCaseQuery = query.toLowerCase();
        return getName().toLowerCase().contains(lowerCaseQuery) ||
               memberId.toLowerCase().contains(lowerCaseQuery);
    }

    // --- Getters and Setters ---
    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public List<Loan> getLoans() {
        return loans;
    }
}
