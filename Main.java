import java.util.Scanner;
import java.time.LocalDate;
import models.Book;
import models.Magazine;
import models.Member;
import models.Librarian;
import models.Loan;

/**
 * Main class for the Library Management System.
 * This class handles the command-line user interface and orchestrates the application flow.
 */
public class Main {
    private static Library library = new Library();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // --- Pre-populate the library with some data for demonstration ---
        setupInitialData();

        System.out.println("Welcome to the OOP Library Management System!");

        // For this example, we'll simulate a logged-in Librarian
        Librarian currentLibrarian = new Librarian("Admin", "L001");
        
        boolean isRunning = true;
        while (isRunning) {
            printMenu();
            System.out.print("Enter your choice: ");
            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    addMember();
                    break;
                case 3:
                    listAllItems();
                    break;
                case 4:
                    listAllMembers();
                    break;
                case 5:
                    borrowItem();
                    break;
                case 6:
                    returnItem();
                    break;
                case 7:
                    listOverdueItems();
                    break;
                case 8:
                    search();
                    break;
                case 0:
                    isRunning = false;
                    System.out.println("Thank you for using the Library Management System.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n--- Library Menu ---");
        System.out.println("1. Add a new Book");
        System.out.println("2. Register a new Member");
        System.out.println("3. List all Library Items");
        System.out.println("4. List all Members");
        System.out.println("5. Borrow an Item");
        System.out.println("6. Return an Item");
        System.out.println("7. List Overdue Items");
        System.out.println("8. Search for Item or Member");
        System.out.println("0. Exit");
        System.out.println("--------------------");
    }

    private static void addBook() {
        System.out.print("Enter Book Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();
        
        Book newBook = new Book(title, author, isbn);
        library.addLibraryItem(newBook);
        System.out.println("Book '" + title + "' added successfully!");
    }

    private static void addMember() {
        System.out.print("Enter Member Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Member ID (e.g., M003): ");
        String memberId = scanner.nextLine();

        Member newMember = new Member(name, memberId);
        library.registerMember(newMember);
        System.out.println("Member '" + name + "' registered successfully!");
    }

    private static void listAllItems() {
        System.out.println("\n--- All Library Items ---");
        library.listAllItems();
        System.out.println("-------------------------");
    }
    
    private static void listAllMembers() {
        System.out.println("\n--- All Library Members ---");
        library.listAllMembers();
        System.out.println("-------------------------");
    }

    private static void borrowItem() {
        System.out.print("Enter Member ID to borrow: ");
        String memberId = scanner.nextLine();
        System.out.print("Enter Item ID to borrow: ");
        String itemId = scanner.nextLine();

        try {
            library.borrowItem(memberId, itemId);
            System.out.println("Item successfully borrowed.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void returnItem() {
        System.out.print("Enter Item ID to return: ");
        String itemId = scanner.nextLine();

        try {
            library.returnItem(itemId);
            System.out.println("Item successfully returned.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void listOverdueItems() {
        System.out.println("\n--- Overdue Items ---");
        library.listOverdueItems();
        System.out.println("---------------------");
    }
    
    private static void search() {
        System.out.print("Enter search query (title, author, name, etc.): ");
        String query = scanner.nextLine();
        System.out.println("\n--- Search Results ---");
        library.search(query);
        System.out.println("----------------------");
    }

    /**
     * Helper method to create some initial data for the library.
     */
    private static void setupInitialData() {
        // Add some books
        library.addLibraryItem(new Book("The Hobbit", "J.R.R. Tolkien", "978-0-345-33968-3"));
        library.addLibraryItem(new Book("1984", "George Orwell", "978-0-452-28423-4"));
        library.addLibraryItem(new Book("Dune", "Frank Herbert", "978-0-441-01359-3"));

        // Add some magazines
        library.addLibraryItem(new Magazine("National Geographic", "August 2025"));
        library.addLibraryItem(new Magazine("Time", "July 2025"));

        // Register some members
        library.registerMember(new Member("Alice Smith", "M001"));
        library.registerMember(new Member("Bob Johnson", "M002"));

        // Simulate a past borrowing to demonstrate overdue feature
        try {
            // We manually create a loan with a past due date
            Member alice = library.findMemberById("M001");
            Book dune = (Book) library.findItemById("ITEM-003"); // Assuming Dune gets this ID
            if (alice != null && dune != null) {
                LocalDate pastBorrowDate = LocalDate.now().minusDays(20);
                LocalDate pastDueDate = pastBorrowDate.plusDays(Library.BORROWING_DAYS_LIMIT);
                Loan overdueLoan = new Loan(dune, alice, pastBorrowDate, pastDueDate);
                dune.setStatus(models.ItemStatus.BORROWED);
                library.getActiveLoans().add(overdueLoan);
            }
        } catch(Exception e) {
            // This might fail if IDs change, but it's okay for a demo.
            System.err.println("Could not create demo overdue loan. " + e.getMessage());
        }
    }
}
