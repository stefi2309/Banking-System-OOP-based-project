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

    public static void setUsers(List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getFirstName(), userInput.getLastName(), userInput.getEmail()));
        }
    }

    public static List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public static User getUser(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public static void addAccount(String email, String currency, String accountType) {
        User user = getUser(email);
        if (user != null) {
            String IBAN = Utils.generateIBAN();
            Account account = new Account(IBAN, 0.0, currency, accountType);
            user.addAccount(account);
        } else {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
    }

    public static void createCard(String email, String IBAN) {
        User user = getUser(email);
        if (user == null) {
            throw new IllegalArgumentException("No user found with email: " + email);
        }

        Account account = user.getAccounts().stream()
                .filter(a -> a.getIBAN().equals(IBAN))
                .findFirst()
                .orElse(null);

        if (account == null) {
            throw new IllegalArgumentException("No account found with IBAN: " + IBAN);
        }

        String cardNumber = Utils.generateCardNumber();
        Card newCard = new Card(cardNumber, "active");
        account.addCard(newCard);
    }

    public static void addFunds(String IBAN, double amount) {
        for (User user : users) {
            Account account = user.getAccounts().stream()
                    .filter(a -> a.getIBAN().equals(IBAN))
                    .findFirst()
                    .orElse(null);
            if (account != null) {
                account.deposit(amount);
                return;
            }
        }
        throw new IllegalArgumentException("Account not found with IBAN: " + IBAN);
    }


    public static void deleteAccount(String email, String IBAN) {
        User user = getUser(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
        Account account = user.getAccounts().stream()
                .filter(a -> a.getIBAN().equals(IBAN))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Account not found with IBAN: " + IBAN));

//        if (account.getBalance() != 0.0) {
//            throw new IllegalArgumentException("Account balance is not zero");
//        }

        user.getAccounts().remove(account);
        account.getCards().clear();
    }

    public static void reset() {
        users = new ArrayList<>();
        timestamp = 0;
    }

}