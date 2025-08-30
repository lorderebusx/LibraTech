import models.*;
import java.util.Scanner;

/**
 * The main entry point for the LibraTech application.
 * Handles all user console interaction and menu navigation.
 */
public class Main {

    private static Library library = new Library();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Data is now loaded from files by the Library's constructor.
        System.out.println("Welcome to the LibraTech Management System!");
        // We will add a login screen here in the next step. For now, it goes to the main menu.
        mainMenu();
        scanner.close();
    }

    private static void mainMenu() {
        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. List All Items");
            System.out.println("2. List All Members");
            System.out.println("3. Add New Item");
            System.out.println("4. Register New Member");
            System.out.println("5. Borrow Item");
            System.out.println("6. Return Item");
            System.out.println("7. List Active Loans");
            System.out.println("8. Search");
            System.out.println("0. Exit");
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
                    library.listAllItems();
                    break;
                case 2:
                    library.listAllMembers();
                    break;
                case 3:
                    addNewItemMenu();
                    break;
                case 4:
                    registerNewMember();
                    break;
                case 5:
                    borrowItem();
                    break;
                case 6:
                    returnItem();
                    break;
                case 7:
                    library.listActiveLoans();
                    break;
                case 8:
                    search();
                    break;
                case 0:
                    System.out.println("Thank you for using LibraTech. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addNewItemMenu() {
        System.out.println("\n--- Add New Item ---");
        System.out.println("1. Add Book");
        System.out.println("2. Add Magazine");
        System.out.println("3. Add Article");
        System.out.print("Enter item type: ");
        
        int type;
        try {
            type = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid type. Returning to main menu.");
            return;
        }

        System.out.print("Enter Title: ");
        String title = scanner.nextLine();

        switch (type) {
            case 1:
                System.out.print("Enter Author: ");
                String author = scanner.nextLine();
                System.out.print("Enter ISBN: ");
                String isbn = scanner.nextLine();
                library.addLibraryItem(new Book(title, author, isbn));
                break;
            case 2:
                System.out.print("Enter Issue Date (e.g., August 2025): ");
                String issueDate = scanner.nextLine();
                library.addLibraryItem(new Magazine(title, issueDate));
                break;
            case 3:
                System.out.print("Enter Author: ");
                String articleAuthor = scanner.nextLine();
                System.out.print("Enter Publication: ");
                String publication = scanner.nextLine();
                library.addLibraryItem(new Article(title, articleAuthor, publication));
                break;
            default:
                System.out.println("Invalid item type.");
                break;
        }
    }

    private static void registerNewMember() {
        System.out.println("\n--- Register New Member ---");
        System.out.print("Enter member name: ");
        String name = scanner.nextLine();
        System.out.print("Enter a password for the member: ");
        String password = scanner.nextLine();
        library.registerMember(new Member(name, password));
    }

    private static void borrowItem() {
        System.out.println("\n--- Borrow Item ---");
        System.out.print("Enter Member ID (e.g., M001): ");
        String memberId = scanner.nextLine();
        System.out.print("Enter Item ID (e.g., ITEM-001): ");
        String itemId = scanner.nextLine();
        library.borrowItem(memberId, itemId);
    }

    private static void returnItem() {
        System.out.println("\n--- Return Item ---");
        System.out.print("Enter Item ID of the item being returned: ");
        String itemId = scanner.nextLine();
        library.returnItem(itemId);
    }
    
    private static void search() {
        System.out.println("\n--- Search Library ---");
        System.out.print("Enter search query: ");
        String query = scanner.nextLine();
        library.search(query);
    }
}

