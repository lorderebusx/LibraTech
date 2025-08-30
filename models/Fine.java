package models;

import java.time.LocalDate;

public class Fine {
    
    private final Loan associatedLoan;
    private final double amount;
    private boolean isPaid;
    @SuppressWarnings("unused")
    private final LocalDate dateAssessed;

    public Fine(Loan loan, double amount) {
        this.associatedLoan = loan;
        this.amount = amount;
        this.isPaid = false;
        this.dateAssessed = LocalDate.now();
    }

    public Member getMember() {
        return associatedLoan.getMember();
    }

    public double getAmount() {
        return amount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void markAsPaid() {
        this.isPaid = true;
    }
}

