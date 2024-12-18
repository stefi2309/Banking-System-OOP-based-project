package org.poo.main;

public class RegularCardStrategy implements CardTransactionStrategy {
    @Override
    public TransactionResult processTransaction(double amount) {
        return new TransactionResult(amount > 0, false);
    }
}
