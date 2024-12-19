package org.poo.main;

public class CardFactory {
    /**
     * @param cardNumber
     * @param status
     * @param type
     * @return A new Card instance
     */
    public static Card createCard(final String cardNumber, final CardStatus status,
                                  final CardType type) {
        CardTransactionStrategy strategy = switch (type) {
            case REGULAR -> new RegularCardStrategy();
            case ONE_TIME -> new OneTimeUseCardStrategy();
        };
        return new Card(cardNumber, status, strategy);
    }
}

