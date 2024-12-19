package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;

public final class CommandRunner {
    private static ObjectMapper objectMapper = new ObjectMapper();

    private CommandRunner() { }

    /**
     * @param commandInput
     * @return JSON node containing a detailed list of users with their financial details
     */
    public static ObjectNode printUsers(final CommandInput commandInput) {
        ObjectNode responseNode = objectMapper.createObjectNode();
        ArrayNode usersArray = objectMapper.createArrayNode();

        for (User user : Bank.getUsers()) {
            ObjectNode userNode = objectMapper.createObjectNode();
            userNode.put("firstName", user.getFirstName());
            userNode.put("lastName", user.getLastName());
            userNode.put("email", user.getEmail());

            ArrayNode accountsArray = objectMapper.createArrayNode();
            for (Account account : user.getAccounts()) {
                ObjectNode accountNode = objectMapper.createObjectNode();
                accountNode.put("IBAN", account.getIban());
                accountNode.put("balance", account.getBalance());
                accountNode.put("currency", account.getCurrency());
                accountNode.put("type", account.getAccountType());

                ArrayNode cardsArray = objectMapper.createArrayNode();
                for (Card card : account.getCards()) {
                    ObjectNode cardNode = objectMapper.createObjectNode();
                    cardNode.put("cardNumber", card.getCardNumber());
                    cardNode.put("status", card.getStatus().toString().toLowerCase());
                    cardsArray.add(cardNode);
                }
                accountNode.set("cards", cardsArray);
                accountsArray.add(accountNode);
            }
            userNode.set("accounts", accountsArray);
            usersArray.add(userNode);
        }

        responseNode.put("command", commandInput.getCommand());
        responseNode.putPOJO("output", usersArray);
        responseNode.put("timestamp", commandInput.getTimestamp());
        return responseNode;
    }

    /**
     * Adds a new account to a specified user based on the provided command input
     * @param commandInput
     */
    public static void addAccount(final CommandInput commandInput) {
        Bank.addAccount(commandInput.getEmail(), commandInput.getCurrency(),
                commandInput.getAccountType());
    }

    /**
     * Creates a new card for a specified account
     * which could be either a regular or a one-time use card
     * @param commandInput
     */
    public static void createCard(final CommandInput commandInput) {
        try {
            CardType cardType = commandInput.getCommand().equals("createOneTimeCard")
                    ? CardType.ONE_TIME : CardType.REGULAR;
            Bank.createCard(commandInput.getEmail(), commandInput.getAccount(), cardType);
        } catch (IllegalArgumentException e) {
            System.err.println("Error creating card: " + e.getMessage());
        }
    }

    /**
     * Deposits funds into a specified account
     * @param commandInput
     */
    public static void addFunds(final CommandInput commandInput) {
        try {
            Bank.addFunds(commandInput.getAccount(), commandInput.getAmount());
        } catch (IllegalArgumentException e) {
            System.err.println("Error adding funds: " + e.getMessage());
        }
    }

    /**
     * @param commandInput
     * @return JSON node confirming the deletion along with a timestamp
     */
    public static ObjectNode deleteAccount(final CommandInput commandInput) {
        ObjectNode responseNode = objectMapper.createObjectNode();
        Bank.deleteAccount(commandInput.getEmail(), commandInput.getAccount());
        ObjectNode output = objectMapper.createObjectNode();
        output.put("success", "Account deleted");
        output.put("timestamp", commandInput.getTimestamp());

        responseNode.put("command", commandInput.getCommand());
        responseNode.set("output", output);
        responseNode.put("timestamp", commandInput.getTimestamp());
        return responseNode;
    }

    /**
     * Deletes a specified card from the system using the card number
     * @param commandInput
     */
    public static void deleteCard(final CommandInput commandInput) {
        try {
            Bank.deleteCard(commandInput.getCardNumber());
        } catch (IllegalArgumentException e) {
            System.err.println("Error deleting card: " + e.getMessage());
        }
    }

    /**
     * Sets a minimum balance for a specific account
     * @param commandInput
     */
    public static void setMinBalance(final CommandInput commandInput) {
        try {
            Bank.setMinimumBalance(commandInput.getAccount(), commandInput.getAmount());
        } catch (IllegalArgumentException e) {
            System.err.println("Error setting balance account: " + e.getMessage());
        }
    }

    /**
     * @param commandInput
     * @return JSON node with transaction results or error information
     */
    public static ObjectNode payOnline(final CommandInput commandInput) {
        ObjectNode responseNode = objectMapper.createObjectNode();
        try {
            User user = Bank.getUser(commandInput.getEmail());
            if (user == null) {
                throw new IllegalArgumentException("User not found");
            }
            Account account = user.getAccountByCardNumber(commandInput.getCardNumber());
            if (account == null) {
                // Create response for card not found
                ObjectNode outputNode = objectMapper.createObjectNode();
                outputNode.put("description", "Card not found");
                outputNode.put("timestamp", commandInput.getTimestamp());

                responseNode.put("command", commandInput.getCommand());
                responseNode.set("output", outputNode);
                responseNode.put("timestamp", commandInput.getTimestamp());
                return responseNode;
            }
            // Convert currency if necessary
            double amountInAccountCurrency = Exchange.convert(
                    commandInput.getCurrency(), account.getCurrency(), commandInput.getAmount());
            account.payment(amountInAccountCurrency);
        } catch (IllegalArgumentException ignored) {
        }
        return responseNode;
    }

    /**
     * Transfers funds from one account to another, converting currency if necessary
     * @param commandInput
     */
    public static void sendMoney(final CommandInput commandInput) {
         try {
            Bank.transferFunds(commandInput.getAccount(), commandInput.getReceiver(),
                    commandInput.getAmount(), commandInput.getDescription());
        } catch (IllegalArgumentException e) {
             System.err.println("Error sending money: " + e.getMessage());
         }
    }

    /**
     * Sets an alias for an account to simplify future transactions
     * @param commandInput
     */
    public static void setAlias(final CommandInput commandInput) {
        try {
            Bank.setAccountAlias(commandInput.getEmail(), commandInput.getAccount(),
                    commandInput.getAlias());
        } catch (IllegalArgumentException e) {
            System.err.println("Error setting alias: " + e.getMessage());
        }
    }

}
