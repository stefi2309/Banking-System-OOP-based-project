package org.poo.main;

public class CardFactory {
    public static Card createCard(String cardNumber, CardStatus status, CardType type) {
        CardTransactionStrategy strategy = switch (type) {
            case REGULAR -> new RegularCardStrategy();
            case ONE_TIME -> new OneTimeUseCardStrategy();
        };
        return new Card(cardNumber, status, strategy);
    }
}

