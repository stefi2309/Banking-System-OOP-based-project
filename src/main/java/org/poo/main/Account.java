package org.poo.main;

import lombok.Getter;
import lombok.Setter;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Account implements AccountOperations {
    private String iban;
    private double balance;
    private String currency;
    private String accountType;
    private List<Card> cards;
    private double minBalance = 0.0;
    private String alias = "";

    public Account(final String iban, final double balance, final String currency,
                   final String accountType) {
        this.iban = iban;
        this.balance = balance;
        this.currency = currency;
        this.accountType = accountType;
        this.cards = new ArrayList<>();
    }

    /**
     * Adds a card to the account
     * @param card The card to be added
     */
    public void addCard(final Card card) {
        cards.add(card);
    }

    /**
     * Deposits a specified amount into the account
     * @param amount The amount to be added to the account's balance
     */
    public void deposit(final double amount) {
        this.balance += amount;
    }

    /**
     * Makes a payment from the account
     * @param amount The amount to be deducted from the account's balance
     * @throws IllegalArgumentException if the amount is greater than the current balance
     */
    public void payment(final double amount) {
        if (amount <= balance) {
            this.balance -= amount;
        } else {
            throw new IllegalArgumentException("Insufficient funds");
        }
    }

    /**
     * Removes a card from the account by its card number
     * @param cardNumber The number of the card to be removed
     */
    public void removeCard(final String cardNumber) {
        cards.removeIf(card -> card.getCardNumber().equals(cardNumber));
    }

    /**
     * Performs a transaction using a specific card linked to this account
     * @param cardNumber The card number to be used for the transaction
     * @param amount The amount of the transaction
     */
    public void performCardTransaction(final String cardNumber, final double amount) {
        for (Card card : cards) {
            if (card.getCardNumber().equals(cardNumber)) {
                TransactionResult result = card.performTransaction(amount);
                if (result.isSuccess()) {
                    if (result.shouldReplaceCard()) {
                        cards.remove(card);
                        Card newCard = CardFactory.createCard(Utils.generateCardNumber(),
                                CardStatus.ACTIVE, CardType.ONE_TIME);
                        addCard(newCard);
                    }
                    return;
                }
                break;
            }
        }
    }
}
