package org.poo.main;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class Card {
    private String cardNumber;
    private CardStatus status;
    private CardTransactionStrategy transactionStrategy;

    public Card(final String cardNumber, final CardStatus status) {
        this.cardNumber = cardNumber;
        this.status = status;
    }

    public Card(final String cardNumber, final CardStatus status,
                final CardTransactionStrategy strategy) {
        this.cardNumber = cardNumber;
        this.status = status;
        this.transactionStrategy = strategy;
    }

    /**
     * Performs a transaction on the card
     * Validates the transaction using the card's transaction strategy
     * @param amount The amount to be transacted
     * @return A TransactionResult
     */
    public TransactionResult performTransaction(final double amount) {
        if (this.status != CardStatus.ACTIVE) {
            return new TransactionResult(false, false);
        }
        return transactionStrategy.processTransaction(amount);
    }


}
