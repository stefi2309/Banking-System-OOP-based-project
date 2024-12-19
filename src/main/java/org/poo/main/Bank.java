package org.poo.main;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.UserInput;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Bank {
    private static List<User> users = new ArrayList<>();
    private static int timestamp = 0;
    Bank() { }

    /**
     * Initializes users from a list of user input data
     * @param userInputList
     */
    public static void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getFirstName(), userInput.getLastName(),
                    userInput.getEmail()));
        }
    }

    public static List<User> getUsers() {
        return new ArrayList<>(users);
    }

    /**
     * @param email
     * @return The User object if found, otherwise null
     */
    public static User getUser(final String email) {
        return users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a new account to a user by email
     * @param email
     * @param currency
     * @param accountType
     * @throws IllegalArgumentException if no user is found with the given email
     */
    public static void addAccount(final String email, final String currency,
                                  final String accountType) {
        User user = getUser(email);
        if (user != null) {
            String iban = Utils.generateIBAN();
            Account account = new Account(iban, 0.0, currency, accountType);
            user.addAccount(account);
        } else {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
    }

    /**
     * Creates a new card and adds it to a user's account
     * @param email
     * @param iban
     * @param type
     * @throws IllegalArgumentException if no user or account is found for the provided identifiers
     */
    public static void createCard(final String email, final String iban, final CardType type) {
        User user = getUser(email);
        if (user == null) {
            throw new IllegalArgumentException("No user found with email: " + email);
        }

        Account account = user.getAccount(iban);

        Card card = CardFactory.createCard(Utils.generateCardNumber(), CardStatus.ACTIVE, type);
        account.addCard(card);
    }

    /**
     * Adds funds to an account identified by IBAN
     * @param iban
     * @param amount
     * @throws IllegalArgumentException if no account is found with the provided IBAN
     */
    public static void addFunds(final String iban, final double amount) {
        for (User user : users) {
            Account account = user.getAccounts().stream()
                    .filter(a -> a.getIban().equals(iban))
                    .findFirst()
                    .orElse(null);
            if (account != null) {
                account.deposit(amount);
                return;
            }
        }
        throw new IllegalArgumentException("Account not found with IBAN: " + iban);
    }

    /**
     * Deletes an account from a user identified by email and account IBAN
     * @param email
     * @param iban
     * @throws IllegalArgumentException if no user or account is found with the provided identifiers
     */
    public static void deleteAccount(final String email, final String iban) {
        User user = getUser(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
        Account account = user.getAccounts().stream()
                .filter(a -> a.getIban().equals(iban))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Account not found with IBAN: " + iban));

        user.getAccounts().remove(account);
        account.getCards().clear();
    }

    /**
     * Deletes a card from any account by its card number.
     * @param cardNumber
     * @throws IllegalArgumentException if the card is not found.
     */
    public static void deleteCard(final String cardNumber) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(cardNumber)) {
                        account.removeCard(cardNumber);
                        return;
                    }
                }
            }
        }
        throw new IllegalArgumentException("Card not found with card number: " + cardNumber);
    }

    /**
     * Sets a minimum balance for an account identified by its IBAN
     * @param iban
     * @param minBalance
     * @throws IllegalArgumentException if the account is not found
     */
    public static void setMinimumBalance(final String iban, final double minBalance) {
        for (User user : users) {
            Account account = user.getAccounts().stream()
                    .filter(a -> a.getIban().equals(iban))
                    .findFirst()
                    .orElse(null);
            if (account != null) {
                account.setMinBalance(minBalance);
                return;
            }
        }
        throw new IllegalArgumentException("Account not found with IBAN: " + iban);
    }

    /**
     * Transfers funds between two accounts using either IBAN or alias identifiers
     * Also handles currency conversion if the accounts are in different currencies
     * @param senderIdentifier IBAN or alias
     * @param receiverIdentifier IBAN or alias
     * @param amount
     * @param description
     * @throws IllegalArgumentException
     */
    public static void transferFunds(final String senderIdentifier, final String receiverIdentifier,
                                     final double amount, final String description)
            throws IllegalArgumentException {
        Account senderAccount = findAccount(senderIdentifier);
        Account receiverAccount = findAccount(receiverIdentifier);

        if (senderAccount == null) {
            throw new IllegalArgumentException("Sender account not found");
        }
        if (receiverAccount == null) {
            throw new IllegalArgumentException("Receiver account not found");
        }

        if (senderAccount.getCurrency().equals(receiverAccount.getCurrency())) {
            senderAccount.payment(amount);
            receiverAccount.deposit(amount);
        } else {
            double convertedAmount = Exchange.convert(senderAccount.getCurrency(),
                    receiverAccount.getCurrency(), amount);
            senderAccount.payment(amount);
            receiverAccount.deposit(convertedAmount);
        }
    }

    /**
     * Associates an alias with a specific account owned by a user
     * @param email
     * @param iban
     * @param alias
     * @throws IllegalArgumentException If no user or account is found with the provided identifiers
     */
    public static void setAccountAlias(final String email, final String iban, final String alias) {
        User user = getUser(email);
        if (user != null) {
            Account account = user.getAccount(iban);
            if (account != null) {
                account.setAlias(alias);
            } else {
                throw new IllegalArgumentException("Account not found with IBAN: " + iban);
            }
        } else {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
    }

    /**
     * @param identifier IBAN or alias
     * @return The found  Account or null if no account matches the identifier
     */
    private static Account findAccount(final String identifier) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(identifier) || account.getAlias().equals(identifier)) {
                    return account;
                }
            }
        }
        return null;
    }

    /**
     * Resets the state of the bank, clearing all users and resetting the timestamp.
     */
    public static void reset() {
        users = new ArrayList<>();
        timestamp = 0;
    }

}
