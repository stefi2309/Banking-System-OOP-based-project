package org.poo.main;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public final class User {
    private String firstName;
    private String lastName;
    private String email;
    private List<Account> accounts;

    public User(final String firstName, final String lastName, final String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accounts = new ArrayList<>();
    }

    /**
     * Adds an account to this user's list of accounts
     * @param account The account to be added
     */
    public void addAccount(final Account account) {
        accounts.add(account);
    }

    /**
     * @param iban
     * @return The Account object if found, otherwise null
     */
    public Account getAccount(final String iban) {
        for (Account acc : accounts) {
            if (acc.getIban().equals(iban)) {
                return acc;
            }
        }
        return null;
    }

    /**
     * @param cardNumber
     * @return The Account object if found, otherwise null
     */
    public Account getAccountByCardNumber(final String cardNumber) {
        for (Account account : accounts) {
            if (account.getCards().stream().anyMatch(card ->
                    card.getCardNumber().equals(cardNumber))) {
                return account;
            }
        }
        return null;
    }

}
