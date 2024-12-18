package org.poo.main;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class Card {
    private String cardNumber;
    private CardStatus status;
    private CardTransactionStrategy transactionStrategy;

    public Card(String cardNumber, CardStatus status) {
        this.cardNumber = cardNumber;
        this.status = status;
    }

    public Card(String cardNumber, CardStatus status, CardTransactionStrategy strategy) {
        this.cardNumber = cardNumber;
        this.status = status;
        this.transactionStrategy = strategy;
    }

    public TransactionResult performTransaction(double amount) {
        if (this.status != CardStatus.ACTIVE) {
            return new TransactionResult(false, false);
            // Inactive or frozen cards cannot process transactions.
        }
        return transactionStrategy.processTransaction(amount);
    }


}
