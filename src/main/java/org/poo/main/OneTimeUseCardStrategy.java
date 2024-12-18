package org.poo.main;

public class OneTimeUseCardStrategy implements CardTransactionStrategy {
    private boolean used = false;

    @Override
    public TransactionResult processTransaction(double amount) {
        if (!used && amount > 0) {
            used = true; // Mark card as used
            return new TransactionResult(true, true); // Indicate that the card should be replaced
        }
        return new TransactionResult(false, false);
    }
}