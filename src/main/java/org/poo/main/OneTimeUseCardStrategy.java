package org.poo.main;

public final class OneTimeUseCardStrategy implements CardTransactionStrategy {
    private boolean used = false;

    @Override
    public TransactionResult processTransaction(final double amount) {
        if (!used && amount > 0) {
            used = true; // Mark card as used
            return new TransactionResult(true, true);
        }
        return new TransactionResult(false, false);
    }
}
