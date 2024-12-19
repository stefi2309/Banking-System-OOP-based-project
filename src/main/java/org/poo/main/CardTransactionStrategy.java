package org.poo.main;

public interface CardTransactionStrategy {
    /**
     * Processes a transaction with a specified amount for a card
     * @param amount
     * @return A TransactionResult indicating the success or failure
     */
    TransactionResult processTransaction(double amount);
}

