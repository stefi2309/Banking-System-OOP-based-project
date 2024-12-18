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

    public static void createCard(String email, String IBAN, CardType type) {
        User user = getUser(email);
        if (user == null) {
            throw new IllegalArgumentException("No user found with email: " + email);
        }

        Account account = user.getAccount(IBAN);

        Card card = CardFactory.createCard(Utils.generateCardNumber(), CardStatus.ACTIVE, type);
        account.addCard(card);
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

    public static void deleteCard(String cardNumber) {
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

    public static void setMinimumBalance(String IBAN, double minBalance) {
        for (User user : users) {
            Account account = user.getAccounts().stream()
                    .filter(a -> a.getIBAN().equals(IBAN))
                    .findFirst()
                    .orElse(null);
            if (account != null) {
                account.setMinBalance(minBalance);
                return;
            }
        }
        throw new IllegalArgumentException("Account not found with IBAN: " + IBAN);
    }

    private static Account findAccountByIBAN(String IBAN) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().equals(IBAN)) {
                    return account;
                }
            }
        }
        return null;
    }

    public static void transferFunds(String senderIBAN, String receiverIBAN, double amount, String description) throws IllegalArgumentException {
        Account senderAccount = findAccountByIBAN(senderIBAN);
        Account receiverAccount = findAccountByIBAN(receiverIBAN);

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
            double convertedAmount = Exchange.convert(senderAccount.getCurrency(), receiverAccount.getCurrency(), amount);
            //double convertedAmount = Exchange.convert(receiverAccount.getCurrency(), senderAccount.getCurrency(), amount);
            senderAccount.payment(amount);
            receiverAccount.deposit(convertedAmount);
        }
    }

    public static void reset() {
        users = new ArrayList<>();
        timestamp = 0;
    }

}