package data;

import models.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages all data persistence operations.
 * This class is responsible for reading from and writing to CSV files.
 * It encapsulates all file I/O logic, separating it from the main library business logic.
 */
@SuppressWarnings("unused")
public class DataManager {

    private static final String DATA_DIRECTORY = "data";
    private static final String BOOKS_FILE = DATA_DIRECTORY + "/books.csv";
    private static final String MAGAZINES_FILE = DATA_DIRECTORY + "/magazines.csv";
    private static final String ARTICLES_FILE = DATA_DIRECTORY + "/articles.csv";
    private static final String MEMBERS_FILE = DATA_DIRECTORY + "/members.csv";
    private static final String LIBRARIANS_FILE = DATA_DIRECTORY + "/librarians.csv";
    private static final String LOANS_FILE = DATA_DIRECTORY + "/loans.csv";

    public DataManager() {
        // Ensure the data directory exists
        try {
            Files.createDirectories(Paths.get(DATA_DIRECTORY));
        } catch (IOException e) {
            System.err.println("Error creating data directory: " + e.getMessage());
        }
    }

    // Generic file writing method
    private <T> void writeToFile(String filePath, List<T> items, ToCsvString<T> converter) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (T item : items) {
                writer.println(converter.convert(item));
            }
        } catch (IOException e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }

    // Generic file reading method
    private <T> List<T> readFromFile(String filePath, FromCsvString<T> converter) {
        List<T> items = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return items; // Return empty list if file doesn't exist
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    items.add(converter.convert(line));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from file " + filePath + ": " + e.getMessage());
        }
        return items;
    }

    // --- Public Save Methods ---

    public void saveLibraryItems(List<LibraryItem> items) {
        writeToFile(BOOKS_FILE, items.stream().filter(i -> i instanceof Book).map(i -> (Book) i).collect(Collectors.toList()), Book::toCsvString);
        writeToFile(MAGAZINES_FILE, items.stream().filter(i -> i instanceof Magazine).map(i -> (Magazine) i).collect(Collectors.toList()), Magazine::toCsvString);
        writeToFile(ARTICLES_FILE, items.stream().filter(i -> i instanceof Article).map(i -> (Article) i).collect(Collectors.toList()), Article::toCsvString);
    }

    public void saveUsers(List<User> users) {
        writeToFile(MEMBERS_FILE, users.stream().filter(u -> u instanceof Member).map(u -> (Member) u).collect(Collectors.toList()), Member::toCsvString);
        writeToFile(LIBRARIANS_FILE, users.stream().filter(u -> u instanceof Librarian).map(u -> (Librarian) u).collect(Collectors.toList()), Librarian::toCsvString);
    }

    public void saveLoans(List<Loan> loans) {
        writeToFile(LOANS_FILE, loans, Loan::toCsvString);
    }

    // --- Public Load Methods ---

    public List<LibraryItem> loadLibraryItems() {
        List<LibraryItem> items = new ArrayList<>();
        items.addAll(readFromFile(BOOKS_FILE, Book::fromCsvString));
        items.addAll(readFromFile(MAGAZINES_FILE, Magazine::fromCsvString));
        items.addAll(readFromFile(ARTICLES_FILE, Article::fromCsvString));
        return items;
    }

    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        users.addAll(readFromFile(MEMBERS_FILE, Member::fromCsvString));
        users.addAll(readFromFile(LIBRARIANS_FILE, Librarian::fromCsvString));
        return users;
    }

    public List<Loan> loadLoans(List<LibraryItem> allItems, List<User> allUsers) {
        return readFromFile(LOANS_FILE, line -> Loan.fromCsvString(line, allItems, allUsers));
    }

    // --- Functional interfaces for converters ---
    @FunctionalInterface
    interface ToCsvString<T> {
        String convert(T item);
    }

    @FunctionalInterface
    interface FromCsvString<T> {
        T convert(String csvLine);
    }
}
