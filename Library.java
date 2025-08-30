import models.*;
import data.*;
import interfaces.Searchable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The core engine of the system. This class acts as a facade,
 * managing all data and business logic, and uses DataManager for persistence.
 */
public class Library {

    private List<LibraryItem> inventory;
    private List<User> users;
    private List<Loan> activeLoans;
    private List<Fine> outstandingFines;
    private DataManager dataManager;

    public Library() {
        this.dataManager = new DataManager();
        this.outstandingFines = new ArrayList<>(); // Fines are calculated at runtime
        
        // Load all data from files
        this.inventory = dataManager.loadLibraryItems();
        this.users = dataManager.loadUsers();
        this.activeLoans = dataManager.loadLoans(this.inventory, this.users);

        // Sync static ID counters to prevent duplicates after loading
        LibraryItem.syncNextId(inventory);
        Member.syncNextId(users);
    }

    // --- Item Management ---
    public void addLibraryItem(LibraryItem item) {
        inventory.add(item);
        dataManager.saveLibraryItems(inventory);
        System.out.println("Successfully added: " + item.getTitle());
    }

    public LibraryItem findItemById(String itemId) {
        return inventory.stream()
            .filter(item -> item.getItemId().equalsIgnoreCase(itemId))
            .findFirst()
            .orElse(null);
    }

    public void listAllItems() {
        if (inventory.isEmpty()) {
            System.out.println("The library inventory is empty.");
            return;
        }
        inventory.forEach(item -> {
            item.display();
            System.out.println("--------------------");
        });
    }

    // --- Member Management ---
    public void registerMember(Member member) {
        users.add(member);
        dataManager.saveUsers(users);
        System.out.println("Successfully registered member: " + member.getName() + " with ID " + member.getMemberId());
    }

    public Member findMemberById(String memberId) {
        return users.stream()
            .filter(user -> user instanceof Member && ((Member) user).getMemberId().equalsIgnoreCase(memberId))
            .map(user -> (Member) user)
            .findFirst()
            .orElse(null);
    }

    public void listAllMembers() {
        boolean found = false;
        for (User user : users) {
            if (user instanceof Member) {
                ((Member) user).display();
                System.out.println("--------------------");
                found = true;
            }
        }
        if (!found) {
            System.out.println("No members have been registered.");
        }
    }

    // --- Loan Management ---
    public void borrowItem(String memberId, String itemId) {
        Member member = findMemberById(memberId);
        if (member == null) {
            System.out.println("Error: No member found with ID " + memberId);
            return;
        }

        LibraryItem item = findItemById(itemId);
        if (item == null) {
            System.out.println("Error: No item found with ID " + itemId);
            return;
        }

        if (item.getStatus() != ItemStatus.AVAILABLE) {
            System.out.println("Error: Item '" + item.getTitle() + "' is currently unavailable.");
            return;
        }

        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(14);
        Loan newLoan = new Loan(item, member, borrowDate, dueDate);
        activeLoans.add(newLoan);
        item.setStatus(ItemStatus.BORROWED);

        dataManager.saveLibraryItems(inventory);
        dataManager.saveLoans(activeLoans);
        System.out.println("Successfully loaned '" + item.getTitle() + "' to " + member.getName() + ".");
        System.out.println("Due Date: " + dueDate);
    }
    
    public void returnItem(String itemId) {
        Optional<Loan> loanToCloseOpt = activeLoans.stream()
            .filter(loan -> loan.getItem().getItemId().equalsIgnoreCase(itemId))
            .findFirst();

        if (!loanToCloseOpt.isPresent()) {
            System.out.println("Error: No active loan found for item ID " + itemId);
            return;
        }

        Loan loanToClose = loanToCloseOpt.get();
        LibraryItem item = loanToClose.getItem();
        item.setStatus(ItemStatus.AVAILABLE);

        LocalDate returnDate = LocalDate.now();
        if (returnDate.isAfter(loanToClose.getDueDate())) {
            long daysOverdue = ChronoUnit.DAYS.between(loanToClose.getDueDate(), returnDate);
            double fineAmount = daysOverdue * 0.25;
            Fine newFine = new Fine(loanToClose, fineAmount);
            outstandingFines.add(newFine);
            System.out.println("Item is overdue by " + daysOverdue + " days. A fine of $" + String.format("%.2f", fineAmount) + " has been assessed.");
        }

        activeLoans.remove(loanToClose);
        dataManager.saveLibraryItems(inventory);
        dataManager.saveLoans(activeLoans);
        System.out.println("Successfully returned '" + item.getTitle() + "'.");
    }

    public void listActiveLoans() {
        if (activeLoans.isEmpty()) {
            System.out.println("There are no items currently on loan.");
            return;
        }
        System.out.println("--- Active Loans ---");
        activeLoans.forEach(loan -> {
            System.out.println("Item: " + loan.getItem().getTitle() + " (ID: " + loan.getItem().getItemId() + ")");
            System.out.println("Member: " + loan.getMember().getName() + " (ID: " + loan.getMember().getMemberId() + ")");
            System.out.println("Due Date: " + loan.getDueDate());
            System.out.println("--------------------");
        });
    }
    
    // --- Search ---
    public void search(String query) {
        System.out.println("\n--- Search Results for '" + query + "' ---");
        boolean found = false;

        for (LibraryItem item : inventory) {
            if (item.matches(query)) {
                item.display();
                System.out.println("--------------------");
                found = true;
            }
        }
        
        for (User user : users) {
            if (user instanceof Searchable && ((Searchable) user).matches(query)) {
                if (user instanceof Member) ((Member)user).display();
                System.out.println("--------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No items or members found matching your query.");
        }
    }
}

