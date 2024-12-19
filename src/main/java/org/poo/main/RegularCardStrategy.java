package org.poo.main;

public final class RegularCardStrategy implements CardTransactionStrategy {
    @Override
    public TransactionResult processTransaction(final double amount) {
        return new TransactionResult(amount > 0, false);
    }
}
