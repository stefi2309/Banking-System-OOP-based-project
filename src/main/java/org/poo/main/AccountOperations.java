package org.poo.main;

public interface AccountOperations {
    /**
     * Deposits a specified amount into an account
     * @param amount
     */
    void deposit(double amount);

    /**
     * Withdraws a specified amount from an account, if sufficient funds are available
     * @param amount
     * @throws IllegalArgumentException
     * If the specified amount exceeds the account balance
     */
    void payment(double amount) throws IllegalArgumentException;

    /**
     * Adds a new card to an account
     * @param card The card to be added to the account
     */
    void addCard(Card card);
}
