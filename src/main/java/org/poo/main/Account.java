package org.poo.main;

import lombok.Getter;
import lombok.Setter;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Account implements AccountOperations {
    private String IBAN;
    private double balance;
    private String currency;
    private String accountType;
    private List<Card> cards;
    private double minBalance = 0.0;

    public Account(String IBAN, double balance, String currency, String accountType) {
        this.IBAN = IBAN;
        this.balance = balance;
        this.currency = currency;
        this.accountType = accountType;
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public void payment(double amount) {
        if (amount <= balance) {
            this.balance -= amount;
        } else {
            throw new IllegalArgumentException("Insufficient funds");
        }
    }

    public void removeCard(String cardNumber) {
        cards.removeIf(card -> card.getCardNumber().equals(cardNumber));
    }

    public void performCardTransaction(String cardNumber, double amount) {
        for (Card card : cards) {
            if (card.getCardNumber().equals(cardNumber)) {
                TransactionResult result = card.performTransaction(amount);
                if (result.isSuccess()) {
                    if (result.shouldReplaceCard()) {
                        // Replace the one-time use card
                        cards.remove(card);
                        Card newCard = CardFactory.createCard(Utils.generateCardNumber(), CardStatus.ACTIVE, CardType.ONE_TIME);
                        addCard(newCard);
                    }
                    return;
                }
                break;
            }
        }
    }


}