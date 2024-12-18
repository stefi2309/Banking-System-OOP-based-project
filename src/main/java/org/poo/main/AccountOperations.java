package org.poo.main;

public interface AccountOperations {
    void deposit(double amount);
    void payment(double amount) throws IllegalArgumentException;
    void addCard(Card card);
}
