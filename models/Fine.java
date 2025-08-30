package models;

/**
 * Represents a fine assessed for an overdue loan.
 * Demonstrates COMPOSITION.
 */
public class Fine {
    private Loan associatedLoan;
    private double fineAmount;
    private boolean isPaid;

    public Fine(Loan associatedLoan, double fineAmount) {
        this.associatedLoan = associatedLoan;
        this.fineAmount = fineAmount;
        this.isPaid = false; // Fines are unpaid by default
    }

    // --- Getters and Setters ---
    public Loan getAssociatedLoan() {
        return associatedLoan;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }
}
