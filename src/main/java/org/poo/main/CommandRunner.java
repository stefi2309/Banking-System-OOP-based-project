package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;

public final class CommandRunner {
    private static ObjectMapper objectMapper = new ObjectMapper();

    private CommandRunner() {}

    public static ObjectNode printUsers(CommandInput commandInput) {
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
                accountNode.put("IBAN", account.getIBAN());
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

    public static void addAccount(CommandInput commandInput) {
        String typeOfAccount = commandInput.getAccountType();
        int type;
        if (typeOfAccount.equals("classic")){
            type = 1;
        } else {
            type = 2;
        }
        Bank.addAccount(commandInput.getEmail(), commandInput.getCurrency(), commandInput.getAccountType());
    }

//    public static void createCard(CommandInput commandInput) {
//        try {
//            Bank.createCard(commandInput.getEmail(), commandInput.getAccount());
//        } catch (IllegalArgumentException e) {
//            System.err.println("Error creating card: " + e.getMessage());
//        }
//    }

    public static void createCard(CommandInput commandInput) {
        try {
            CardType cardType = commandInput.getCommand().equals("createOneTimeCard") ? CardType.ONE_TIME : CardType.REGULAR;
            Bank.createCard(commandInput.getEmail(), commandInput.getAccount(), cardType);
        } catch (IllegalArgumentException e) {
            System.err.println("Error creating card: " + e.getMessage());
        }
    }


    public static void addFunds(CommandInput commandInput) {
        try {
            Bank.addFunds(commandInput.getAccount(), commandInput.getAmount());
        } catch (IllegalArgumentException e) {
            System.err.println("Error adding funds: " + e.getMessage());
        }
    }

    public static ObjectNode  deleteAccount(CommandInput commandInput) {
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

    public static void deleteCard(CommandInput commandInput) {
        try {
            Bank.deleteCard(commandInput.getCardNumber());
        } catch (IllegalArgumentException e) {
            System.err.println("Error deleting card: " + e.getMessage());
        }
    }

    public static void setMinBalance(CommandInput commandInput) {
        try {
            Bank.setMinimumBalance(commandInput.getAccount(), commandInput.getAmount());
        } catch (IllegalArgumentException e) {
            System.err.println("Error setting balance account: " + e.getMessage());
        }
    }

//    public static void payOnline(CommandInput commandInput) {
////        ObjectMapper objectMapper = new ObjectMapper();
////        ObjectNode responseNode = objectMapper.createObjectNode();
//
//        try {
//            User user = Bank.getUser(commandInput.getEmail());
//            if (user == null) {
//                throw new IllegalArgumentException("User not found");
//            }
//
//            Account account = user.getAccountByCardNumber(commandInput.getCardNumber());
////            if (account == null) {
////                // Create response for card not found
////                ObjectNode outputNode = objectMapper.createObjectNode();
////                outputNode.put("timestamp", commandInput.getTimestamp());
////                outputNode.put("description", "Card not found");
////
////                responseNode.put("command", commandInput.getCommand());
////                responseNode.set("output", outputNode);
////                responseNode.put("timestamp", commandInput.getTimestamp());
////                return responseNode;
////            }
//
//            // Convert currency and perform payment
//            double amountInCurrency = Exchange.convert(
//                    commandInput.getCurrency(), account.getCurrency(), commandInput.getAmount());
//
//            // Perform payment
//            //account.performCardTransaction(commandInput.getCardNumber(), amountInCurrency);
//            account.payment(amountInCurrency);
//
//        } catch (IllegalArgumentException e) {
//            // Log error or handle it as needed
//            System.err.println("Error paying online: " + e.getMessage());
//        }
//
//        // Return a node with no output if transaction succeeds
////        return null;
//    }

    public static ObjectNode payOnline(CommandInput commandInput) {
        ObjectMapper objectMapper = new ObjectMapper();
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

    public static void sendMoney(CommandInput commandInput) {
         try {
            Bank.transferFunds(commandInput.getAccount(), commandInput.getReceiver(), commandInput.getAmount(), commandInput.getDescription());
        } catch (IllegalArgumentException e) {
             System.err.println("Error sending money: " + e.getMessage());
         }
    }

}
