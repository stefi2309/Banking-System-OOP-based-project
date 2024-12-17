package org.poo.main;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Account {
    private String IBAN;
    private double balance;
    private String currency;
    private String accountType;
    private List<Card> cards;

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

    public void withdraw(double amount) {
        if (amount <= balance) {
            this.balance -= amount;
        } else {
            throw new IllegalArgumentException("Insufficient funds");
        }
    }

}