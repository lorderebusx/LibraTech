import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import models.LibraryItem;
import models.Member;
import models.User;
import models.Loan;
import models.Fine;
import models.ItemStatus;
import interfaces.Searchable;

/**
 * Represents the core of the library system.
 * This class manages collections of items, members, and loans,
 * and contains the main business logic for library operations.
 * It acts as a Facade for the main application.
 */
public class Library {
    private List<LibraryItem> inventory = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<Loan> activeLoans = new ArrayList<>();
    private List<Fine> outstandingFines = new ArrayList<>();
    
    public static final int BORROWING_DAYS_LIMIT = 14;
    public static final double DAILY_FINE_AMOUNT = 0.25;

    // --- Item Management ---
    public void addLibraryItem(LibraryItem item) {
        inventory.add(item);
    }

    public LibraryItem findItemById(String itemId) {
        for (LibraryItem item : inventory) {
            if (item.getItemId().equalsIgnoreCase(itemId)) {
                return item;
            }
        }
        return null;
    }
    
    public void listAllItems() {
        if (inventory.isEmpty()) {
            System.out.println("No items in the library inventory.");
            return;
        }
        for (LibraryItem item : inventory) {
            item.display();
            System.out.println("---");
        }
    }
    
    // --- Member Management ---
    public void registerMember(Member member) {
        users.add(member);
    }
    
    public Member findMemberById(String memberId) {
        for (User user : users) {
            // Check if the user is a Member and if the ID matches
            if (user instanceof Member && ((Member) user).getMemberId().equalsIgnoreCase(memberId)) {
                return (Member) user;
            }
        }
        return null;
    }

    public void listAllMembers() {
        boolean foundMembers = false;
        for (User user : users) {
            if (user instanceof Member) {
                ((Member) user).display();
                System.out.println("---");
                foundMembers = true;
            }
        }
        if (!foundMembers) {
            System.out.println("No members registered in the system.");
        }
    }
    
    // --- Borrowing and Returning Logic ---
    public void borrowItem(String memberId, String itemId) throws Exception {
        Member member = findMemberById(memberId);
        if (member == null) {
            throw new Exception("Member not found.");
        }

        LibraryItem item = findItemById(itemId);
        if (item == null) {
            throw new Exception("Item not found.");
        }

        if (item.getStatus() != ItemStatus.AVAILABLE) {
            throw new Exception("Item is currently not available for borrowing.");
        }

        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(BORROWING_DAYS_LIMIT);
        
        Loan newLoan = new Loan(item, member, borrowDate, dueDate);
        activeLoans.add(newLoan);
        item.setStatus(ItemStatus.BORROWED);
    }

    public void returnItem(String itemId) throws Exception {
        Loan loanToClose = null;
        for (Loan loan : activeLoans) {
            if (loan.getItem().getItemId().equalsIgnoreCase(itemId)) {
                loanToClose = loan;
                break;
            }
        }

        if (loanToClose == null) {
            throw new Exception("No active loan found for this item ID.");
        }

        // Check for fines
        LocalDate returnDate = LocalDate.now();
        if (returnDate.isAfter(loanToClose.getDueDate())) {
            long daysOverdue = ChronoUnit.DAYS.between(loanToClose.getDueDate(), returnDate);
            double fineAmount = daysOverdue * DAILY_FINE_AMOUNT;
            Fine newFine = new Fine(loanToClose, fineAmount);
            outstandingFines.add(newFine);
            System.out.println("Item is overdue. A fine of $" + String.format("%.2f", fineAmount) + " has been issued.");
        }

        loanToClose.getItem().setStatus(ItemStatus.AVAILABLE);
        activeLoans.remove(loanToClose);
    }
    
    // --- Reporting and Searching ---
    
    public void listOverdueItems() {
        boolean foundOverdue = false;
        LocalDate today = LocalDate.now();
        for (Loan loan : activeLoans) {
            if (today.isAfter(loan.getDueDate())) {
                System.out.println("Item: " + loan.getItem().getTitle() + " (ID: " + loan.getItem().getItemId() + ")");
                System.out.println("Member: " + loan.getMember().getName() + " (ID: " + loan.getMember().getMemberId() + ")");
                System.out.println("Due Date: " + loan.getDueDate());
                long daysOverdue = ChronoUnit.DAYS.between(loan.getDueDate(), today);
                System.out.println("Days Overdue: " + daysOverdue);
                System.out.println("---");
                foundOverdue = true;
            }
        }
        if (!foundOverdue) {
            System.out.println("No items are currently overdue.");
        }
    }
    
    public void search(String query) {
        System.out.println("Searching Items...");
        boolean found = false;
        for (LibraryItem item : inventory) {
            if (item.matches(query)) {
                item.display();
                System.out.println("---");
                found = true;
            }
        }

        System.out.println("\nSearching Members...");
        for (User user : users) {
             if (user instanceof Searchable && ((Searchable)user).matches(query)) {
                 if (user instanceof Member) {
                     ((Member)user).display();
                 }
                 System.out.println("---");
                 found = true;
             }
        }

        if (!found) {
            System.out.println("No results found for '" + query + "'.");
        }
    }

    // Getter for demo data setup
    public List<Loan> getActiveLoans() {
        return this.activeLoans;
    }
}
